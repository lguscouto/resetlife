package br.com.resetlife.presentation.habit

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import br.com.resetlife.R
import br.com.resetlife.presentation.theme.LocalResetLifeColors
import br.com.resetlife.presentation.theme.ResetLifeSpacing
import java.time.LocalDate

/**
 * Heatmap de streak: grade 7 colunas x 5 linhas (35 células) representando
 * os ~35 dias até hoje. Cada célula é pintada conforme o dia foi concluído.
 */
@Composable
fun StreakCalendar(
    doneDates: Set<String>,
    modifier: Modifier = Modifier,
) {
    val today = remember { LocalDate.now() }
    val days: List<LocalDate> = remember(today) {
        (0 until 35).map { today.minusDays((34 - it).toLong()) }
    }
    val colors = LocalResetLifeColors.current

    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(ResetLifeSpacing.xs),
        ) {
            repeat(5) { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(ResetLifeSpacing.xs),
                ) {
                    repeat(7) { col ->
                        val index = row * 7 + col
                        val day = days.getOrNull(index) ?: return@Row
                        val dateStr = day.toString()
                        val done = doneDates.contains(dateStr)
                        val cellColor = if (done) colors.success else MaterialTheme.colorScheme.surfaceVariant
                        val textColor = if (done) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        val description = if (done) {
                            stringResource(R.string.habit_calendar_done) + " " + dateStr
                        } else {
                            stringResource(R.string.habit_calendar_pending) + " " + dateStr
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(cellColor)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    shape = RoundedCornerShape(6.dp),
                                )
                                .semantics { contentDescription = description },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = day.dayOfMonth.toString(),
                                style = MaterialTheme.typography.labelSmall,
                                color = textColor,
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = ResetLifeSpacing.sm),
            horizontalArrangement = Arrangement.spacedBy(ResetLifeSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LegendDot(color = colors.success)
            Text(
                text = stringResource(R.string.habit_calendar_done),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            LegendDot(color = MaterialTheme.colorScheme.surfaceVariant)
            Text(
                text = stringResource(R.string.habit_calendar_pending),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun LegendDot(color: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .size(14.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(color)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(4.dp)),
    )
}
