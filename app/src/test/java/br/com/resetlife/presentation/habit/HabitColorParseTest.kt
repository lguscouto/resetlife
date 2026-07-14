package br.com.resetlife.presentation.habit

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HabitColorParseTest {

    @Test
    fun `palette has 8 colors`() {
        assertEquals(8, HABIT_COLOR_PALETTE.size)
    }

    @Test
    fun `palette names are pt-br and unique`() {
        val names = HABIT_COLOR_PALETTE.map { it.name }
        assertTrue(names.contains("Verde"))
        assertTrue(names.contains("Azul"))
        assertTrue(names.contains("Roxo"))
        assertTrue(names.contains("Âmbar"))
        assertTrue(names.contains("Vermelho"))
        assertTrue(names.contains("Rosa"))
        assertTrue(names.contains("Ciano"))
        assertTrue(names.contains("Cinza"))
        assertEquals(names.toSet().size, names.size)
    }

    @Test
    fun `palette hex values are valid and parseable`() {
        HABIT_COLOR_PALETTE.forEach { color ->
            // Garante que cada hex é um #RRGGBB válido.
            assertTrue(
                "Hex inválido: ${color.hex}",
                Regex("^#[0-9A-Fa-f]{6}$").matches(color.hex),
            )
            val parsed = parseHabitColor(color.hex)
            assertEquals("Falha ao parsear ${color.hex}", false, parsed == null)
        }
    }

    @Test
    fun `parseHabitColor handles valid formats`() {
        // #RRGGBB parseia para cor não-nula e é equivalente entre maiúsculas/com/sem '#'
        val lower = parseHabitColor("#1f6b5b")
        val upperNoHash = parseHabitColor("1F6B5B")
        val withHash = parseHabitColor("#1F6B5B")
        assertTrue(lower != null)
        assertEquals(lower, upperNoHash)
        assertEquals(lower, withHash)
        // #AARRGGBB também parseia
        assertTrue(parseHabitColor("#80ABCDEF") != null)
    }

    @Test
    fun `parseHabitColor returns null for invalid input`() {
        assertNull(parseHabitColor(null))
        assertNull(parseHabitColor(""))
        assertNull(parseHabitColor("   "))
        assertNull(parseHabitColor("#ZZZ"))
        assertNull(parseHabitColor("#12345")) // 5 dígitos
        assertNull(parseHabitColor("#1234567")) // 7 dígitos
    }
}
