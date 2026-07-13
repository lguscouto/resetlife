package br.com.resetlife.presentation.onboarding

import androidx.lifecycle.ViewModel
import br.com.resetlife.domain.onboarding.LifeArea
import br.com.resetlife.domain.onboarding.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class OnboardingUiState(
    val step: Int = 1,
    val selectedArea: LifeArea? = null,
    val availableMinutes: Int? = null,
    val planDurationDays: Int? = null,
)

class OnboardingViewModel(
    private val userProfileStore: br.com.resetlife.data.onboarding.UserProfileRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun selectArea(area: LifeArea) {
        _uiState.value = _uiState.value.copy(selectedArea = area)
    }

    fun nextStep() {
        // placeholder: navigate to next step or complete onboarding
    }

    fun setAvailableMinutes(minutes: Int) {
        _uiState.value = _uiState.value.copy(availableMinutes = minutes)
    }

    fun setPlanDuration(days: Int) {
        _uiState.value = _uiState.value.copy(planDurationDays = days)
    }

    fun completeOnboarding() {
        if (_uiState.value.selectedArea == null ||
            _uiState.value.availableMinutes == null ||
            _uiState.value.planDurationDays == null
        ) return
        // persist via repository would happen here
    }
}