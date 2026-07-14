package br.com.resetlife.presentation.weeklyreview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.resetlife.R
import br.com.resetlife.presentation.components.ResetLifeSectionHeader
import br.com.resetlife.presentation.components.ResetLifeSurface
import br.com.resetlife.presentation.theme.ResetLifeSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyReviewScreen(
    state: WeeklyReviewUiState,
    onCompletedChanged: (String) -> Unit,
    onPendingChanged: (String) -> Unit,
    onDifficultyChanged: (String) -> Unit,
    onNextWeekChanged: (String) -> Unit,
    onHabitChanged: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = ResetLifeSpacing.screenHorizontal)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.lg),
        ) {
            ResetLifeSectionHeader(
                title = stringResource(R.string.weekly_review_title),
                supportingText = stringResource(R.string.weekly_review_subtitle),
            )

            // Resumo da semana
            ResetLifeSurface(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(ResetLifeSpacing.md)) {
                    Text(
                        text = stringResource(R.string.weekly_review_summary),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = stringResource(
                            R.string.weekly_review_summary_values,
                            state.completedCount,
                            state.pendingCount,
                            state.taskDoneCount,
                            state.taskOpenCount,
                            state.checkInCount,
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            ReviewField(
                label = stringResource(R.string.weekly_review_completed_label),
                value = state.completed,
                onValueChange = onCompletedChanged,
            )
            ReviewField(
                label = stringResource(R.string.weekly_review_pending_label),
                value = state.pending,
                onValueChange = onPendingChanged,
            )
            ReviewField(
                label = stringResource(R.string.weekly_review_difficulty_label),
                value = state.difficulty,
                onValueChange = onDifficultyChanged,
            )
            ReviewField(
                label = stringResource(R.string.weekly_review_next_week_label),
                value = state.nextWeekPriorities,
                onValueChange = onNextWeekChanged,
            )
            ReviewField(
                label = stringResource(R.string.weekly_review_habit_label),
                value = state.habitToAdjust,
                onValueChange = onHabitChanged,
            )

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.save_weekly_review))
            }
        }
    }
}

@Composable
private fun ReviewField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = ResetLifeSpacing.xs),
            minLines = 2,
            maxLines = 4,
        )
    }
}
