package br.com.resetlife.presentation.wellbeing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import br.com.resetlife.R
import br.com.resetlife.presentation.components.ResetLifeMessage
import br.com.resetlife.presentation.components.ResetLifeMessageTone
import br.com.resetlife.presentation.components.ResetLifeSectionHeader
import br.com.resetlife.presentation.theme.ResetLifeSpacing
import br.com.resetlife.presentation.theme.ResetLifeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInScreen(
    uiState: CheckInUiState,
    onMoodChanged: (Int) -> Unit,
    onEnergyChanged: (Int) -> Unit,
    onStressChanged: (Int) -> Unit,
    onSleepChanged: (Int) -> Unit,
    onNoteChanged: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.wellbeing_nav)) },
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
                .padding(horizontal = ResetLifeSpacing.screenHorizontal, vertical = ResetLifeSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.md),
        ) {
            ResetLifeSectionHeader(
                title = stringResource(R.string.wellbeing_title),
                supportingText = stringResource(R.string.wellbeing_subtitle),
            )

            WellbeingSlider(
                label = stringResource(R.string.mood_label),
                value = uiState.mood?.toFloat() ?: 3f,
                onValueChanged = { onMoodChanged(it.toInt()) },
            )

            WellbeingSlider(
                label = stringResource(R.string.energy_label),
                value = uiState.energy?.toFloat() ?: 3f,
                onValueChanged = { onEnergyChanged(it.toInt()) },
            )

            WellbeingSlider(
                label = stringResource(R.string.stress_label),
                value = uiState.stress?.toFloat() ?: 3f,
                onValueChanged = { onStressChanged(it.toInt()) },
            )

            WellbeingSlider(
                label = stringResource(R.string.sleep_label),
                value = uiState.sleepPerceived?.toFloat() ?: 3f,
                onValueChanged = { onSleepChanged(it.toInt()) },
            )

            OutlinedTextField(
                value = uiState.note,
                onValueChange = onNoteChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.note_label)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    onSave()
                    focusManager.clearFocus()
                }),
            )

            Button(
                onClick = {
                    onSave()
                    focusManager.clearFocus()
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.save_checkin))
            }

            if (uiState.saved) {
                ResetLifeMessage(
                    text = stringResource(R.string.checkin_saved),
                    tone = ResetLifeMessageTone.Success,
                )
            }
        }
    }
}

@Composable
private fun WellbeingSlider(
    label: String,
    value: Float,
    onValueChanged: (Float) -> Unit,
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(ResetLifeSpacing.xs))
        Slider(
            value = value,
            onValueChange = onValueChanged,
            valueRange = 1f..5f,
            steps = 3,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckInScreenPreview() {
    ResetLifeTheme {
        CheckInScreen(
            uiState = CheckInUiState(
                mood = 4,
                energy = 3,
                stress = 2,
                sleepPerceived = 4,
                note = "Bom dia!",
            ),
            onMoodChanged = {},
            onEnergyChanged = {},
            onStressChanged = {},
            onSleepChanged = {},
            onNoteChanged = {},
            onSave = {},
        )
    }
}