package br.com.resetlife.widget

import br.com.resetlife.data.today.PriorityStore
import br.com.resetlife.domain.habit.Habit
import br.com.resetlife.domain.habit.HabitFrequency
import br.com.resetlife.domain.habit.HabitGoalType
import br.com.resetlife.domain.today.PriorityItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FakePriorityStore : PriorityStore {
    var completedId: String? = null

    override fun observeToday(): Flow<List<PriorityItem>> = flowOf(emptyList())

    override suspend fun add(priority: PriorityItem) = Unit

    override suspend fun complete(id: String): Boolean {
        completedId = id
        return true
    }
}

private fun testHabit(id: String) = Habit(
    id = id,
    name = "Hábito $id",
    frequency = HabitFrequency.DAILY,
    goalType = HabitGoalType.BINARY,
    createdAt = "2026-07-14",
)

class WidgetActionTest {

    @Test
    fun completePriority_chamaCompleteDoStore() = runTest {
        val fake = FakePriorityStore()
        val result = WidgetActions.completePriority(fake, "p1")
        assertTrue(result)
        assertEquals("p1", fake.completedId)
    }

    @Test
    fun toggleHabit_quandoExiste_chamaToggleToday() = runTest {
        val toggled = mutableListOf<String>()
        val result = WidgetActions.toggleHabit(
            observeHabits = { flowOf(listOf(testHabit("h1"), testHabit("h2"))) },
            toggleToday = { toggled.add(it.id) },
            id = "h2",
        )
        assertTrue(result)
        assertEquals(listOf("h2"), toggled)
    }

    @Test
    fun toggleHabit_quandoNaoExiste_naoChamaToggleToday() = runTest {
        val toggled = mutableListOf<String>()
        val result = WidgetActions.toggleHabit(
            observeHabits = { flowOf(emptyList()) },
            toggleToday = { toggled.add(it.id) },
            id = "ausente",
        )
        assertFalse(result)
        assertTrue(toggled.isEmpty())
    }
}
