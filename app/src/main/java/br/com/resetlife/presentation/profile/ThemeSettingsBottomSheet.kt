package br.com.resetlife.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.resetlife.R
import br.com.resetlife.presentation.theme.ResetLifeSpacing
import br.com.resetlife.presentation.theme.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSettingsBottomSheet(
    current: ThemeMode,
    onSelect: (ThemeMode) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = ResetLifeSpacing.screenHorizontal,
                    end = ResetLifeSpacing.screenHorizontal,
                    bottom = ResetLifeSpacing.lg,
                ),
            verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.xs),
        ) {
            Text(
                text = stringResource(R.string.theme_settings_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = ResetLifeSpacing.sm),
            )
            Text(
                text = stringResource(R.string.theme_settings_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = ResetLifeSpacing.md),
            )
            ThemeOption(
                selected = current == ThemeMode.LIGHT,
                label = stringResource(R.string.theme_mode_light),
                onClick = { onSelect(ThemeMode.LIGHT) },
            )
            ThemeOption(
                selected = current == ThemeMode.DARK,
                label = stringResource(R.string.theme_mode_dark),
                onClick = { onSelect(ThemeMode.DARK) },
            )
            ThemeOption(
                selected = current == ThemeMode.SYSTEM,
                label = stringResource(R.string.theme_mode_system),
                onClick = { onSelect(ThemeMode.SYSTEM) },
            )
        }
    }
}

@Composable
private fun ThemeOption(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = ResetLifeSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ResetLifeSpacing.md),
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
