package br.com.resetlife.presentation.habit

import br.com.resetlife.MainDispatcherRule
import br.com.resetlife.data.habit.HabitRepository
import br.com.resetlife.data.local.habit.HabitDao
import br.com.resetlife.domain.habit.Habit
import br.com.resetlife.domain.habit.HabitFrequency
import br.com.resetlife.domain.habit.HabitGoalType
import br.com.resetlife.domain.habit.HabitLog
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class HabitDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun logOn(dayOffset: Long, done: Boolean = true, value: Int = 1): HabitLog {
        val date = LocalDate.now().minusDays(dayOffset).toString()
        return HabitLog(
            id = "log-$dayOffset",
            habitId = "habit-1",
            date = date,
            value = value,
            done = done,
        )
    }

    private fun habit(createdAtOffsetDays: Long): Habit {
        val createdAt = LocalDate.now().minusDays(createdAtOffsetDays).toString()
        return Habit(
            id = "habit-1",
            name = "Beber água",
            frequency = HabitFrequency.DAILY,
            goalType = HabitGoalType.BINARY,
            createdAt = createdAt,
        )
    }

    @Test
    fun `streak counts consecutive done days until today`() = runTest {
        val logs = listOf(
            logOn(0), // hoje
            logOn(1), // ontem
            logOn(3), // 3 dias atrás (com folga de 1 dia)
        )
        val viewModel = HabitDetailViewModel(
            habitId = "habit-1",
            repository = FakeHabitRepository(logs, habit(createdAtOffsetDays = 30)),
        )

        assertEquals(2, viewModel.uiState.value.streakCurrent)
    }

    @Test
    fun `streak counts until yesterday when today not done`() = runTest {
        val logs = listOf(
            logOn(1),
            logOn(2),
            logOn(3),
        )
        val viewModel = HabitDetailViewModel(
            habitId = "habit-1",
            repository = FakeHabitRepository(logs, habit(createdAtOffsetDays = 30)),
        )

        // hoje não feito: sequência conta até ontem (3 dias seguidos)
        assertEquals(3, viewModel.uiState.value.streakCurrent)
    }

    @Test
    fun `completion rate divides done days by total days since creation`() = runTest {
        // criado há 9 dias => 10 dias no total (inclusive)
        val logs = listOf(
            logOn(0),
            logOn(1),
            logOn(2),
        )
        val viewModel = HabitDetailViewModel(
            habitId = "habit-1",
            repository = FakeHabitRepository(logs, habit(createdAtOffsetDays = 9)),
        )

        // 3 dias feitos / 10 dias totais = 0.3
        assertEquals(0.3f, viewModel.uiState.value.completionRate, 0.0001f)
    }

    @Test
    fun `empty history yields zero streak and zero rate`() = runTest {
        val viewModel = HabitDetailViewModel(
            habitId = "habit-1",
            repository = FakeHabitRepository(emptyList(), habit(createdAtOffsetDays = 9)),
        )

        assertEquals(0, viewModel.uiState.value.streakCurrent)
        assertEquals(0f, viewModel.uiState.value.completionRate, 0.0001f)
    }
}

private class FakeHabitRepository(
    private val logs: List<HabitLog>,
    private val habit: Habit?,
) : HabitRepository(FakeHabitDao()) {

    override fun observeLogs(habitId: String): kotlinx.coroutines.flow.Flow<List<HabitLog>> =
        flowOf(logs)

    override suspend fun getHabit(id: String): Habit? = habit
}

private class FakeHabitDao : HabitDao {
    override fun observeAll() =
        flowOf(emptyList<br.com.resetlife.data.local.habit.HabitEntity>())

    override suspend fun get(id: String) = null

    override suspend fun insert(habit: br.com.resetlife.data.local.habit.HabitEntity) = Unit

    override suspend fun update(habit: br.com.resetlife.data.local.habit.HabitEntity) = Unit

    override fun observeLogsByHabit(habitId: String) =
        flowOf(emptyList<br.com.resetlife.data.local.habit.HabitLogEntity>())

    override fun observeLogsByDate(date: String) =
        flowOf(emptyList<br.com.resetlife.data.local.habit.HabitLogEntity>())

    override fun observeAllLogs() =
        flowOf(emptyList<br.com.resetlife.data.local.habit.HabitLogEntity>())

    override suspend fun getLog(habitId: String, date: String) = null

    override suspend fun getLogsByDate(date: String) =
        emptyList<br.com.resetlife.data.local.habit.HabitLogEntity>()

    override suspend fun upsertLog(log: br.com.resetlife.data.local.habit.HabitLogEntity) = Unit

    override suspend fun deleteLog(habitId: String, date: String) = Unit
}
