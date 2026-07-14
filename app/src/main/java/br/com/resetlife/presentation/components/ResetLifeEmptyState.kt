package br.com.resetlife.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.resetlife.presentation.theme.ResetLifeShapes
import br.com.resetlife.presentation.theme.ResetLifeSpacing

/**
 * Estado vazio acionável, reutilizável em todas as telas.
 *
 * Centraliza um ícone, título e descrição com microcopy que orienta a próxima
 * ação, além de um botão de ação opcional quando há um próximo passo claro.
 *
 * @param title Título curto do estado vazio (ex: "Nenhum projeto ainda").
 * @param description Microcopy que explica o próximo passo (ex: "Crie seu
 *   primeiro projeto para começar.").
 * @param modifier Modificador opcional.
 * @param actionLabel Rótulo do botão de ação; se nulo, nenhum botão é exibido.
 * @param onAction Callback executado ao acionar o botão; se nulo, nenhum
 *   botão é exibido.
 */
@Composable
fun ResetLifeEmptyState(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    ResetLifeSurface(
        modifier = modifier.fillMaxWidth(),
        muted = true,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ResetLifeSpacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.sm),
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            if (actionLabel != null && onAction != null) {
                Button(
                    onClick = onAction,
                    modifier = Modifier.padding(top = ResetLifeSpacing.xs),
                ) {
                    Text(text = actionLabel)
                }
            }
        }
    }
}
