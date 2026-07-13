package br.com.resetlife.presentation.organize

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.resetlife.R
import br.com.resetlife.domain.organize.Project
import br.com.resetlife.domain.organize.Task
import br.com.resetlife.domain.organize.TaskStatus
import br.com.resetlife.presentation.components.ResetLifeMessage
import br.com.resetlife.presentation.components.ResetLifeMessageTone
import br.com.resetlife.presentation.components.ResetLifeSectionHeader
import br.com.resetlife.presentation.components.ResetLifeSurface
import br.com.resetlife.presentation.components.ResetLifeLoading
import br.com.resetlife.presentation.theme.ResetLifeSpacing
import br.com.resetlife.presentation.theme.ResetLifeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganizeScreen(
    uiState: OrganizeUiState,
    onSearchChanged: (String) -> Unit,
    onProjectTitleChanged: (String) -> Unit,
    onProjectGoalChanged: (String) -> Unit,
    onProjectSelected: (String?) -> Unit,
    onAddProject: () -> Unit,
    onTaskTitleChanged: (String) -> Unit,
    onTaskNoteChanged: (String) -> Unit,
    onTaskDueDateChanged: (String) -> Unit,
    onTaskEstimatedMinutesChanged: (String) -> Unit,
    onAddTask: () -> Unit,
    onToggleTask: (Task) -> Unit,
    onPromoteToToday: (Task) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var projectFormExpanded by rememberSaveable { mutableStateOf(false) }
    var taskFormExpanded by rememberSaveable { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current
    val projectGoalFocusRequester = remember { FocusRequester() }
    val taskNoteFocusRequester = remember { FocusRequester() }
    val taskDueDateFocusRequester = remember { FocusRequester() }
    val taskDurationFocusRequester = remember { FocusRequester() }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.organize_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .padding(horizontal = ResetLifeSpacing.screenHorizontal, vertical = ResetLifeSpacing.lg)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.md),
        ) {
            ResetLifeSectionHeader(
                title = stringResource(R.string.organize_title),
                supportingText = stringResource(R.string.organize_subtitle),
            )

            ResetLifeSurface(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(ResetLifeSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
                ) {
                    FormToggle(
                        title = stringResource(R.string.project_form_title),
                        supportingText = stringResource(R.string.project_form_hint),
                        expanded = projectFormExpanded,
                        onToggle = { projectFormExpanded = !projectFormExpanded },
                    )
                    if (projectFormExpanded) {
                        OutlinedTextField(
                            value = uiState.projectTitleInput,
                            onValueChange = onProjectTitleChanged,
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(stringResource(R.string.project_title_label)) },
                            supportingText = {
                                FieldErrorText(uiState.projectTitleError)
                            },
                            isError = uiState.projectTitleError != null,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(
                                onNext = { projectGoalFocusRequester.requestFocus() },
                            ),
                        )
                        OutlinedTextField(
                            value = uiState.projectGoalInput,
                            onValueChange = onProjectGoalChanged,
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(projectGoalFocusRequester),
                            label = { Text(stringResource(R.string.project_goal_label)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    onAddProject()
                                    focusManager.clearFocus()
                                },
                            ),
                        )
                        Button(
                            onClick = onAddProject,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isDataMutationInProgress,
                        ) {
                            Text(
                                stringResource(
                                    if (uiState.isProjectSaving) R.string.saving else R.string.add_project,
                                ),
                            )
                        }
                    }
                }
            }

            ResetLifeSurface(modifier = Modifier.fillMaxWidth(), emphasized = taskFormExpanded) {
                Column(
                    modifier = Modifier.padding(ResetLifeSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
                ) {
                    FormToggle(
                        title = stringResource(R.string.task_form_title),
                        supportingText = stringResource(R.string.task_form_hint),
                        expanded = taskFormExpanded,
                        onToggle = { taskFormExpanded = !taskFormExpanded },
                    )
                    if (taskFormExpanded) {
                        Text(
                            text = stringResource(R.string.required_fields_hint),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        OutlinedTextField(
                            value = uiState.taskTitleInput,
                            onValueChange = onTaskTitleChanged,
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(stringResource(R.string.task_title_label)) },
                            supportingText = {
                                FieldErrorText(uiState.taskTitleError)
                            },
                            isError = uiState.taskTitleError != null,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    onAddTask()
                                    focusManager.clearFocus()
                                },
                            ),
                        )
                        ProjectSelector(
                            projects = uiState.projects,
                            selectedProjectId = uiState.selectedProjectId,
                            onProjectSelected = onProjectSelected,
                        )
                        ResetLifeSurface(muted = true, modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(ResetLifeSpacing.sm),
                                verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
                            ) {
                                Text(
                                    text = stringResource(R.string.optional_details_title),
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                Text(
                                    text = stringResource(R.string.optional_details_hint),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                OutlinedTextField(
                                    value = uiState.taskNoteInput,
                                    onValueChange = onTaskNoteChanged,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .focusRequester(taskNoteFocusRequester),
                                    label = { Text(stringResource(R.string.task_note_label)) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                    keyboardActions = KeyboardActions(
                                        onNext = { taskDueDateFocusRequester.requestFocus() },
                                    ),
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm)) {
                                    OutlinedTextField(
                                        value = uiState.taskDueDateInput,
                                        onValueChange = {
                                            onTaskDueDateChanged(it.filter(Char::isDigit).take(8))
                                        },
                                        visualTransformation = BrazilianDateVisualTransformation(),
                                        modifier = Modifier
                                            .weight(1f)
                                            .focusRequester(taskDueDateFocusRequester),
                                        label = { Text(stringResource(R.string.task_due_date_label)) },
                                        supportingText = {
                                            FieldErrorText(uiState.taskDueDateError)
                                        },
                                        isError = uiState.taskDueDateError != null,
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Next,
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onNext = { taskDurationFocusRequester.requestFocus() },
                                        ),
                                    )
                                    OutlinedTextField(
                                        value = uiState.taskEstimatedMinutesInput,
                                        onValueChange = onTaskEstimatedMinutesChanged,
                                        modifier = Modifier
                                            .weight(1f)
                                            .focusRequester(taskDurationFocusRequester),
                                        label = { Text(stringResource(R.string.task_duration_label)) },
                                        supportingText = {
                                            FieldErrorText(uiState.taskDurationError)
                                        },
                                        isError = uiState.taskDurationError != null,
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Done,
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                onAddTask()
                                                focusManager.clearFocus()
                                            },
                                        ),
                                    )
                                }
                            }
                        }
                        Button(
                            onClick = onAddTask,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isDataMutationInProgress,
                        ) {
                            Text(
                                stringResource(
                                    if (uiState.isTaskSaving) R.string.saving else R.string.add_task,
                                ),
                            )
                        }
                    }
                }
            }

            if (uiState.feedback != null && !(uiState.loadError && uiState.feedback == OrganizeFeedback.StorageError)) {
                val isSuccess = uiState.feedback == OrganizeFeedback.ProjectCreated ||
                    uiState.feedback == OrganizeFeedback.TaskCreated ||
                    uiState.feedback == OrganizeFeedback.TaskCompleted ||
                    uiState.feedback == OrganizeFeedback.TaskReopened ||
                    uiState.feedback == OrganizeFeedback.Promoted
                ResetLifeMessage(
                    text = when (uiState.feedback) {
                        OrganizeFeedback.EmptyProjectTitle -> stringResource(R.string.empty_project_title)
                        OrganizeFeedback.EmptyTaskTitle -> stringResource(R.string.empty_task_title)
                        OrganizeFeedback.InvalidDuration -> stringResource(R.string.invalid_task_duration)
                        OrganizeFeedback.ProjectCreated -> stringResource(R.string.project_created)
                        OrganizeFeedback.TaskCreated -> stringResource(R.string.task_created)
                        OrganizeFeedback.TaskCompleted -> stringResource(R.string.task_completed)
                        OrganizeFeedback.TaskReopened -> stringResource(R.string.task_reopened)
                        OrganizeFeedback.PriorityLimitReached -> stringResource(R.string.organize_priority_limit)
                        OrganizeFeedback.Promoted -> stringResource(R.string.task_promoted)
                        OrganizeFeedback.StorageError -> stringResource(R.string.organize_storage_error)
                    },
                    tone = if (isSuccess) ResetLifeMessageTone.Success else ResetLifeMessageTone.Error,
                    actionLabel = if (uiState.feedback == OrganizeFeedback.StorageError) {
                        stringResource(R.string.retry)
                    } else {
                        null
                    },
                    onAction = if (uiState.feedback == OrganizeFeedback.StorageError) onRetry else null,
                )
            }

            ResetLifeSectionHeader(
                title = stringResource(R.string.task_list_section),
                supportingText = stringResource(R.string.task_list_section_hint),
            )
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.search_tasks_label)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
            )
            if (uiState.isLoading) {
                ResetLifeLoading(text = stringResource(R.string.loading_tasks))
            } else if (uiState.loadError) {
                ResetLifeMessage(
                    text = stringResource(R.string.organize_load_error),
                    tone = ResetLifeMessageTone.Error,
                    actionLabel = stringResource(R.string.retry),
                    onAction = onRetry,
                )
            } else if (uiState.filteredTasks.isEmpty()) {
                ResetLifeMessage(
                    text = if (uiState.searchQuery.isBlank()) {
                        stringResource(R.string.no_tasks)
                    } else {
                        stringResource(R.string.no_tasks_for_search)
                    },
                    tone = ResetLifeMessageTone.Info,
                )
            } else {
                if (uiState.filteredOpenTasks.isNotEmpty()) {
                    ResetLifeSectionHeader(
                        title = stringResource(R.string.open_tasks_section),
                        supportingText = stringResource(R.string.open_tasks_hint),
                    )
                    uiState.filteredOpenTasks.forEach { task ->
                        TaskRow(
                            task = task,
                            projectTitle = uiState.projects.firstOrNull { it.id == task.projectId }?.title,
                            onToggle = { onToggleTask(task) },
                            onPromote = { onPromoteToToday(task) },
                            isActionInProgress = uiState.isDataMutationInProgress,
                        )
                    }
                }
                if (uiState.filteredCompletedTasks.isNotEmpty()) {
                    ResetLifeSectionHeader(
                        title = stringResource(R.string.completed_tasks_section),
                        supportingText = stringResource(R.string.completed_tasks_hint),
                    )
                    uiState.filteredCompletedTasks.forEach { task ->
                        TaskRow(
                            task = task,
                            projectTitle = uiState.projects.firstOrNull { it.id == task.projectId }?.title,
                            onToggle = { onToggleTask(task) },
                            onPromote = { onPromoteToToday(task) },
                            isActionInProgress = uiState.isDataMutationInProgress,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(ResetLifeSpacing.sm))
        }
    }
}

