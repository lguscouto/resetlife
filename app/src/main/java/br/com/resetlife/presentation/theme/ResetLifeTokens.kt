package br.com.resetlife.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

object ResetLifeSpacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
    val screenHorizontal = 20.dp
}

object ResetLifeShapes {
    val card = RoundedCornerShape(20.dp)
    val field = RoundedCornerShape(14.dp)
    val control = RoundedCornerShape(12.dp)
    val pill = RoundedCornerShape(50)
}

@Immutable
data class ResetLifeSemanticColors(
    val focus: androidx.compose.ui.graphics.Color,
    val success: androidx.compose.ui.graphics.Color,
    val warning: androidx.compose.ui.graphics.Color,
    val onWarning: androidx.compose.ui.graphics.Color,
    val surfaceMuted: androidx.compose.ui.graphics.Color,
)
