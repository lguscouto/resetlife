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
import androidx.compose.material.icons.filled.Palette
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import br.com.resetlife.R
import br.com.resetlife.presentation.components.ResetLifeSectionHeader
import br.com.resetlife.presentation.components.ResetLifeSurface
import br.com.resetlife.presentation.navigation.ResetLifeDestination
import br.com.resetlife.presentation.theme.ResetLifeSpacing
import br.com.resetlife.presentation.theme.ThemeManager
import br.com.resetlife.presentation.theme.ThemeMode

@Composable
fun ProfileScreen(
    themeManager: ThemeManager,
    onNavigate: (ResetLifeDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentThemeMode by themeManager.themeMode.collectAsStateWithLifecycle(ThemeMode.SYSTEM)
    var showThemeSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

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
