package br.com.resetlife.domain.habit

import br.com.resetlife.data.local.habit.HabitEntity
import br.com.resetlife.domain.habit.toDomain
import br.com.resetlife.domain.habit.toEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class HabitMappersColorTest {

    @Test
    fun `colorHex round trips through mappers`() {
        val entity = HabitEntity(
            id = "h1",
            name = "Beber água",
            frequency = "DAILY",
            goalType = "BINARY",
            targetValue = null,
            unit = null,
            active = true,
            paused = false,
            createdAt = "2026-07-14",
            colorHex = "#245A8D",
        )

        val domain = entity.toDomain()
        assertEquals("#245A8D", domain.colorHex)

        val back = domain.toEntity()
        assertEquals("#245A8D", back.colorHex)
    }

    @Test
    fun `null colorHex round trips through mappers`() {
        val entity = HabitEntity(
            id = "h2",
            name = "Correr",
            frequency = "WEEKDAYS",
            goalType = "QUANTITY",
            targetValue = 5,
            unit = "km",
            active = true,
            paused = false,
            createdAt = "2026-07-14",
            colorHex = null,
        )

        val domain = entity.toDomain()
        assertNull(domain.colorHex)

        val back = domain.toEntity()
        assertNull(back.colorHex)
    }
}
