package br.com.resetlife.presentation.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.resetlife.data.habit.HabitRepository
import br.com.resetlife.domain.habit.Habit
import br.com.resetlife.domain.habit.HabitFrequency
import br.com.resetlife.domain.habit.HabitGoalType
import br.com.resetlife.presentation.reward.RewardProvider
import br.com.resetlife.presentation.reward.RewardType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate

data class HabitUiItem(
    val habit: Habit,
    val doneToday: Boolean,
    val loggedValueToday: Int?,
)

data class HabitUiState(
    val habits: List<HabitUiItem> = emptyList(),
    val showAddDialog: Boolean = false,
    val errorMessage: String? = null,
    val saved: Boolean = false,
    val rewardMessage: String? = null,
)

class HabitViewModel(
    private val repository: HabitRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HabitUiState())
    val uiState: StateFlow<HabitUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val today = LocalDate.now().toString()
            combine(
                repository.observeHabits(),
                repository.observeAllLogsForDate(today),
            ) { habits, logs ->
                val doneMap = logs.filter { it.done }.map { it.habitId }.toSet()
                val valueMap = logs.associate { it.habitId to it.value }
                habits
                    .filter { it.active }
                    .map { habit ->
                        HabitUiItem(
                            habit = habit,
                            doneToday = doneMap.contains(habit.id),
                            loggedValueToday = valueMap[habit.id],
                        )
                    }
            }.collect { items ->
                _uiState.value = _uiState.value.copy(habits = items)
            }
        }
    }

    fun toggleToday(habit: Habit) {
        viewModelScope.launch {
            val wasDone = _uiState.value.habits.firstOrNull { it.habit.id == habit.id }?.doneToday ?: false
            repository.toggleToday(habit)
            if (!wasDone) {
                _uiState.value = _uiState.value.copy(rewardMessage = RewardProvider.randomReward(RewardType.HABIT_DONE))
            } else {
                _uiState.value = _uiState.value.copy(rewardMessage = null)
            }
        }
    }

    fun clearReward() {
        _uiState.value = _uiState.value.copy(rewardMessage = null)
    }

    fun setQuantityToday(habit: Habit, value: Int) {
        viewModelScope.launch {
            repository.setQuantityToday(habit, value.coerceAtLeast(0))
        }
    }

    fun pause(habit: Habit) {
        viewModelScope.launch { repository.pause(habit.id) }
    }

    fun resume(habit: Habit) {
        viewModelScope.launch { repository.resume(habit.id) }
    }

    fun archive(habit: Habit) {
        viewModelScope.launch { repository.archive(habit.id) }
    }

    fun showAddDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = true)
    }

    fun hideAddDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = false, errorMessage = null)
    }

    fun addHabit(
        name: String,
        frequency: HabitFrequency,
        goalType: HabitGoalType,
        targetValue: Int?,
        unit: String?,
        colorHex: String? = null,
    ) {
        viewModelScope.launch {
            when (val result = repository.add(name, frequency, goalType, targetValue, unit, colorHex)) {
                is br.com.resetlife.domain.habit.HabitCreationResult.Created -> {
                    _uiState.value = _uiState.value.copy(showAddDialog = false, errorMessage = null, saved = true)
                }
                is br.com.resetlife.domain.habit.HabitCreationResult.EmptyName -> {
                    _uiState.value = _uiState.value.copy(errorMessage = "Informe um nome para o hábito.")
                }
                is br.com.resetlife.domain.habit.HabitCreationResult.InvalidTarget -> {
                    _uiState.value = _uiState.value.copy(errorMessage = "A meta precisa ser maior que zero.")
                }
            }
        }
    }
}
