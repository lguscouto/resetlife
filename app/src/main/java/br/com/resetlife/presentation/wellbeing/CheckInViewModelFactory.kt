package br.com.resetlife.presentation.wellbeing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.resetlife.data.wellbeing.WellbeingRepository

class CheckInViewModelFactory(
    private val repository: WellbeingRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CheckInViewModel(repository) as T
    }
}