@Composable
private fun FieldErrorText(error: OrganizeFieldError?) {
    val message = when (error) {
        OrganizeFieldError.Required -> stringResource(R.string.required_field_error)
        OrganizeFieldError.InvalidDate -> stringResource(R.string.invalid_task_date)
        OrganizeFieldError.InvalidDuration -> stringResource(R.string.invalid_task_duration)
        null -> null
    }
    if (message != null) Text(message)
}

@Composable
private fun FormToggle(
    title: String,
    supportingText: String,
    expanded: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Text(
                supportingText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        TextButton(
            onClick = onToggle,
            modifier = Modifier,
        ) {
            Text(stringResource(if (expanded) R.string.collapse_form else R.string.expand_form))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectSelector(
    projects: List<Project>,
    selectedProjectId: String?,
    onProjectSelected: (String?) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val selectedTitle = projects.firstOrNull { it.id == selectedProjectId }?.title
        ?: stringResource(R.string.no_project_selected)

    Column(verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.xs)) {
        Text(
            text = stringResource(R.string.project_selector_hint),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.selected_project_label, selectedTitle))
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.no_project_selected)) },
                onClick = { onProjectSelected(null); expanded = false },
            )
            projects.forEach { project ->
                DropdownMenuItem(
                    text = { Text(project.title) },
                    onClick = { onProjectSelected(project.id); expanded = false },
                )
            }
        }
    }
}

