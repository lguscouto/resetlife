package br.com.resetlife.presentation.environment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.resetlife.data.environment.EnvironmentRepository

class EnvironmentViewModelFactory(
    private val repository: EnvironmentRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EnvironmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EnvironmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
