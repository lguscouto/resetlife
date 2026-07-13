package br.com.resetlife.presentation.organize

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.resetlife.data.organize.OrganizeStore
import br.com.resetlife.data.today.PriorityStore
import br.com.resetlife.domain.organize.Project
import br.com.resetlife.domain.organize.ProjectCreationResult
import br.com.resetlife.domain.organize.Task
import br.com.resetlife.domain.organize.TaskCreationResult
import br.com.resetlife.domain.organize.TaskStatus
import br.com.resetlife.domain.today.AddPriorityResult
import br.com.resetlife.domain.today.DailyPriorities
import br.com.resetlife.domain.today.PriorityItem
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

data class OrganizeUiState(
    val projects: List<Project> = emptyList(),
    val tasks: List<Task> = emptyList(),
    val searchQuery: String = "",
    val projectTitleInput: String = "",
    val projectGoalInput: String = "",
    val taskTitleInput: String = "",
    val taskNoteInput: String = "",
    val taskDueDateInput: String = "",
    val taskEstimatedMinutesInput: String = "",
    val selectedProjectId: String? = null,
    val projectTitleError: OrganizeFieldError? = null,
    val taskTitleError: OrganizeFieldError? = null,
    val taskDueDateError: OrganizeFieldError? = null,
    val taskDurationError: OrganizeFieldError? = null,
    val feedback: OrganizeFeedback? = null,
    val isLoading: Boolean = true,
    val loadError: Boolean = false,
    val isProjectSaving: Boolean = false,
    val isTaskSaving: Boolean = false,
    val pendingTaskId: String? = null,
) {
    val canRetry: Boolean
        get() = loadError || feedback == OrganizeFeedback.StorageError

    val isDataMutationInProgress: Boolean
        get() = isProjectSaving || isTaskSaving || pendingTaskId != null

    val filteredTasks: List<Task>
        get() = tasks.filter { it.matchesQuery(searchQuery) }

    val filteredOpenTasks: List<Task>
        get() = filteredTasks.filter { it.status != TaskStatus.COMPLETED }

    val filteredCompletedTasks: List<Task>
        get() = filteredTasks.filter { it.status == TaskStatus.COMPLETED }
}

enum class OrganizeFieldError {
    Required,
    InvalidDate,
    InvalidDuration,
}

enum class OrganizeFeedback {
    EmptyProjectTitle,
    EmptyTaskTitle,
    InvalidDuration,
    ProjectCreated,
    TaskCreated,
    TaskCompleted,
    TaskReopened,
    PriorityLimitReached,
    Promoted,
    StorageError,
}

private sealed interface OrganizeRetryAction {
    data object Load : OrganizeRetryAction
    data object AddProject : OrganizeRetryAction
    data object AddTask : OrganizeRetryAction
    data class ToggleTask(val task: Task) : OrganizeRetryAction
    data class Promote(val priority: PriorityItem) : OrganizeRetryAction
}

