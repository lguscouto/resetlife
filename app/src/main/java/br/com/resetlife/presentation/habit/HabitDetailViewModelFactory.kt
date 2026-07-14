package br.com.resetlife.presentation.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.resetlife.data.habit.HabitRepository
import br.com.resetlife.presentation.theme.PressurePreferences

class HabitDetailViewModelFactory(
    private val habitId: String,
    private val repository: HabitRepository,
    private val pressurePreferences: PressurePreferences? = null,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitDetailViewModel::class.java)) {
            return HabitDetailViewModel(habitId, repository, pressurePreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}
