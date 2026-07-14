package br.com.resetlife.presentation.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.resetlife.data.habit.HabitRepository
import br.com.resetlife.domain.habit.Habit
import br.com.resetlife.domain.habit.HabitGoalType
import br.com.resetlife.domain.habit.HabitLog
import br.com.resetlife.presentation.theme.PressurePreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class HabitDetailUiState(
    val habit: Habit? = null,
    val logs: List<HabitLog> = emptyList(),
    val streakCurrent: Int = 0,
    val completionRate: Float = 0f,
    val relaxedMode: Boolean = false,
    val isLoading: Boolean = true,
)

class HabitDetailViewModel(
    private val habitId: String,
    private val repository: HabitRepository,
    private val pressurePreferences: PressurePreferences? = null,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HabitDetailUiState())
    val uiState: StateFlow<HabitDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeLogs(habitId).collect { logs ->
                val current = _uiState.value
                val habit = current.habit ?: repository.getHabit(habitId)
                _uiState.value = current.copy(
                    habit = habit,
                    logs = logs,
                    streakCurrent = computeStreak(logs, habit),
                    completionRate = computeCompletionRate(habit, logs),
                    isLoading = false,
                )
            }
        }
        pressurePreferences?.let { prefs ->
            viewModelScope.launch {
                prefs.relaxedMode.collect { relaxed ->
                    _uiState.value = _uiState.value.copy(relaxedMode = relaxed)
                }
            }
        }
    }

    fun setRelaxedMode(enabled: Boolean) {
        val prefs = pressurePreferences ?: return
        viewModelScope.launch {
            prefs.setRelaxedMode(enabled)
        }
    }

    private fun computeStreak(logs: List<HabitLog>, habit: Habit?): Int {
        val doneDates = logs.filter { it.isDoneFor(habit) }.map { it.date }.toSet()
        if (doneDates.isEmpty()) return 0
        val today = LocalDate.now()
        var streak = 0
        // Se hoje não foi feito, a sequência conta até ontem (sem zerar a contagem).
        var cursor = if (doneDates.contains(today.toString())) today else today.minusDays(1)
        while (doneDates.contains(cursor.toString())) {
            streak += 1
            cursor = cursor.minusDays(1)
        }
        return streak
    }

    private fun computeCompletionRate(habit: Habit?, logs: List<HabitLog>): Float {
        val createdAt = habit?.createdAt ?: return 0f
        val created = runCatching { LocalDate.parse(createdAt) }.getOrNull() ?: return 0f
        val today = LocalDate.now()
        // Total de dias desde a criação (inclusive) até hoje.
        val totalDays = java.time.temporal.ChronoUnit.DAYS.between(created, today).toInt() + 1
        if (totalDays <= 0) return 0f
        val doneCount = logs.count { it.isDoneFor(habit) }
        return doneCount.toFloat() / totalDays
    }

    private fun HabitLog.isDoneFor(habit: Habit?): Boolean {
        return if (habit?.goalType == HabitGoalType.QUANTITY) {
            // Para QUANTITY, concluído quando há registro com valor > 0 (done cobre value>=target,
            // mas qualquer valor positivo registrado conta como dia cumprido na prática do app).
            this.done || this.value > 0
        } else {
            this.done
        }
    }
}
