package br.com.resetlife.presentation.environment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.resetlife.data.environment.EnvironmentRepository
import br.com.resetlife.domain.environment.EnvironmentSpace
import br.com.resetlife.domain.environment.EnvironmentTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class EnvironmentUiState(
    val spaces: List<EnvironmentSpace> = emptyList(),
    val selectedSpaceId: String? = null,
    val tasks: List<EnvironmentTask> = emptyList(),
    val discardList: List<EnvironmentTask> = emptyList(),
    val showAddSpaceDialog: Boolean = false,
    val showAddTaskDialog: Boolean = false,
    val newSpaceName: String = "",
    val newTaskTitle: String = "",
    val newTaskMinutes: Int = 15,
    val addToDiscardList: Boolean = false,
)

class EnvironmentViewModel(private val repository: EnvironmentRepository) : ViewModel() {

    private val _spaces = repository.observeSpaces()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedSpaceId = MutableStateFlow<String?>(null)

    private val _tasks = MutableStateFlow<List<EnvironmentTask>>(emptyList())
    private val _discardList = repository.observeDiscardList()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow(EnvironmentUiState())
    val uiState: StateFlow<EnvironmentUiState> = combine(
        _spaces,
        _selectedSpaceId,
        _tasks,
        _discardList,
        _uiState,
    ) { spaces, selectedSpaceId, tasks, discardList, ui ->
        ui.copy(
            spaces = spaces,
            selectedSpaceId = selectedSpaceId ?: spaces.firstOrNull()?.id,
            tasks = tasks,
            discardList = discardList,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EnvironmentUiState())

    init {
        viewModelScope.launch {
            _spaces.collect { spaces ->
                if (_selectedSpaceId.value == null && spaces.isNotEmpty()) {
                    _selectedSpaceId.value = spaces.first().id
                }
            }
        }
        observeSelectedSpace()
    }

    private fun observeSelectedSpace() {
        viewModelScope.launch {
            _selectedSpaceId.collect { spaceId ->
                if (spaceId != null) {
                    repository.observeTasksBySpace(spaceId).collect { tasks ->
                        _tasks.value = tasks
                    }
                }
            }
        }
    }

    fun selectSpace(spaceId: String) {
        _selectedSpaceId.value = spaceId
    }

    fun onNewSpaceNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(newSpaceName = name)
    }

    fun onNewTaskTitleChanged(title: String) {
        _uiState.value = _uiState.value.copy(newTaskTitle = title)
    }

    fun onNewTaskMinutesChanged(minutes: Int) {
        _uiState.value = _uiState.value.copy(newTaskMinutes = minutes)
    }

    fun onAddToDiscardListChanged(value: Boolean) {
        _uiState.value = _uiState.value.copy(addToDiscardList = value)
    }

    fun showAddSpaceDialog() {
        _uiState.value = _uiState.value.copy(showAddSpaceDialog = true)
    }

    fun hideAddSpaceDialog() {
        _uiState.value = _uiState.value.copy(showAddSpaceDialog = false, newSpaceName = "")
    }

    fun showAddTaskDialog() {
        _uiState.value = _uiState.value.copy(showAddTaskDialog = true)
    }

    fun hideAddTaskDialog() {
        _uiState.value = _uiState.value.copy(showAddTaskDialog = false, newTaskTitle = "")
    }

    fun addSpace() {
        val name = _uiState.value.newSpaceName.trim()
        if (name.isEmpty()) {
            hideAddSpaceDialog()
            return
        }
        viewModelScope.launch {
            val id = repository.addSpace(name)
            if (_selectedSpaceId.value == null) {
                _selectedSpaceId.value = id
            }
        }
        hideAddSpaceDialog()
    }

    fun addTask() {
        val state = _uiState.value
        val spaceId = state.selectedSpaceId ?: state.spaces.firstOrNull()?.id
        val title = state.newTaskTitle.trim()
        if (spaceId == null || title.isEmpty()) {
            hideAddTaskDialog()
            return
        }
        viewModelScope.launch {
            repository.addTask(
                spaceId = spaceId,
                title = title,
                estimatedMinutes = state.newTaskMinutes,
                discardList = state.addToDiscardList,
            )
        }
        hideAddTaskDialog()
    }

    fun toggleTask(task: EnvironmentTask) {
        viewModelScope.launch {
            repository.setTaskDone(task, !task.done)
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
        }
    }
}
