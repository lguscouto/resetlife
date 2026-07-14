package br.com.resetlife.presentation.habit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.resetlife.R
import br.com.resetlife.domain.habit.HabitFrequency
import br.com.resetlife.domain.habit.HabitGoalType
import br.com.resetlife.presentation.components.ResetLifeMessage
import br.com.resetlife.presentation.components.ResetLifeMessageTone
import br.com.resetlife.presentation.components.ResetLifeSectionHeader
import br.com.resetlife.presentation.components.ResetLifeSurface
import br.com.resetlife.presentation.theme.ResetLifeSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(
    state: HabitUiState,
    onToggleToday: (br.com.resetlife.domain.habit.Habit) -> Unit,
    onQuantityChanged: (br.com.resetlife.domain.habit.Habit, Int) -> Unit,
    onPause: (br.com.resetlife.domain.habit.Habit) -> Unit,
    onResume: (br.com.resetlife.domain.habit.Habit) -> Unit,
    onArchive: (br.com.resetlife.domain.habit.Habit) -> Unit,
    onOpenDetail: (br.com.resetlife.domain.habit.Habit) -> Unit,
    onShowAddDialog: () -> Unit,
    onHideAddDialog: () -> Unit,
    onAddHabit: (String, HabitFrequency, HabitGoalType, Int?, String?) -> Unit,
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
                title = stringResource(R.string.habits_title),
                supportingText = stringResource(R.string.habits_subtitle),
            )

            Button(
                onClick = onShowAddDialog,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.habit_add))
            }

            if (state.errorMessage != null) {
                ResetLifeMessage(
                    text = state.errorMessage!!,
                    tone = ResetLifeMessageTone.Error,
                )
            }

            if (state.habits.isEmpty()) {
                ResetLifeMessage(
                    text = stringResource(R.string.habits_empty),
                    tone = ResetLifeMessageTone.Info,
                )
            } else {
                state.habits.forEach { item ->
                    HabitRow(
                        item = item,
                        onToggleToday = onToggleToday,
                        onQuantityChanged = onQuantityChanged,
                        onPause = onPause,
                        onResume = onResume,
                        onArchive = onArchive,
                        onOpenDetail = onOpenDetail,
                    )
                }
            }
        }
    }

    if (state.showAddDialog) {
        AddHabitDialog(
            errorMessage = state.errorMessage,
            onDismiss = onHideAddDialog,
            onAdd = onAddHabit,
        )
    }
}

@Composable
private fun HabitRow(
    item: HabitUiItem,
    onToggleToday: (br.com.resetlife.domain.habit.Habit) -> Unit,
    onQuantityChanged: (br.com.resetlife.domain.habit.Habit, Int) -> Unit,
    onPause: (br.com.resetlife.domain.habit.Habit) -> Unit,
    onResume: (br.com.resetlife.domain.habit.Habit) -> Unit,
    onArchive: (br.com.resetlife.domain.habit.Habit) -> Unit,
    onOpenDetail: (br.com.resetlife.domain.habit.Habit) -> Unit,
) {
    ResetLifeSurface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenDetail(item.habit) },
    ) {
        Column(modifier = Modifier.padding(ResetLifeSpacing.md)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
            ) {
                Checkbox(
                    checked = item.doneToday,
                    onCheckedChange = { onToggleToday(item.habit) },
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.habit.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    val detail = when (item.habit.goalType) {
                        HabitGoalType.BINARY -> if (item.habit.paused) stringResource(R.string.habit_paused) else stringResource(R.string.habit_daily)
                        HabitGoalType.QUANTITY -> {
                            val target = item.habit.targetValue ?: 1
                            val unit = item.habit.unit ?: ""
                            val logged = item.loggedValueToday ?: 0
                            "$logged/$target $unit".trim()
                        }
                    }
                    Text(
                        text = detail,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (item.habit.paused) {
                    TextButton(onClick = { onResume(item.habit) }) {
                        Text(text = stringResource(R.string.habit_resume))
                    }
                } else {
                    TextButton(onClick = { onPause(item.habit) }) {
                        Text(text = stringResource(R.string.habit_pause))
                    }
                }
            }
            if (item.habit.goalType == HabitGoalType.QUANTITY && !item.habit.paused) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = ResetLifeSpacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
                ) {
                    Text(text = stringResource(R.string.habit_value))
                    androidx.compose.material3.Slider(
                        value = (item.loggedValueToday ?: 0).toFloat(),
                        onValueChange = { onQuantityChanged(item.habit, it.toInt()) },
                        valueRange = 0f..((item.habit.targetValue ?: 5).toFloat() * 2),
                        steps = ((item.habit.targetValue ?: 5) * 2),
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            TextButton(onClick = { onArchive(item.habit) }) {
                Text(text = stringResource(R.string.habit_archive))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddHabitDialog(
    errorMessage: String?,
    onDismiss: () -> Unit,
    onAdd: (String, HabitFrequency, HabitGoalType, Int?, String?) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf(HabitFrequency.DAILY) }
    var goalType by remember { mutableStateOf(HabitGoalType.BINARY) }
    var targetValue by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val target = targetValue.toIntOrNull()
                    onAdd(name, frequency, goalType, target, unit.takeIf { it.isNotBlank() })
                },
            ) {
                Text(text = stringResource(R.string.habit_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        title = { Text(text = stringResource(R.string.habit_new_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.habit_name)) },
                    modifier = Modifier.fillMaxWidth(),
                )
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                OutlinedButton(onClick = {
                    frequency = if (frequency == HabitFrequency.DAILY) HabitFrequency.WEEKDAYS else HabitFrequency.DAILY
                }) {
                    Text(
                        text = if (frequency == HabitFrequency.DAILY) {
                            stringResource(R.string.habit_freq_daily)
                        } else {
                            stringResource(R.string.habit_freq_weekdays)
                        },
                    )
                }
                OutlinedButton(onClick = {
                    goalType = if (goalType == HabitGoalType.BINARY) HabitGoalType.QUANTITY else HabitGoalType.BINARY
                }) {
                    Text(
                        text = if (goalType == HabitGoalType.BINARY) {
                            stringResource(R.string.habit_goal_binary)
                        } else {
                            stringResource(R.string.habit_goal_quantity)
                        },
                    )
                }
                if (goalType == HabitGoalType.QUANTITY) {
                    OutlinedTextField(
                        value = targetValue,
                        onValueChange = { targetValue = it.filter { c -> c.isDigit() } },
                        label = { Text(stringResource(R.string.habit_target)) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text(stringResource(R.string.habit_unit)) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
    )
}