class OrganizeViewModel(
    private val organizeStore: OrganizeStore,
    private val priorityStore: PriorityStore,
) : ViewModel() {
    private var dailyPriorities = DailyPriorities.empty()
    private var retryAction: OrganizeRetryAction? = null
    private val mutableUiState = MutableStateFlow(OrganizeUiState())

    val uiState: StateFlow<OrganizeUiState> = mutableUiState.asStateFlow()

    init {
        observeOrganizeData()
        viewModelScope.launch {
            priorityStore.observeToday()
                .catch { /* The priority screen owns its own storage feedback. */ }
                .collect { priorities -> dailyPriorities = DailyPriorities.from(priorities) }
        }
    }

    fun retryStorageOperation() {
        if (!uiState.value.canRetry) return
        when (val action = retryAction) {
            null, OrganizeRetryAction.Load -> {
                retryAction = null
                mutableUiState.value = mutableUiState.value.copy(
                    isLoading = true,
                    loadError = false,
                    feedback = null,
                )
                observeOrganizeData()
            }
            OrganizeRetryAction.AddProject -> {
                retryAction = null
                addProject()
            }
            OrganizeRetryAction.AddTask -> {
                retryAction = null
                addTask()
            }
            is OrganizeRetryAction.ToggleTask -> {
                retryAction = null
                toggleTask(action.task)
            }
            is OrganizeRetryAction.Promote -> {
                retryAction = null
                persistPromotedPriority(action.priority)
            }
        }
    }

    private fun observeOrganizeData() {
        viewModelScope.launch {
            combine(
                organizeStore.observeProjects(),
                organizeStore.observeTasks(),
            ) { projects, tasks -> projects to tasks }
                .catch { showLoadError() }
                .collect { (projects, tasks) ->
                    mutableUiState.value = mutableUiState.value.copy(
                        projects = projects,
                        tasks = tasks,
                        isLoading = false,
                        loadError = false,
                    )
                }
        }
    }

    fun onSearchChanged(value: String) = update { copy(searchQuery = value, feedback = null) }
    fun onProjectTitleChanged(value: String) = update {
        copy(projectTitleInput = value, projectTitleError = null, feedback = null)
    }
    fun onProjectGoalChanged(value: String) = update { copy(projectGoalInput = value) }
    fun onProjectSelected(id: String?) = update { copy(selectedProjectId = id) }
    fun onTaskTitleChanged(value: String) = update {
        copy(taskTitleInput = value, taskTitleError = null, feedback = null)
    }
    fun onTaskNoteChanged(value: String) = update { copy(taskNoteInput = value) }
    fun onTaskDueDateChanged(value: String) = update {
        copy(taskDueDateInput = value, taskDueDateError = null, feedback = null)
    }
    fun onTaskEstimatedMinutesChanged(value: String) = update {
        copy(taskEstimatedMinutesInput = value, taskDurationError = null, feedback = null)
    }

    fun addProject() {
        if (uiState.value.isDataMutationInProgress) return
        when (val result = Project.create(UUID.randomUUID().toString(), uiState.value.projectTitleInput, uiState.value.projectGoalInput)) {
            is ProjectCreationResult.Created -> {
                update { copy(isProjectSaving = true, projectTitleError = null, feedback = null) }
                viewModelScope.launch {
                    val saved = runCatching { organizeStore.addProject(result.project) }.isSuccess
                    if (saved) {
                        retryAction = null
                        update {
                            copy(
                                projectTitleInput = "",
                                projectGoalInput = "",
                                projectTitleError = null,
                                feedback = OrganizeFeedback.ProjectCreated,
                                isProjectSaving = false,
                            )
                        }
                    } else {
                        retryAction = OrganizeRetryAction.AddProject
                        showStorageError()
                    }
                }
            }
            ProjectCreationResult.EmptyTitle -> update {
                copy(projectTitleError = OrganizeFieldError.Required, feedback = null)
            }
        }
    }

    fun addTask() {
        if (uiState.value.isDataMutationInProgress) return
        val currentState = uiState.value
        val titleError = OrganizeFieldError.Required.takeIf { currentState.taskTitleInput.isBlank() }
        val rawDueDate = currentState.taskDueDateInput.trim()
        val normalizedDueDate = normalizeDueDate(rawDueDate)
        val dueDateError = OrganizeFieldError.InvalidDate.takeIf {
            rawDueDate.isNotEmpty() && normalizedDueDate == null
        }
        val rawDuration = uiState.value.taskEstimatedMinutesInput.trim()
        val duration = rawDuration.takeIf { it.isNotEmpty() }?.toIntOrNull()
        val durationError = OrganizeFieldError.InvalidDuration.takeIf {
            rawDuration.isNotEmpty() && (duration == null || duration <= 0)
        }
        if (titleError != null || dueDateError != null || durationError != null) {
            update {
                copy(
                    taskTitleError = titleError,
                    taskDueDateError = dueDateError,
                    taskDurationError = durationError,
                    feedback = null,
                )
            }
            return
        }

        when (
            val result = Task.create(
                id = UUID.randomUUID().toString(),
                title = uiState.value.taskTitleInput,
                note = uiState.value.taskNoteInput,
                dueDate = normalizedDueDate,
                estimatedMinutes = duration,
                projectId = uiState.value.selectedProjectId,
            )
        ) {
            is TaskCreationResult.Created -> {
                update { copy(isTaskSaving = true, feedback = null) }
                viewModelScope.launch(Dispatchers.IO) {
                    val saved = runCatching { organizeStore.addTask(result.task) }.isSuccess
                    if (saved) {
                        retryAction = null
                        update {
                            copy(
                                taskTitleInput = "",
                                taskNoteInput = "",
                                taskDueDateInput = "",
                                taskEstimatedMinutesInput = "",
                                taskTitleError = null,
                                taskDueDateError = null,
                                taskDurationError = null,
                                feedback = OrganizeFeedback.TaskCreated,
                                isTaskSaving = false,
                            )
                        }
                    } else {
                        retryAction = OrganizeRetryAction.AddTask
                        showStorageError()
                    }
                }
            }
            TaskCreationResult.EmptyTitle -> update {
                copy(taskTitleError = OrganizeFieldError.Required, feedback = null)
            }
        }
    }

    fun toggleTask(task: Task) {
        if (uiState.value.isDataMutationInProgress) return
        val updated = if (task.status == TaskStatus.COMPLETED) task.reopen() else task.complete()
        update { copy(pendingTaskId = task.id, feedback = null) }
        viewModelScope.launch {
            val saved = runCatching { organizeStore.updateTask(updated) }.getOrDefault(false)
            if (saved) {
                retryAction = null
                update {
                    copy(
                        pendingTaskId = null,
                        feedback = if (updated.status == TaskStatus.COMPLETED) {
                            OrganizeFeedback.TaskCompleted
                        } else {
                            OrganizeFeedback.TaskReopened
                        },
                    )
                }
            } else {
                retryAction = OrganizeRetryAction.ToggleTask(task)
                showStorageError()
            }
        }
    }

    fun promoteToToday(task: Task) {
        if (uiState.value.isDataMutationInProgress) return
        when (val result = dailyPriorities.add(id = task.id, title = task.title)) {
            is AddPriorityResult.Added -> {
                dailyPriorities = result.priorities
                update { copy(pendingTaskId = task.id, feedback = null) }
                persistPromotedPriority(result.priorities.items.last())
            }
            AddPriorityResult.LimitReached -> {
                retryAction = null
                update { copy(feedback = OrganizeFeedback.PriorityLimitReached) }
            }
            AddPriorityResult.EmptyTitle -> {
                retryAction = null
                update { copy(feedback = OrganizeFeedback.EmptyTaskTitle) }
            }
        }
    }

    private fun persistPromotedPriority(priority: PriorityItem) {
        viewModelScope.launch {
            val saved = runCatching { priorityStore.add(priority) }.isSuccess
            if (saved) {
                retryAction = null
                update { copy(pendingTaskId = null, feedback = OrganizeFeedback.Promoted) }
            } else {
                retryAction = OrganizeRetryAction.Promote(priority)
                showStorageError()
            }
        }
    }

    private fun update(transform: OrganizeUiState.() -> OrganizeUiState) {
        mutableUiState.value = transform(mutableUiState.value)
    }

    private fun showLoadError() {
        retryAction = OrganizeRetryAction.Load
        update {
            copy(
                feedback = OrganizeFeedback.StorageError,
                isLoading = false,
                loadError = true,
                isProjectSaving = false,
                isTaskSaving = false,
                pendingTaskId = null,
            )
        }
    }

    private fun showStorageError() {
        update {
            copy(
                feedback = OrganizeFeedback.StorageError,
                isLoading = false,
                loadError = false,
                isProjectSaving = false,
                isTaskSaving = false,
                pendingTaskId = null,
            )
        }
    }

    private fun normalizeDueDate(rawValue: String): String? = TaskDateFormat.normalizeInput(rawValue)
}
