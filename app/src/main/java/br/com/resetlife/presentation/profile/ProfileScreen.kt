package br.com.resetlife.presentation.profile

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
fun ProfileScreen(
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
            title = stringResource(R.string.profile_title),
            supportingText = stringResource(R.string.profile_subtitle),
        )
        AccessCard(
            title = stringResource(R.string.weekly_review_nav),
            description = stringResource(R.string.profile_review_hint),
            symbol = "☷",
            onClick = { onNavigate(ResetLifeDestination.WeeklyReview) },
        )
        AccessCard(
            title = stringResource(R.string.profile_settings),
            description = stringResource(R.string.profile_settings_hint),
            symbol = "⚙",
            onClick = { /* Configurações ainda não implementadas */ },
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
