package br.com.resetlife.presentation.habit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.resetlife.R
import br.com.resetlife.presentation.components.ResetLifeMessage
import br.com.resetlife.presentation.components.ResetLifeMessageTone
import br.com.resetlife.presentation.components.ResetLifeSectionHeader
import br.com.resetlife.presentation.components.ResetLifeSurface
import br.com.resetlife.presentation.theme.ResetLifeSpacing

@Composable
fun HabitDetailScreen(
    state: HabitDetailUiState,
    onBack: () -> Unit,
    onToggleToday: (br.com.resetlife.domain.habit.Habit) -> Unit,
    onPause: (br.com.resetlife.domain.habit.Habit) -> Unit,
    onResume: (br.com.resetlife.domain.habit.Habit) -> Unit,
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
                title = state.habit?.name ?: stringResource(R.string.habit_detail_title),
                supportingText = stringResource(R.string.habit_detail_subtitle),
            )

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.back))
            }

            if (state.logs.isEmpty()) {
                ResetLifeMessage(
                    text = stringResource(R.string.habit_no_history),
                    tone = ResetLifeMessageTone.Info,
                )
            } else {
                ResetLifeSurface(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(ResetLifeSpacing.md),
                        verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(R.string.habit_streak_label),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = stringResource(R.string.habit_streak_value, state.streakCurrent),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                        val percent = (state.completionRate * 100).toInt()
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(R.string.habit_completion_rate),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = stringResource(R.string.habit_completion_value, percent),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }

                ResetLifeSectionHeader(title = stringResource(R.string.habit_calendar_title))
                StreakCalendar(
                    doneDates = state.logs.filter { it.done }.map { it.date }.toSet(),
                )
            }

            state.habit?.let { habit ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
                ) {
                    Button(
                        onClick = { onToggleToday(habit) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = stringResource(R.string.habit_toggle_today))
                    }
                    if (habit.paused) {
                        OutlinedButton(onClick = { onResume(habit) }) {
                            Text(text = stringResource(R.string.habit_resume))
                        }
                    } else {
                        OutlinedButton(onClick = { onPause(habit) }) {
                            Text(text = stringResource(R.string.habit_pause))
                        }
                    }
                }
            }
        }
    }
}
