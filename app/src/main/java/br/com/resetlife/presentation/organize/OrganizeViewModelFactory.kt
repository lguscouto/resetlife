package br.com.resetlife.presentation.organize

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.resetlife.data.organize.OrganizeStore
import br.com.resetlife.data.today.PriorityStore

class OrganizeViewModelFactory(
    private val organizeStore: OrganizeStore,
    private val priorityStore: PriorityStore,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrganizeViewModel::class.java)) {
            return OrganizeViewModel(organizeStore, priorityStore) as T
        }
        throw IllegalArgumentException("Classe de ViewModel não suportada: ${modelClass.name}")
    }
}