@Composable
private fun TaskRow(
    task: Task,
    projectTitle: String?,
    onToggle: () -> Unit,
    onPromote: () -> Unit,
    isActionInProgress: Boolean,
) {
    val completed = task.status == TaskStatus.COMPLETED
    val dueDateForDisplay = task.dueDate?.let { TaskDateFormat.formatStored(it) ?: it }
    val completedLabel = stringResource(R.string.a11y_completed_indicator)
    val openLabel = stringResource(R.string.a11y_open_indicator)
    val checkboxDescription = if (completed) {
        stringResource(R.string.a11y_task_reopen_checkbox, task.title)
    } else {
        stringResource(R.string.a11y_task_checkbox, task.title)
    }
    ResetLifeSurface(
        modifier = Modifier.fillMaxWidth(),
        muted = completed,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ResetLifeSpacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = completed,
                onCheckedChange = { onToggle() },
                enabled = !isActionInProgress,
                modifier = Modifier
                    
                    .semantics {
                        contentDescription = checkboxDescription
                        stateDescription = if (completed) completedLabel else openLabel
                    },
            )
            Column(
                modifier = Modifier.weight(1f).padding(start = ResetLifeSpacing.sm),
                verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.xs),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (completed) {
                        Text(
                            text = "✓",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.semantics { contentDescription = completedLabel },
                        )
                        Spacer(modifier = Modifier.size(ResetLifeSpacing.xs))
                    }
                    Text(
                        task.title,
                        style = MaterialTheme.typography.bodyLarge,
                        textDecoration = if (completed) TextDecoration.LineThrough else null,
                        color = if (completed) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                    )
                }
                Text(
                    text = stringResource(if (completed) R.string.task_completed_state else R.string.task_open_state),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (projectTitle != null) {
                    Text(
                        stringResource(R.string.task_project_metadata, projectTitle),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
                if (dueDateForDisplay != null || task.estimatedMinutes != null) {
                    Text(
                        listOfNotNull(dueDateForDisplay, task.estimatedMinutes?.let { "$it min" }).joinToString(" • "),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            val promoteDescription = stringResource(R.string.a11y_promote_button, task.title)
            if (!completed) {
                OutlinedButton(
                    onClick = onPromote,
                    enabled = !isActionInProgress,
                    modifier = Modifier
                        
                        .semantics {
                            contentDescription = promoteDescription
                        },
                ) {
                    Text(stringResource(R.string.add_to_today))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OrganizeScreenPreview() {
    ResetLifeTheme {
        OrganizeScreen(
            uiState = OrganizeUiState(isLoading = false),
            onSearchChanged = {}, onProjectTitleChanged = {}, onProjectGoalChanged = {},
            onProjectSelected = {}, onAddProject = {}, onTaskTitleChanged = {},
            onTaskNoteChanged = {}, onTaskDueDateChanged = {}, onTaskEstimatedMinutesChanged = {},
            onAddTask = {}, onToggleTask = {}, onPromoteToToday = {}, onRetry = {},
        )
    }
}
