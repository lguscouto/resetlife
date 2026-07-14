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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import br.com.resetlife.R
import br.com.resetlife.domain.habit.HabitType
import br.com.resetlife.presentation.components.ResetLifeMessage
import br.com.resetlife.presentation.components.ResetLifeMessageTone
import br.com.resetlife.presentation.components.ResetLifeSectionHeader
import br.com.resetlife.presentation.components.ResetLifeSurface
import br.com.resetlife.presentation.habit.parseHabitColor
import br.com.resetlife.presentation.theme.ResetLifeSpacing
import br.com.resetlife.presentation.theme.LocalResetLifeColors

@Composable
fun HabitDetailScreen(
    state: HabitDetailUiState,
    onBack: () -> Unit,
    onToggleToday: (br.com.resetlife.domain.habit.Habit) -> Unit,
    onPause: (br.com.resetlife.domain.habit.Habit) -> Unit,
    onResume: (br.com.resetlife.domain.habit.Habit) -> Unit,
    onRelaxedModeChanged: (Boolean) -> Unit = {},
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

            state.habit?.let { habit ->
                ResetLifeSurface(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(ResetLifeSpacing.md),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
                    ) {
                        Text(
                            text = stringResource(R.string.habit_type),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = if (habit.type == HabitType.AVOID) {
                                stringResource(R.string.habit_type_avoid)
                            } else {
                                stringResource(R.string.habit_type_habit)
                            },
                            style = MaterialTheme.typography.titleMedium,
                            color = if (habit.type == HabitType.AVOID) {
                                LocalResetLifeColors.current.warning
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                        )
                    }
                }
            }

            // Modo "sem pressão": oculta a contagem de sequência e mostra uma mensagem gentil.
            val relaxedDescription = if (state.relaxedMode) {
                stringResource(R.string.relaxed_mode_on)
            } else {
                stringResource(R.string.relaxed_mode_off)
            }
            ResetLifeSurface(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(ResetLifeSpacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(ResetLifeSpacing.md),
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.xs),
                    ) {
                        Text(
                            text = stringResource(R.string.relaxed_mode_title),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = stringResource(R.string.relaxed_mode_hint),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Switch(
                        checked = state.relaxedMode,
                        onCheckedChange = onRelaxedModeChanged,
                        modifier = Modifier.semantics {
                            contentDescription = relaxedDescription
                        },
                    )
                }
            }

            if (state.logs.isEmpty()) {
                ResetLifeMessage(
                    text = stringResource(R.string.habit_no_history),
                    tone = ResetLifeMessageTone.Info,
                )
            } else {
                if (state.relaxedMode) {
                    // Sem pressão: mensagem gentil no lugar da contagem de sequência.
                    ResetLifeMessage(
                        text = stringResource(R.string.relaxed_mode_message),
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
                }

                // O calendário é informação (não pressão) e permanece visível em ambos os modos.
                ResetLifeSectionHeader(title = stringResource(R.string.habit_calendar_title))
                StreakCalendar(
                    doneDates = state.logs.filter { it.done }.map { it.date }.toSet(),
                    accentColor = state.habit?.colorHex?.let { parseHabitColor(it) },
                )
            }

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.back))
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
