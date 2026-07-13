package br.com.resetlife.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.resetlife.data.onboarding.UserProfileRepository
import br.com.resetlife.data.today.PriorityStore

class OnboardingViewModelFactory(
    private val userProfileStore: UserProfileRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnboardingViewModel::class.java)) {
            return OnboardingViewModel(userProfileStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}