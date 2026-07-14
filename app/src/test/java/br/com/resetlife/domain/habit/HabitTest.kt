package br.com.resetlife.domain.habit

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HabitTest {
    private val today = "2026-07-14"
    private val saturday = "2026-07-18" // sábado

    @Test
    fun `create rejects empty name`() {
        val result = Habit.create(id = "1", name = "   ", createdAt = today)
        assertTrue(result is HabitCreationResult.EmptyName)
    }

    @Test
    fun `create daily habit is loggable every day`() {
        val result = Habit.create(id = "1", name = "Beber água", createdAt = today)
        assertTrue(result is HabitCreationResult.Created)
        val habit = (result as HabitCreationResult.Created).habit
        assertTrue(habit.isLoggableOn(today))
        assertTrue(habit.isLoggableOn(saturday))
    }

    @Test
    fun `weekdays habit not loggable on weekend`() {
        val result = Habit.create(
            id = "1",
            name = "Estudar",
            frequency = HabitFrequency.WEEKDAYS,
            createdAt = today,
        )
        val habit = (result as HabitCreationResult.Created).habit
        assertTrue(habit.isLoggableOn(today)) // terça
        assertFalse(habit.isLoggableOn(saturday))
    }

    @Test
    fun `paused habit not loggable`() {
        val result = Habit.create(id = "1", name = "Correr", createdAt = today)
        val habit = (result as HabitCreationResult.Created).habit.copy(paused = true)
        assertFalse(habit.isLoggableOn(today))
    }

    @Test
    fun `quantity habit without target defaults to 1`() {
        val result = Habit.create(
            id = "1",
            name = "Passos",
            goalType = HabitGoalType.QUANTITY,
            unit = "passos",
            createdAt = today,
        )
        val habit = (result as HabitCreationResult.Created).habit
        assertEquals(1, habit.targetValue)
        assertEquals("passos", habit.unit)
    }

    @Test
    fun `quantity habit with zero target is invalid`() {
        val result = Habit.create(
            id = "1",
            name = "Passos",
            goalType = HabitGoalType.QUANTITY,
            targetValue = 0,
            createdAt = today,
        )
        assertTrue(result is HabitCreationResult.InvalidTarget)
    }
}
