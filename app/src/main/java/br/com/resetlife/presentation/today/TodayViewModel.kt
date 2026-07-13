package br.com.resetlife.presentation.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.resetlife.data.today.PriorityStore
import br.com.resetlife.domain.today.AddPriorityResult
import br.com.resetlife.domain.today.CompletionResult
import br.com.resetlife.domain.today.DailyPriorities
import br.com.resetlife.domain.today.PriorityItem
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class TodayUiState(
    val priorityInput: String = "",
    val priorities: List<PriorityItem> = emptyList(),
    val feedback: TodayFeedback? = null,
    val isLoading: Boolean = true,
    val isSavingPriority: Boolean = false,
) {
    val activePriorityCount: Int
        get() = activePriorities.size

    val activePriorities: List<PriorityItem>
        get() = priorities.filterNot(PriorityItem::isCompleted)

    val completedPriorities: List<PriorityItem>
        get() = priorities.filter(PriorityItem::isCompleted)

    val isAddPriorityEnabled: Boolean
        get() = !isLoading && !isSavingPriority && priorityInput.isNotBlank() &&
            activePriorityCount < DailyPriorities.MAX_ACTIVE_PRIORITIES
}

enum class TodayFeedback {
    Added,
    EmptyTitle,
    LimitReached,
    StorageError,
}

class TodayViewModel(
    private val priorityStore: PriorityStore,
) : ViewModel() {
    private var dailyPriorities = DailyPriorities.empty()
    private val mutableUiState = MutableStateFlow(TodayUiState())

    val uiState: StateFlow<TodayUiState> = mutableUiState.asStateFlow()

    init {
        viewModelScope.launch {
            priorityStore.observeToday()
                .catch {
                    mutableUiState.value = mutableUiState.value.copy(
                        isLoading = false,
                        feedback = TodayFeedback.StorageError,
                    )
                }
                .collect { priorities ->
                    dailyPriorities = DailyPriorities.from(priorities)
                    mutableUiState.value = mutableUiState.value.copy(
                        priorities = priorities,
                        isLoading = false,
                    )
                }
        }
    }

    fun onPriorityInputChanged(value: String) {
        mutableUiState.value = mutableUiState.value.copy(
            priorityInput = value,
            feedback = null,
        )
    }

    fun addPriority() {
        if (uiState.value.isSavingPriority) return
        when (val result = dailyPriorities.add(
            id = UUID.randomUUID().toString(),
            title = uiState.value.priorityInput,
        )) {
            is AddPriorityResult.Added -> {
                val addedPriority = result.priorities.items.last()
                dailyPriorities = result.priorities
                mutableUiState.value = mutableUiState.value.copy(
                    feedback = null,
                    isSavingPriority = true,
                )
                persistAdd(addedPriority)
            }

            AddPriorityResult.EmptyTitle -> {
                mutableUiState.value = mutableUiState.value.copy(feedback = TodayFeedback.EmptyTitle)
            }

            AddPriorityResult.LimitReached -> {
                mutableUiState.value = mutableUiState.value.copy(feedback = TodayFeedback.LimitReached)
            }
        }
    }

    fun completePriority(id: String) {
        when (val result = dailyPriorities.complete(id)) {
            is CompletionResult.Updated -> {
                dailyPriorities = result.priorities
                mutableUiState.value = mutableUiState.value.copy(feedback = null)
                viewModelScope.launch {
                    val persisted = runCatching { priorityStore.complete(id) }.getOrDefault(false)
                    if (!persisted) showStorageError()
                }
            }

            CompletionResult.NotFound -> {
                mutableUiState.value = mutableUiState.value.copy(feedback = TodayFeedback.StorageError)
            }
        }
    }

    private fun persistAdd(priority: PriorityItem) {
        viewModelScope.launch {
            val persisted = runCatching {
                priorityStore.add(priority)
                true
            }.getOrDefault(false)
            if (persisted) {
                mutableUiState.value = mutableUiState.value.copy(
                    priorityInput = "",
                    feedback = TodayFeedback.Added,
                    isSavingPriority = false,
                )
            } else {
                dailyPriorities = DailyPriorities.from(uiState.value.priorities)
                showStorageError()
            }
        }
    }

    private fun showStorageError() {
        mutableUiState.value = mutableUiState.value.copy(
            feedback = TodayFeedback.StorageError,
            isSavingPriority = false,
        )
    }
}
