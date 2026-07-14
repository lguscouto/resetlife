package br.com.resetlife.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import br.com.resetlife.R
import br.com.resetlife.data.reminder.ReminderScheduler
import br.com.resetlife.presentation.components.ResetLifeMessage
import br.com.resetlife.presentation.components.ResetLifeMessageTone
import br.com.resetlife.presentation.components.ResetLifeSectionHeader
import br.com.resetlife.presentation.components.ResetLifeSurface
import br.com.resetlife.presentation.navigation.ResetLifeDestination
import br.com.resetlife.presentation.theme.ResetLifeSpacing
import br.com.resetlife.presentation.theme.ReminderPreferences
import br.com.resetlife.presentation.theme.ThemeManager
import br.com.resetlife.presentation.theme.ThemeMode

@Composable
fun ProfileScreen(
    themeManager: ThemeManager,
    reminderPreferences: ReminderPreferences,
    onNavigate: (ResetLifeDestination) -> Unit,
    dataExportViewModel: DataExportViewModel,
    modifier: Modifier = Modifier,
) {
    val currentThemeMode by themeManager.themeMode.collectAsStateWithLifecycle(ThemeMode.SYSTEM)
    val exportState by dataExportViewModel.uiState.collectAsStateWithLifecycle()
    val reminderEnabled by reminderPreferences.enabled.collectAsStateWithLifecycle(false)
    val reminderHour by reminderPreferences.hour.collectAsStateWithLifecycle(20)
    val reminderMinute by reminderPreferences.minute.collectAsStateWithLifecycle(0)
    var showThemeSheet by remember { mutableStateOf(false) }
    var showReminderSheet by remember { mutableStateOf(false) }
    var reminderScheduled by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
        onResult = { uri ->
            if (uri != null) {
                dataExportViewModel.exportTo(uri, context)
            }
        },
    )

    if (showThemeSheet) {
        ThemeSettingsBottomSheet(
            current = currentThemeMode,
            onSelect = { mode ->
                scope.launch { themeManager.setThemeMode(mode) }
                showThemeSheet = false
            },
            onDismiss = { showThemeSheet = false },
        )
    }

    if (showReminderSheet) {
        ReminderSettingsBottomSheet(
            enabled = reminderEnabled,
            hour = reminderHour,
            minute = reminderMinute,
            onEnabledChange = { enabled ->
                scope.launch {
                    reminderPreferences.setEnabled(enabled)
                    if (enabled) {
                        ReminderScheduler.schedule(context, reminderHour, reminderMinute)
                        reminderScheduled = true
                    } else {
                        ReminderScheduler.cancel(context)
                        reminderScheduled = false
                    }
                }
                if (!enabled) showReminderSheet = false
            },
            onTimeChange = { h, m ->
                scope.launch {
                    reminderPreferences.setTime(h, m)
                    if (reminderEnabled) {
                        ReminderScheduler.schedule(context, h, m)
                        reminderScheduled = true
                    }
                }
            },
            onDismiss = { showReminderSheet = false },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = ResetLifeSpacing.screenHorizontal)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.lg),
    ) {
        ResetLifeSectionHeader(
            title = stringResource(R.string.profile_title),
            supportingText = stringResource(R.string.profile_subtitle),
        )
        AccessCard(
            title = stringResource(R.string.weekly_review_nav),
            description = stringResource(R.string.profile_review_hint),
            icon = Icons.Filled.AutoAwesome,
            onClick = { onNavigate(ResetLifeDestination.WeeklyReview) },
        )
        AccessCard(
            title = stringResource(R.string.theme_settings_title),
            description = stringResource(R.string.profile_settings_hint),
            icon = Icons.Filled.Palette,
            onClick = { showThemeSheet = true },
        )
        AccessCard(
            title = stringResource(R.string.reminder_title),
            description = if (reminderEnabled) {
                stringResource(R.string.reminder_hint)
            } else {
                stringResource(R.string.reminder_disabled_hint)
            },
            icon = Icons.Filled.Notifications,
            onClick = { showReminderSheet = true },
        )
        val exportFilename = stringResource(R.string.data_export_filename)
        DataCard(
            title = stringResource(R.string.data_export_title),
            description = stringResource(R.string.data_export_hint),
            buttonLabel = stringResource(R.string.data_export_button),
            exporting = exportState.status == ExportStatus.Exporting,
            onExport = { exportLauncher.launch(exportFilename) },
        )
        when (exportState.status) {
            ExportStatus.Done -> ResetLifeMessage(
                text = stringResource(R.string.data_export_success),
                tone = ResetLifeMessageTone.Success,
            )
            ExportStatus.Error -> ResetLifeMessage(
                text = stringResource(R.string.data_export_error),
                tone = ResetLifeMessageTone.Error,
            )
            else -> Unit
        }
        if (reminderScheduled) {
            ResetLifeMessage(
                text = stringResource(R.string.reminder_scheduled),
                tone = ResetLifeMessageTone.Success,
            )
        }
    }
}

@Composable
private fun DataCard(
    title: String,
    description: String,
    buttonLabel: String,
    exporting: Boolean,
    onExport: () -> Unit,
) {
    ResetLifeSurface(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(ResetLifeSpacing.md),
            verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.md),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ResetLifeSpacing.md),
            ) {
                Icon(
                    imageVector = Icons.Filled.SaveAlt,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.xs),
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Button(
                onClick = onExport,
                enabled = !exporting,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(buttonLabel)
            }
        }
    }
}

@Composable
private fun AccessCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    ResetLifeSurface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(ResetLifeSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ResetLifeSpacing.md),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.xs),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
