package br.com.resetlife.domain.habit

import br.com.resetlife.data.local.habit.HabitEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class HabitTypeTest {

    @Test
    fun `entity AVOID maps to domain AVOID`() {
        val entity = HabitEntity(
            id = "1",
            name = "Fumar",
            frequency = "DAILY",
            goalType = "BINARY",
            targetValue = null,
            unit = null,
            active = true,
            paused = false,
            createdAt = "2026-07-14",
            colorHex = null,
            type = "AVOID",
        )
        assertEquals(HabitType.AVOID, entity.toDomain().type)
    }

    @Test
    fun `entity with default HABIT maps to domain HABIT`() {
        val entity = HabitEntity(
            id = "1",
            name = "Beber água",
            frequency = "DAILY",
            goalType = "BINARY",
            targetValue = null,
            unit = null,
            active = true,
            paused = false,
            createdAt = "2026-07-14",
        )
        assertEquals(HabitType.HABIT, entity.toDomain().type)
    }

    @Test
    fun `domain AVOID round-trips to entity AVOID`() {
        val habit = Habit(
            id = "1",
            name = "Fumar",
            frequency = HabitFrequency.DAILY,
            goalType = HabitGoalType.BINARY,
            createdAt = "2026-07-14",
            type = HabitType.AVOID,
        )
        assertEquals("AVOID", habit.toEntity().type)
    }

    @Test
    fun `domain HABIT round-trips to entity HABIT`() {
        val habit = Habit(
            id = "1",
            name = "Beber água",
            frequency = HabitFrequency.DAILY,
            goalType = HabitGoalType.BINARY,
            createdAt = "2026-07-14",
            type = HabitType.HABIT,
        )
        assertEquals("HABIT", habit.toEntity().type)
    }
}
