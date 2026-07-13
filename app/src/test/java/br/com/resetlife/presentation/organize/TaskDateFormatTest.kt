package br.com.resetlife.presentation.organize

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TaskDateFormatTest {
    @Test
    fun `normalizes brazilian date to storage iso`() {
        assertEquals("2026-07-15", TaskDateFormat.normalizeInput("15/07/2026"))
    }

    @Test
    fun `rejects impossible brazilian date`() {
        assertNull(TaskDateFormat.normalizeInput("30/02/2026"))
    }

    @Test
    fun `rejects iso date format`() {
        assertNull(TaskDateFormat.normalizeInput("2026-07-15"))
    }

    @Test
    fun `normalizes numeric keyboard value`() {
        assertEquals("2026-07-15", TaskDateFormat.normalizeInput("15072026"))
    }

    @Test
    fun `formats stored iso date for brazilian display`() {
        assertEquals("15/07/2026", TaskDateFormat.formatStored("2026-07-15"))
    }

    @Test
    fun `formats numeric value for brazilian display`() {
        assertEquals("15/07/2026", TaskDateFormat.formatDigitsForDisplay("15072026"))
        assertEquals("15/07", TaskDateFormat.formatDigitsForDisplay("15/07/"))
    }

    @Test
    fun `returns null for empty date`() {
        assertNull(TaskDateFormat.normalizeInput("   "))
    }
}
