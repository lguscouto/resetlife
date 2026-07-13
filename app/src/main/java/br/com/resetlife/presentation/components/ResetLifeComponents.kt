package br.com.resetlife.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.resetlife.presentation.theme.LocalResetLifeColors
import br.com.resetlife.presentation.theme.ResetLifeShapes
import br.com.resetlife.presentation.theme.ResetLifeSpacing

@Composable
fun ResetLifeSurface(
    modifier: Modifier = Modifier,
    emphasized: Boolean = false,
    muted: Boolean = false,
    content: @Composable () -> Unit,
) {
    val containerColor = when {
        emphasized -> MaterialTheme.colorScheme.primaryContainer
        muted -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.colorScheme.surface
    }
    Surface(
        modifier = modifier.animateContentSize(),
        shape = ResetLifeShapes.card,
        color = containerColor,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        tonalElevation = if (emphasized) 2.dp else 0.dp,
        content = content,
    )
}

@Composable
fun ResetLifeSectionHeader(
    title: String,
    supportingText: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        if (supportingText != null) {
            Spacer(modifier = Modifier.padding(top = ResetLifeSpacing.xs))
            Text(
                supportingText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

enum class ResetLifeMessageTone { Info, Success, Warning, Error }

@Composable
fun ResetLifeMessage(
    text: String,
    tone: ResetLifeMessageTone,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    val colors = LocalResetLifeColors.current
    val (container, content) = when (tone) {
        ResetLifeMessageTone.Info -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        ResetLifeMessageTone.Success -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        ResetLifeMessageTone.Warning -> colors.warning to colors.onWarning
        ResetLifeMessageTone.Error -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
    }
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = ResetLifeShapes.field,
        color = container,
    ) {
        Column(
            modifier = Modifier.padding(ResetLifeSpacing.md),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(ResetLifeSpacing.xs),
        ) {
            AnimatedContent(targetState = text, label = "resetLifeMessageText") { message ->
                Text(message, style = MaterialTheme.typography.bodyMedium, color = content)
            }
            if (actionLabel != null && onAction != null) {
                TextButton(onClick = onAction) {
                    Text(actionLabel, color = content)
                }
            }
        }
    }
}

@Composable
fun ResetLifeLoading(
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(ResetLifeSpacing.lg),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(ResetLifeSpacing.sm),
    ) {
        CircularProgressIndicator(modifier = Modifier.size(28.dp), strokeWidth = 3.dp)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
