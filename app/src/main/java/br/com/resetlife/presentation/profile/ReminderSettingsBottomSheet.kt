package br.com.resetlife.presentation.profile

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.resetlife.R
import br.com.resetlife.data.reminder.ReminderScheduler
import br.com.resetlife.presentation.theme.ResetLifeSpacing
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ReminderSettingsBottomSheet(
    enabled: Boolean,
    hour: Int,
    minute: Int,
    onEnabledChange: (Boolean) -> Unit,
    onTimeChange: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var localEnabled by remember { mutableStateOf(enabled) }
    var timeText by remember { mutableStateOf(String.format("%02d:%02d", hour, minute)) }
    var showPermissionHint by remember { mutableStateOf(false) }

    val postNotificationPermission = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS,
    ) { granted ->
        if (granted) {
            localEnabled = true
            onEnabledChange(true)
        } else {
            localEnabled = false
            showPermissionHint = true
        }
    }

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
            verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.md),
        ) {
            Text(
                text = stringResource(R.string.reminder_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(R.string.reminder_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.xs),
                ) {
                    Text(
                        text = stringResource(R.string.reminder_enabled),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                Switch(
                    checked = localEnabled,
                    onCheckedChange = { wanted ->
                        if (wanted) {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                                postNotificationPermission.status == PermissionStatus.Granted
                            ) {
                                localEnabled = true
                                onEnabledChange(true)
                            } else {
                                postNotificationPermission.launchPermissionRequest()
                            }
                        } else {
                            localEnabled = false
                            onEnabledChange(false)
                        }
                    },
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.xs),
            ) {
                Text(
                    text = stringResource(R.string.reminder_time),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                OutlinedTextField(
                    value = timeText,
                    onValueChange = { new ->
                        timeText = new
                        ReminderScheduler.parseReminderTime(new)?.let { (h, m) ->
                            onTimeChange(h, m)
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("HH:MM") },
                    isError = ReminderScheduler.parseReminderTime(timeText) == null,
                )
            }

            if (showPermissionHint) {
                Text(
                    text = stringResource(R.string.reminder_permission_rationale),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}
