package br.com.resetlife.presentation.life

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.resetlife.R
import br.com.resetlife.presentation.components.ResetLifeSectionHeader
import br.com.resetlife.presentation.components.ResetLifeSurface
import br.com.resetlife.presentation.navigation.ResetLifeDestination
import br.com.resetlife.presentation.theme.ResetLifeSpacing

@Composable
fun LifeScreen(
    onNavigate: (ResetLifeDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = ResetLifeSpacing.screenHorizontal)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.lg),
    ) {
        ResetLifeSectionHeader(
            title = stringResource(R.string.life_title),
            supportingText = stringResource(R.string.life_subtitle),
        )
        AccessCard(
            title = stringResource(R.string.habits_nav),
            description = stringResource(R.string.life_habits_hint),
            symbol = "✓",
            onClick = { onNavigate(ResetLifeDestination.Habits) },
        )
        AccessCard(
            title = stringResource(R.string.environment_nav),
            description = stringResource(R.string.life_environment_hint),
            symbol = "⌂",
            onClick = { onNavigate(ResetLifeDestination.Environment) },
        )
        AccessCard(
            title = stringResource(R.string.wellbeing_nav),
            description = stringResource(R.string.life_wellbeing_hint),
            symbol = "❤",
            onClick = { onNavigate(ResetLifeDestination.Wellbeing) },
        )
    }
}

@Composable
private fun AccessCard(
    title: String,
    description: String,
    symbol: String,
    onClick: () -> Unit,
) {
    ResetLifeSurface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column(modifier = Modifier.padding(ResetLifeSpacing.md)) {
            Text(
                text = "$symbol  $title",
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
