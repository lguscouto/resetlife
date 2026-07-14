package br.com.resetlife.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import br.com.resetlife.presentation.theme.LocalResetLifeColors
import br.com.resetlife.presentation.theme.ResetLifeShapes
import br.com.resetlife.presentation.theme.ResetLifeSpacing

@Composable
fun RewardMessage(
    text: String,
    modifier: Modifier = Modifier,
) {
    val successColor = LocalResetLifeColors.current.success
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                liveRegion = LiveRegionMode.Polite
                contentDescription = text
            },
        shape = ResetLifeShapes.field,
        color = successColor.copy(alpha = 0.16f),
    ) {
        Row(
            modifier = Modifier.padding(ResetLifeSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
        ) {
            Icon(
                imageVector = Icons.Filled.Celebration,
                contentDescription = null,
                tint = successColor,
                modifier = Modifier.size(28.dp),
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.xs),
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
