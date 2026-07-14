package br.com.resetlife.data.reminder

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ReminderSchedulerTest {

    @Test
    fun `parse valid midnight`() {
        assertEquals(0 to 0, ReminderScheduler.parseReminderTime("00:00"))
    }

    @Test
    fun `parse valid time trims spaces`() {
        assertEquals(20 to 30, ReminderScheduler.parseReminderTime(" 20:30 "))
    }

    @Test
    fun `parse valid end of day`() {
        assertEquals(23 to 59, ReminderScheduler.parseReminderTime("23:59"))
    }

    @Test
    fun `parse single digit hour and minute`() {
        assertEquals(9 to 5, ReminderScheduler.parseReminderTime("9:5"))
    }

    @Test
    fun `invalid when empty`() {
        assertNull(ReminderScheduler.parseReminderTime(""))
    }

    @Test
    fun `invalid when missing separator`() {
        assertNull(ReminderScheduler.parseReminderTime("2030"))
    }

    @Test
    fun `invalid when too many parts`() {
        assertNull(ReminderScheduler.parseReminderTime("20:30:00"))
    }

    @Test
    fun `invalid when non numeric`() {
        assertNull(ReminderScheduler.parseReminderTime("ab:cd"))
    }

    @Test
    fun `invalid when hour out of range`() {
        assertNull(ReminderScheduler.parseReminderTime("24:00"))
    }

    @Test
    fun `invalid when minute out of range`() {
        assertNull(ReminderScheduler.parseReminderTime("20:60"))
    }

    @Test
    fun `invalid when negative`() {
        assertNull(ReminderScheduler.parseReminderTime("-1:0"))
    }
}
