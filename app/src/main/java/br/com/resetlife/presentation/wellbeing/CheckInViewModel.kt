package br.com.resetlife.presentation.wellbeing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.resetlife.data.wellbeing.WellbeingRepository
import br.com.resetlife.domain.wellbeing.WellbeingCheckIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class CheckInUiState(
    val mood: Int? = null,
    val energy: Int? = null,
    val stress: Int? = null,
    val sleepPerceived: Int? = null,
    val note: String = "",
    val saved: Boolean = false,
)

class CheckInViewModel(
    private val repository: WellbeingRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CheckInUiState())
    val uiState: StateFlow<CheckInUiState> = _uiState.asStateFlow()

    fun setMood(value: Int) {
        _uiState.value = _uiState.value.copy(mood = value)
    }

    fun setEnergy(value: Int) {
        _uiState.value = _uiState.value.copy(energy = value)
    }

    fun setStress(value: Int) {
        _uiState.value = _uiState.value.copy(stress = value)
    }

    fun setSleep(value: Int) {
        _uiState.value = _uiState.value.copy(sleepPerceived = value)
    }

    fun setNote(value: String) {
        _uiState.value = _uiState.value.copy(note = value)
    }

    fun save() {
        val state = _uiState.value
        val today = LocalDate.now().toString()
        viewModelScope.launch {
            repository.upsert(
                WellbeingCheckIn(
                    date = today,
                    mood = state.mood ?: 3,
                    energy = state.energy ?: 3,
                    stress = state.stress ?: 3,
                    sleepPerceived = state.sleepPerceived ?: 3,
                    note = state.note.takeIf { it.isNotBlank() },
                ),
            )
            _uiState.value = state.copy(saved = true)
        }
    }
}