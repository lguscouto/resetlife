package br.com.resetlife.presentation.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.resetlife.data.today.PriorityStore

class TodayViewModelFactory(
    private val priorityStore: PriorityStore,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodayViewModel::class.java)) {
            return TodayViewModel(priorityStore) as T
        }
        throw IllegalArgumentException("Classe de ViewModel não suportada: ${modelClass.name}")
    }
}
