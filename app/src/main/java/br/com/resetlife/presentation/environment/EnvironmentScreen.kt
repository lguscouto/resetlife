package br.com.resetlife.presentation.environment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.resetlife.R
import br.com.resetlife.domain.environment.EnvironmentTask
import br.com.resetlife.presentation.components.ResetLifeSectionHeader
import br.com.resetlife.presentation.components.ResetLifeEmptyState
import br.com.resetlife.presentation.components.ResetLifeSurface
import br.com.resetlife.presentation.theme.ResetLifeSpacing

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EnvironmentScreen(
    state: EnvironmentUiState,
    onSelectSpace: (String) -> Unit,
    onShowAddSpaceDialog: () -> Unit,
    onHideAddSpaceDialog: () -> Unit,
    onNewSpaceNameChanged: (String) -> Unit,
    onAddSpace: () -> Unit,
    onShowAddTaskDialog: () -> Unit,
    onHideAddTaskDialog: () -> Unit,
    onNewTaskTitleChanged: (String) -> Unit,
    onNewTaskMinutesChanged: (Int) -> Unit,
    onAddToDiscardListChanged: (Boolean) -> Unit,
    onAddTask: () -> Unit,
    onToggleTask: (EnvironmentTask) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = ResetLifeSpacing.screenHorizontal)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.lg),
    ) {
        ResetLifeSectionHeader(
            title = stringResource(R.string.environment_title),
            supportingText = stringResource(R.string.environment_subtitle),
        )

        // Seletor de espaços
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
            verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
        ) {
            state.spaces.forEach { space ->
                val selected = space.id == state.selectedSpaceId
                Surface(
                    modifier = Modifier
                        .clickable { onSelectSpace(space.id) },
                    shape = MaterialTheme.shapes.medium,
                    color = if (selected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant,
                    ),
                ) {
                    Column(modifier = Modifier.padding(ResetLifeSpacing.sm)) {
                        Text(
                            text = space.name,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        space.lastOrganizedAt?.let { date ->
                            Text(
                                text = stringResource(R.string.environment_last_organized, date),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
            TextButton(onClick = onShowAddSpaceDialog) {
                Text(stringResource(R.string.environment_add_space))
            }
        }

        // Tarefas do espaço selecionado
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.environment_tasks),
                style = MaterialTheme.typography.titleMedium,
            )
            OutlinedButton(onClick = onShowAddTaskDialog) {
                Text(stringResource(R.string.environment_add_task))
            }
        }

        if (state.tasks.isEmpty()) {
            ResetLifeEmptyState(
                title = stringResource(R.string.empty_environment),
                description = stringResource(R.string.empty_environment_hint),
                actionLabel = stringResource(R.string.environment_add_space),
                onAction = onShowAddSpaceDialog,
            )
        } else {
            state.tasks.forEach { task ->
                TaskRow(task = task, onToggle = { onToggleTask(task) })
            }
        }

        // Lista de descarte/doação
        ResetLifeSectionHeader(
            title = stringResource(R.string.environment_discard_title),
            supportingText = stringResource(R.string.environment_discard_subtitle),
        )
        if (state.discardList.isEmpty()) {
            ResetLifeEmptyState(
                title = stringResource(R.string.empty_environment),
                description = stringResource(R.string.environment_discard_empty),
            )
        } else {
            state.discardList.forEach { task ->
                TaskRow(task = task, onToggle = { onToggleTask(task) })
            }
        }

        if (state.showAddSpaceDialog) {
            AlertDialog(
                onDismissRequest = onHideAddSpaceDialog,
                title = { Text(stringResource(R.string.environment_add_space)) },
                text = {
                    TextField(
                        value = state.newSpaceName,
                        onValueChange = onNewSpaceNameChanged,
                        label = { Text(stringResource(R.string.environment_space_name)) },
                        singleLine = true,
                    )
                },
                confirmButton = {
                    TextButton(onClick = onAddSpace) {
                        Text(stringResource(R.string.save))
                    }
                },
                dismissButton = {
                    TextButton(onClick = onHideAddSpaceDialog) {
                        Text(stringResource(R.string.cancel))
                    }
                },
            )
        }

        if (state.showAddTaskDialog) {
            AlertDialog(
                onDismissRequest = onHideAddTaskDialog,
                title = { Text(stringResource(R.string.environment_add_task)) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm)) {
                        TextField(
                            value = state.newTaskTitle,
                            onValueChange = onNewTaskTitleChanged,
                            label = { Text(stringResource(R.string.environment_task_title)) },
                            singleLine = true,
                        )
                        Text(
                            text = stringResource(R.string.environment_duration),
                            style = MaterialTheme.typography.labelMedium,
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm)) {
                            listOf(5, 15, 30).forEach { minutes ->
                                val selected = state.newTaskMinutes == minutes
                                Surface(
                                    modifier = Modifier.clickable { onNewTaskMinutesChanged(minutes) },
                                    shape = MaterialTheme.shapes.medium,
                                    color = if (selected) {
                                        MaterialTheme.colorScheme.primaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant
                                    },
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        MaterialTheme.colorScheme.outlineVariant,
                                    ),
                                ) {
                                    Text(
                                        text = stringResource(R.string.environment_minutes, minutes),
                                        modifier = Modifier.padding(ResetLifeSpacing.sm),
                                        style = MaterialTheme.typography.labelLarge,
                                    )
                                }
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = state.addToDiscardList,
                                onCheckedChange = onAddToDiscardListChanged,
                            )
                            Text(stringResource(R.string.environment_add_to_discard))
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = onAddTask) {
                        Text(stringResource(R.string.save))
                    }
                },
                dismissButton = {
                    TextButton(onClick = onHideAddTaskDialog) {
                        Text(stringResource(R.string.cancel))
                    }
                },
            )
        }
    }
}

@Composable
private fun TaskRow(
    task: br.com.resetlife.domain.environment.EnvironmentTask,
    onToggle: () -> Unit,
) {
    ResetLifeSurface(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ResetLifeSpacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
        ) {
            Checkbox(checked = task.done, onCheckedChange = { onToggle() })
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = stringResource(R.string.environment_minutes, task.estimatedMinutes),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
