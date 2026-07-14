package br.com.resetlife.presentation.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.resetlife.data.environment.EnvironmentRepository
import br.com.resetlife.data.today.PriorityStore
import br.com.resetlife.domain.environment.EnvironmentTask
import br.com.resetlife.domain.today.AddPriorityResult
import br.com.resetlife.domain.today.CompletionResult
import br.com.resetlife.domain.today.DailyPriorities
import br.com.resetlife.domain.today.PriorityItem
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class TodayUiState(
    val priorityInput: String = "",
    val priorities: List<PriorityItem> = emptyList(),
    val feedback: TodayFeedback? = null,
    val isLoading: Boolean = true,
    val loadError: Boolean = false,
    val isSavingPriority: Boolean = false,
    val pendingPriorityId: String? = null,
    val environmentSuggestion: EnvironmentTask? = null,
) {
    val isPriorityActionInProgress: Boolean
        get() = pendingPriorityId != null

    val canRetry: Boolean
        get() = loadError || feedback == TodayFeedback.StorageError

    val activePriorityCount: Int
        get() = activePriorities.size

    val activePriorities: List<PriorityItem>
        get() = priorities.filterNot(PriorityItem::isCompleted)

    val completedPriorities: List<PriorityItem>
        get() = priorities.filter(PriorityItem::isCompleted)

    val isAddPriorityEnabled: Boolean
        get() = !isLoading && !isSavingPriority && !isPriorityActionInProgress && priorityInput.isNotBlank() &&
            activePriorityCount < DailyPriorities.MAX_ACTIVE_PRIORITIES
}

enum class TodayFeedback {
    Added,
    Completed,
    EmptyTitle,
    LimitReached,
    StorageError,
}

private sealed interface TodayRetryAction {
    data object Load : TodayRetryAction
    data object AddPriority : TodayRetryAction
    data class CompletePriority(val id: String) : TodayRetryAction
}

class TodayViewModel(
    private val priorityStore: PriorityStore,
    private val environmentRepository: EnvironmentRepository,
) : ViewModel() {
    private var dailyPriorities = DailyPriorities.empty()
    private var retryAction: TodayRetryAction? = null
    private val mutableUiState = MutableStateFlow(TodayUiState())

    val uiState: StateFlow<TodayUiState> = mutableUiState.asStateFlow()

    init {
        observePriorities()
        observeEnvironmentSuggestion()
    }

    fun retryStorageOperation() {
        if (!uiState.value.canRetry) return
        when (val action = retryAction) {
            null, TodayRetryAction.Load -> {
                retryAction = null
                mutableUiState.value = mutableUiState.value.copy(
                    isLoading = true,
                    loadError = false,
                    feedback = null,
                )
                observePriorities()
            }
            TodayRetryAction.AddPriority -> {
                retryAction = null
                addPriority()
            }
            is TodayRetryAction.CompletePriority -> {
                retryAction = null
                completePriority(action.id)
            }
        }
    }

    private fun observePriorities() {
        viewModelScope.launch {
            priorityStore.observeToday()
                .catch {
                    retryAction = TodayRetryAction.Load
                    mutableUiState.value = mutableUiState.value.copy(
                        isLoading = false,
                        loadError = true,
                        feedback = TodayFeedback.StorageError,
                    )
                }
                .collect { priorities ->
                    dailyPriorities = DailyPriorities.from(priorities)
                    mutableUiState.value = mutableUiState.value.copy(
                        priorities = priorities,
                        isLoading = false,
                        loadError = false,
                    )
                }
        }
    }

    private fun observeEnvironmentSuggestion() {
        viewModelScope.launch {
            environmentRepository.observeSpaces()
                .collect { spaces ->
                    if (spaces.isEmpty()) {
                        mutableUiState.value = mutableUiState.value.copy(environmentSuggestion = null)
                        return@collect
                    }
                    val tasksBySpace = mutableMapOf<String, List<EnvironmentTask>>()
                    spaces.forEach { space ->
                        launch {
                            environmentRepository.observeTasksBySpace(space.id).collect { tasks ->
                                tasksBySpace[space.id] = tasks
                                val all = tasksBySpace.values.flatten()
                                val pending = all.firstOrNull { !it.done && !it.discardList }
                                mutableUiState.value = mutableUiState.value.copy(environmentSuggestion = pending)
                            }
                        }
                    }
                }
        }
    }

    fun completeEnvironmentSuggestion() {
        val task = uiState.value.environmentSuggestion ?: return
        viewModelScope.launch {
            environmentRepository.setTaskDone(task, true)
        }
    }

    fun onPriorityInputChanged(value: String) {
        mutableUiState.value = mutableUiState.value.copy(
            priorityInput = value,
            feedback = null,
        )
        retryAction = null
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
                retryAction = null
                mutableUiState.value = mutableUiState.value.copy(feedback = TodayFeedback.EmptyTitle)
            }

            AddPriorityResult.LimitReached -> {
                retryAction = null
                mutableUiState.value = mutableUiState.value.copy(feedback = TodayFeedback.LimitReached)
            }
        }
    }

    fun completePriority(id: String) {
        if (uiState.value.isPriorityActionInProgress) return
        when (val result = dailyPriorities.complete(id)) {
            is CompletionResult.Updated -> {
                dailyPriorities = result.priorities
                mutableUiState.value = mutableUiState.value.copy(
                    feedback = null,
                    pendingPriorityId = id,
                )
                viewModelScope.launch {
                    val persisted = runCatching { priorityStore.complete(id) }.getOrDefault(false)
                    if (!persisted) {
                        dailyPriorities = DailyPriorities.from(uiState.value.priorities)
                        retryAction = TodayRetryAction.CompletePriority(id)
                        showStorageError()
                    } else {
                        retryAction = null
                        mutableUiState.value = mutableUiState.value.copy(
                            feedback = TodayFeedback.Completed,
                            pendingPriorityId = null,
                        )
                    }
                }
            }

            CompletionResult.NotFound -> {
                retryAction = TodayRetryAction.Load
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
                retryAction = null
                mutableUiState.value = mutableUiState.value.copy(
                    priorityInput = "",
                    feedback = TodayFeedback.Added,
                    isSavingPriority = false,
                )
            } else {
                dailyPriorities = DailyPriorities.from(uiState.value.priorities)
                retryAction = TodayRetryAction.AddPriority
                showStorageError()
            }
        }
    }

    private fun showStorageError() {
        mutableUiState.value = mutableUiState.value.copy(
            feedback = TodayFeedback.StorageError,
            loadError = false,
            isSavingPriority = false,
            pendingPriorityId = null,
        )
    }
}
