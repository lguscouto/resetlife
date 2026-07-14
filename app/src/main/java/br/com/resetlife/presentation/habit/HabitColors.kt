package br.com.resetlife.presentation.habit

import androidx.compose.ui.graphics.Color

/**
 * Cor predefinida para um hábito. [hex] no formato #RRGGBB (ou #AARRGGBB).
 */
data class HabitColor(
    val name: String,
    val hex: String,
)

/**
 * Paleta de 8 cores para seleção no diálogo de novo hábito (nomes em PT-BR).
 * A cor é opcional: um hábito sem cor usa o padrão do tema.
 */
val HABIT_COLOR_PALETTE: List<HabitColor> = listOf(
    HabitColor("Verde", "#1F6B5B"),
    HabitColor("Azul", "#245A8D"),
    HabitColor("Roxo", "#6A4CA5"),
    HabitColor("Âmbar", "#B26A00"),
    HabitColor("Vermelho", "#C0392B"),
    HabitColor("Rosa", "#C2185B"),
    HabitColor("Ciano", "#0097A7"),
    HabitColor("Cinza", "#607D8B"),
)

/**
 * Converte uma string de cor (#RRGGBB ou #AARRGGBB) em [Color].
 * Implementação pura Kotlin (sem android.graphics) para funcionar também em testes
 * unitários de JVM. Retorna null se a string for nula, vazia ou inválida.
 */
fun parseHabitColor(hex: String?): Color? {
    if (hex.isNullOrBlank()) return null
    val cleaned = hex.trim().removePrefix("#").uppercase()
    val argb = when (cleaned.length) {
        6 -> "FF$cleaned"
        8 -> cleaned
        else -> return null
    }
    val value = runCatching { argb.toLong(16) }.getOrNull() ?: return null
    if (value < 0 || value > 0xFFFFFFFF) return null
    val a = ((value shr 24) and 0xFF).toFloat() / 255f
    val r = ((value shr 16) and 0xFF).toFloat() / 255f
    val g = ((value shr 8) and 0xFF).toFloat() / 255f
    val b = (value and 0xFF).toFloat() / 255f
    return Color(alpha = a, red = r, green = g, blue = b)
}
