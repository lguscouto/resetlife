package br.com.resetlife.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.resetlife.domain.onboarding.LifeArea
import br.com.resetlife.domain.onboarding.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OnboardingUiState(
    val step: Int = 1,
    val selectedArea: LifeArea? = null,
    val availableMinutes: Int? = null,
    val planDurationDays: Int? = null,
    val onboardingCompleted: Boolean = false,
)

class OnboardingViewModel(
    private val userProfileStore: br.com.resetlife.data.onboarding.UserProfileRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userProfileStore.observe().collect { profile ->
                profile?.let {
                    _uiState.value = _uiState.value.copy(
                        onboardingCompleted = it.onboardingCompleted,
                        selectedArea = if (it.lifeAreas.isNotEmpty()) it.lifeAreas.first() else null,
                        availableMinutes = it.dailyAvailabilityMinutes,
                        planDurationDays = it.resetPlanDurationDays,
                    )
                }
            }
        }
    }

    fun selectArea(area: LifeArea) {
        _uiState.value = _uiState.value.copy(selectedArea = area)
    }

    fun setAvailableMinutes(minutes: Int) {
        _uiState.value = _uiState.value.copy(availableMinutes = minutes, step = 2)
    }

    fun setPlanDuration(days: Int) {
        _uiState.value = _uiState.value.copy(planDurationDays = days, step = 3)
    }

    fun completeOnboarding() {
        val state = _uiState.value
        if (state.selectedArea == null ||
            state.availableMinutes == null ||
            state.planDurationDays == null
        ) return

        viewModelScope.launch {
            userProfileStore.completeOnboarding(
                areas = listOf(state.selectedArea),
                durationDays = state.planDurationDays,
                dailyMinutes = state.availableMinutes,
            )
        }
    }

    fun nextStep() {
        val state = _uiState.value
        if (state.step == 1 && state.selectedArea != null) {
            _uiState.value = state.copy(step = 2)
        } else if (state.step == 2 && state.availableMinutes != null) {
            _uiState.value = state.copy(step = 3)
        } else if (state.step == 3 && state.planDurationDays != null) {
            completeOnboarding()
        }
    }
}