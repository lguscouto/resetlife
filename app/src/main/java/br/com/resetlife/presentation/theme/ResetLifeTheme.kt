package br.com.resetlife.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val LightColors = lightColorScheme(
    primary = Color(0xFF1F6B5B),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC5EBDD),
    onPrimaryContainer = Color(0xFF07382D),
    secondary = Color(0xFF245A8D),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD2E6FF),
    onSecondaryContainer = Color(0xFF073250),
    tertiary = Color(0xFF6B4F24),
    onTertiary = Color.White,
    background = Color(0xFFF7F8F6),
    surface = Color(0xFFF7F8F6),
    surfaceVariant = Color(0xFFE1E7E2),
    onBackground = Color(0xFF202522),
    onSurface = Color(0xFF202522),
    onSurfaceVariant = Color(0xFF414943),
    outline = Color(0xFF717A73),
    error = Color(0xFFBA1A1A),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFA7D7C5),
    onPrimary = Color(0xFF123C33),
    primaryContainer = Color(0xFF245144),
    onPrimaryContainer = Color(0xFFC5EBDD),
    secondary = Color(0xFF9DCBFF),
    onSecondary = Color(0xFF003354),
    secondaryContainer = Color(0xFF084A74),
    onSecondaryContainer = Color(0xFFD2E6FF),
    tertiary = Color(0xFFE9C176),
    onTertiary = Color(0xFF3B2F16),
    background = Color(0xFF141816),
    surface = Color(0xFF141816),
    surfaceVariant = Color(0xFF414943),
    onBackground = Color(0xFFE0E5E1),
    onSurface = Color(0xFFE0E5E1),
    onSurfaceVariant = Color(0xFFC1CAC2),
    outline = Color(0xFF8B938C),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
)

private val ResetLifeTypography = Typography(
    headlineMedium = Typography().headlineMedium.copy(
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (-0.25).sp,
    ),
    titleLarge = Typography().titleLarge.copy(fontWeight = FontWeight.SemiBold),
    titleMedium = Typography().titleMedium.copy(fontWeight = FontWeight.SemiBold),
    bodyLarge = Typography().bodyLarge.copy(lineHeight = 24.sp),
    bodyMedium = Typography().bodyMedium.copy(lineHeight = 20.sp),
    labelLarge = Typography().labelLarge.copy(fontWeight = FontWeight.SemiBold),
)

val LocalResetLifeColors = staticCompositionLocalOf {
    ResetLifeSemanticColors(
        focus = Color(0xFF245A8D),
        success = Color(0xFF1F6B5B),
        warning = Color(0xFF8A5A00),
        onWarning = Color.White,
        surfaceMuted = Color(0xFFE1E7E2),
    )
}

@Composable
fun ResetLifeTheme(content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
    val semanticColors = if (dark) {
        ResetLifeSemanticColors(
            focus = Color(0xFF9DCBFF),
            success = Color(0xFFA7D7C5),
            warning = Color(0xFFE9C176),
            onWarning = Color(0xFF3B2F16),
            surfaceMuted = Color(0xFF2A312D),
        )
    } else {
        ResetLifeSemanticColors(
            focus = Color(0xFF245A8D),
            success = Color(0xFF1F6B5B),
            warning = Color(0xFF8A5A00),
            onWarning = Color.White,
            surfaceMuted = Color(0xFFE1E7E2),
        )
    }

    CompositionLocalProvider(LocalResetLifeColors provides semanticColors) {
        MaterialTheme(
            colorScheme = if (dark) DarkColors else LightColors,
            typography = ResetLifeTypography,
            shapes = androidx.compose.material3.Shapes(
                small = ResetLifeShapes.control,
                medium = ResetLifeShapes.field,
                large = ResetLifeShapes.card,
            ),
            content = content,
        )
    }
}
