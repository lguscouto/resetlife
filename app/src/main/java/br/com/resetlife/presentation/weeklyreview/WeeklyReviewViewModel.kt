package br.com.resetlife.presentation.weeklyreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.resetlife.data.organize.OrganizeStore
import br.com.resetlife.data.today.PriorityStore
import br.com.resetlife.data.weeklyreview.WeeklyReviewStore
import br.com.resetlife.domain.organize.Task
import br.com.resetlife.domain.organize.TaskStatus
import br.com.resetlife.domain.today.PriorityItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

data class WeeklyReviewUiState(
    val completedCount: Int = 0,
    val pendingCount: Int = 0,
    val taskOpenCount: Int = 0,
    val taskDoneCount: Int = 0,
    val checkInCount: Int = 0,
    val completed: String = "",
    val pending: String = "",
    val difficulty: String = "",
    val nextWeekPriorities: String = "",
    val habitToAdjust: String = "",
    val saved: Boolean = false,
)

class WeeklyReviewViewModel(
    private val priorityStore: PriorityStore,
    private val organizeStore: OrganizeStore,
    private val weeklyReviewStore: WeeklyReviewStore,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeeklyReviewUiState())
    val uiState: StateFlow<WeeklyReviewUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                priorityStore.observeToday(),
                organizeStore.observeTasks(),
                weeklyReviewStore.observeReviews(),
            ) { priorities, tasks, reviews ->
                val done = priorities.count { it.isCompleted }
                val pend = priorities.count { !it.isCompleted }
                val openTasks = tasks.count { it.status != TaskStatus.COMPLETED }
                val doneTasks = tasks.count { it.status == TaskStatus.COMPLETED }
                val checkIns = reviews.size
                _uiState.value = _uiState.value.copy(
                    completedCount = done,
                    pendingCount = pend,
                    taskOpenCount = openTasks,
                    taskDoneCount = doneTasks,
                    checkInCount = checkIns,
                )
            }.collect { }
        }
    }

    fun setCompleted(value: String) {
        _uiState.value = _uiState.value.copy(completed = value)
    }

    fun setPending(value: String) {
        _uiState.value = _uiState.value.copy(pending = value)
    }

    fun setDifficulty(value: String) {
        _uiState.value = _uiState.value.copy(difficulty = value)
    }

    fun setNextWeekPriorities(value: String) {
        _uiState.value = _uiState.value.copy(nextWeekPriorities = value)
    }

    fun setHabitToAdjust(value: String) {
        _uiState.value = _uiState.value.copy(habitToAdjust = value)
    }

    fun save() {
        val state = _uiState.value
        val today = LocalDate.now()
        val start = today.minusDays(6).format(DateTimeFormatter.ISO_DATE)
        val end = today.format(DateTimeFormatter.ISO_DATE)
        viewModelScope.launch {
            weeklyReviewStore.upsert(
                br.com.resetlife.domain.weeklyreview.WeeklyReview(
                    id = UUID.randomUUID().toString(),
                    periodStart = start,
                    periodEnd = end,
                    completed = state.completed,
                    pending = state.pending,
                    difficulty = state.difficulty,
                    nextWeekPriorities = state.nextWeekPriorities,
                    habitToAdjust = state.habitToAdjust,
                    createdAt = System.currentTimeMillis(),
                ),
            )
            _uiState.value = state.copy(saved = true)
        }
    }
}
