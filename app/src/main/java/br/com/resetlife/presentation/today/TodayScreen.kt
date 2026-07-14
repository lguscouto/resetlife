package br.com.resetlife.presentation.today

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.resetlife.R
import br.com.resetlife.domain.today.PriorityItem
import br.com.resetlife.presentation.components.ResetLifeLoading
import br.com.resetlife.presentation.components.ResetLifeMessage
import br.com.resetlife.presentation.components.ResetLifeMessageTone
import br.com.resetlife.presentation.components.ResetLifeSectionHeader
import br.com.resetlife.presentation.components.ResetLifeSurface
import br.com.resetlife.presentation.theme.ResetLifeSpacing
import br.com.resetlife.presentation.theme.ResetLifeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    uiState: TodayUiState,
    onPriorityInputChanged: (String) -> Unit,
    onAddPriority: () -> Unit,
    onCompletePriority: (String) -> Unit,
    onCompleteEnvironmentSuggestion: () -> Unit,
    onOpenEnvironment: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.today_nav)) },
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
                .padding(horizontal = ResetLifeSpacing.screenHorizontal, vertical = ResetLifeSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.md),
        ) {
            ResetLifeSectionHeader(
                title = stringResource(R.string.today_title),
                supportingText = stringResource(R.string.today_subtitle),
            )

            uiState.environmentSuggestion?.let { task ->
                ResetLifeSurface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onOpenEnvironment),
                ) {
                    Row(
                        modifier = Modifier.padding(ResetLifeSpacing.md),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(ResetLifeSpacing.md),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Eco,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp),
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = ResetLifeSpacing.sm),
                            verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.xs),
                        ) {
                            Text(
                                text = stringResource(R.string.today_env_suggestion),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                        Button(onClick = onCompleteEnvironmentSuggestion) {
                            Text(stringResource(R.string.done))
                        }
                    }
                }
            }

            ResetLifeSurface(
                modifier = Modifier.fillMaxWidth(),
                emphasized = true,
            ) {
                Column(modifier = Modifier.padding(ResetLifeSpacing.md)) {
                    Text(
                        text = stringResource(
                            R.string.active_priority_count,
                            uiState.activePriorityCount,
                            3,
                        ),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.height(ResetLifeSpacing.xs))
                    Text(
                        text = stringResource(R.string.priority_capacity_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            ResetLifeSurface(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(ResetLifeSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
                ) {
                    Text(
                        text = stringResource(R.string.next_step_section),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = stringResource(R.string.next_step_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    OutlinedTextField(
                        value = uiState.priorityInput,
                        onValueChange = onPriorityInputChanged,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = stringResource(R.string.priority_input_label)) },
                        isError = uiState.feedback == TodayFeedback.EmptyTitle,
                        supportingText = if (uiState.feedback == TodayFeedback.EmptyTitle) {
                            { Text(stringResource(R.string.empty_priority_title)) }
                        } else {
                            null
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                onAddPriority()
                                focusManager.clearFocus()
                            },
                        ),
                    )
                    Button(
                        onClick = onAddPriority,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState.isAddPriorityEnabled,
                    ) {
                        Text(
                            text = stringResource(
                                if (uiState.isSavingPriority) R.string.saving else R.string.add_priority,
                            ),
                        )
                    }
                }
            }

            if (uiState.feedback != null && !(uiState.loadError && uiState.feedback == TodayFeedback.StorageError)) {
                val feedbackText = when (uiState.feedback) {
                    TodayFeedback.Added -> stringResource(R.string.priority_added)
                    TodayFeedback.Completed -> stringResource(R.string.priority_completed)
                    TodayFeedback.EmptyTitle -> stringResource(R.string.empty_priority_title)
                    TodayFeedback.LimitReached -> stringResource(R.string.priority_limit_reached)
                    TodayFeedback.StorageError -> stringResource(R.string.priority_storage_error)
                }
                ResetLifeMessage(
                    text = feedbackText,
                    tone = if (uiState.feedback == TodayFeedback.Added || uiState.feedback == TodayFeedback.Completed) {
                        ResetLifeMessageTone.Success
                    } else {
                        ResetLifeMessageTone.Error
                    },
                    actionLabel = if (uiState.feedback == TodayFeedback.StorageError) {
                        stringResource(R.string.retry)
                    } else {
                        null
                    },
                    onAction = if (uiState.feedback == TodayFeedback.StorageError) onRetry else null,
                )
            }

            if (uiState.isLoading) {
                ResetLifeLoading(text = stringResource(R.string.loading_priorities))
            } else if (uiState.loadError) {
                ResetLifeMessage(
                    text = stringResource(R.string.priority_load_error),
                    tone = ResetLifeMessageTone.Error,
                    actionLabel = stringResource(R.string.retry),
                    onAction = onRetry,
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
                ) {
                    if (uiState.activePriorities.isEmpty()) {
                        item {
                            ResetLifeMessage(
                                text = if (uiState.completedPriorities.isEmpty()) {
                                    stringResource(R.string.no_priorities)
                                } else {
                                    stringResource(R.string.no_active_priorities)
                                },
                                tone = ResetLifeMessageTone.Info,
                            )
                        }
                    } else {
                        item {
                            ResetLifeSectionHeader(
                                title = stringResource(R.string.active_priorities_section),
                                supportingText = stringResource(R.string.active_priorities_hint),
                            )
                        }
                        items(uiState.activePriorities, key = { priority -> priority.id }) { priority ->
                            ResetLifeSurface(modifier = Modifier.fillMaxWidth()) {
                                PriorityRow(
                                    priority = priority,
                                    onComplete = onCompletePriority,
                                    isActionInProgress = uiState.isPriorityActionInProgress,
                                )
                            }
                        }
                    }

                    if (uiState.completedPriorities.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(ResetLifeSpacing.sm))
                            ResetLifeSectionHeader(
                                title = stringResource(R.string.completed_priorities_section),
                                supportingText = stringResource(R.string.completed_priorities_hint),
                            )
                        }
                        items(uiState.completedPriorities, key = { priority -> priority.id }) { priority ->
                            ResetLifeSurface(
                                modifier = Modifier.fillMaxWidth(),
                                muted = true,
                            ) {
                                PriorityRow(
                                    priority = priority,
                                    onComplete = onCompletePriority,
                                    isActionInProgress = uiState.isPriorityActionInProgress,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PriorityRow(
    priority: PriorityItem,
    onComplete: (String) -> Unit,
    isActionInProgress: Boolean,
) {
    val completedLabel = stringResource(R.string.a11y_completed_indicator)
    val openLabel = stringResource(R.string.a11y_open_indicator)
    val checkboxDescription = stringResource(R.string.a11y_priority_checkbox, priority.title)
    val priorityDescription = if (priority.isCompleted) {
        stringResource(R.string.completed_priority_description, priority.title)
    } else {
        stringResource(R.string.active_priority_description, priority.title)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                contentDescription = priorityDescription
                stateDescription = if (priority.isCompleted) completedLabel else openLabel
            }
            .padding(horizontal = ResetLifeSpacing.sm, vertical = ResetLifeSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = priority.isCompleted,
            onCheckedChange = { checked ->
                if (checked && !priority.isCompleted) {
                    onComplete(priority.id)
                }
            },
            modifier = Modifier.semantics {
                contentDescription = checkboxDescription
                stateDescription = if (priority.isCompleted) completedLabel else openLabel
            },
            enabled = !priority.isCompleted && !isActionInProgress,
        )
        Spacer(modifier = Modifier.size(ResetLifeSpacing.sm))
        if (priority.isCompleted) {
            Text(
                text = "✓",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.semantics { contentDescription = completedLabel },
            )
            Spacer(modifier = Modifier.size(ResetLifeSpacing.xs))
        }
        Text(
            text = priority.title,
            modifier = Modifier.padding(start = ResetLifeSpacing.sm),
            style = MaterialTheme.typography.bodyLarge,
            textDecoration = if (priority.isCompleted) TextDecoration.LineThrough else null,
            color = if (priority.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TodayScreenPreview() {
    ResetLifeTheme {
        TodayScreen(
            uiState = TodayUiState(
                priorityInput = "",
                priorities = listOf(
                    PriorityItem(id = "1", title = "Organizar documentos"),
                    PriorityItem(id = "2", title = "Caminhar 15 minutos", isCompleted = true),
                ),
                isLoading = false,
            ),
            onPriorityInputChanged = {},
            onAddPriority = {},
            onCompletePriority = {},
            onCompleteEnvironmentSuggestion = {},
            onOpenEnvironment = {},
            onRetry = {},
        )
    }
}