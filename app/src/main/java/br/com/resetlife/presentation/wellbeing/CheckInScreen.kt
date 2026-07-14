package br.com.resetlife.presentation.wellbeing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.resetlife.R
import br.com.resetlife.presentation.components.ResetLifeSectionHeader
import br.com.resetlife.presentation.components.ResetLifeSurface
import br.com.resetlife.presentation.theme.ResetLifeSpacing

@Composable
fun CheckInScreen(
    state: CheckInUiState,
    onMoodSelected: (Int) -> Unit,
    onEnergySelected: (Int) -> Unit,
    onStressSelected: (Int) -> Unit,
    onSleepSelected: (Int) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ResetLifeSectionHeader(
        title = stringResource(R.string.wellbeing_title),
        supportingText = stringResource(R.string.wellbeing_subtitle),
    )

    CheckInSlider(
        label = stringResource(R.string.mood_label),
        value = state.mood,
        onSelect = onMoodSelected,
    )
    CheckInSlider(
        label = stringResource(R.string.energy_label),
        value = state.energy,
        onSelect = onEnergySelected,
    )
    CheckInSlider(
        label = stringResource(R.string.stress_label),
        value = state.stress,
        onSelect = onStressSelected,
    )
    CheckInSlider(
        label = stringResource(R.string.sleep_label),
        value = state.sleepPerceived,
        onSelect = onSleepSelected,
    )

    Button(
        onClick = onSave,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(text = stringResource(R.string.save_checkin))
    }
}

@Composable
private fun CheckInSlider(
    label: String,
    value: Int?,
    onSelect: (Int) -> Unit,
) {
    ResetLifeSurface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = ResetLifeSpacing.xs),
    ) {
        Column(modifier = Modifier.padding(ResetLifeSpacing.sm)) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(ResetLifeSpacing.xs))
            Row(
                horizontalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
            ) {
                (1..5).forEach { i ->
                    Button(
                        onClick = { onSelect(i) },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(text = "$i")
                    }
                }
            }
        }
    }
}