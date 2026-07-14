package br.com.resetlife.presentation.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.com.resetlife.ResetLifeApplication
import br.com.resetlife.data.export.DataExportService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class ExportStatus { Idle, Exporting, Done, Error }

data class DataExportUiState(
    val status: ExportStatus = ExportStatus.Idle,
    val jsonPreview: String? = null,
)

class DataExportViewModel(
    private val service: DataExportService,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DataExportUiState())
    val uiState: StateFlow<DataExportUiState> = _uiState.asStateFlow()

    /** Gera o JSON e salva no [uri] escolhido pelo usuário via CreateDocument. */
    fun exportTo(uri: Uri, context: Context) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = ExportStatus.Exporting)
            try {
                val json = service.exportAll()
                val text = json.toString(2)
                context.contentResolver.openOutputStream(uri)?.use { stream ->
                    stream.bufferedWriter().use { writer -> writer.write(text) }
                } ?: throw IllegalStateException("Não foi possível abrir o arquivo.")
                _uiState.value = _uiState.value.copy(
                    status = ExportStatus.Done,
                    jsonPreview = text,
                )
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(
                    status = ExportStatus.Error,
                    jsonPreview = null,
                )
            }
        }
    }

    fun resetStatus() {
        _uiState.value = _uiState.value.copy(status = ExportStatus.Idle)
    }
}

class DataExportViewModelFactory(
    private val application: ResetLifeApplication,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DataExportViewModel::class.java)) {
            return DataExportViewModel(DataExportService(application)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}
