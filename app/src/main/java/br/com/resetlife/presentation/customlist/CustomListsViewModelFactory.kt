package br.com.resetlife.presentation.customlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.resetlife.data.environment.EnvironmentRepository

class CustomListsViewModelFactory(
    private val repository: EnvironmentRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CustomListsViewModel(repository) as T
    }
}
