package br.com.resetlife.data.habit

import br.com.resetlife.data.local.habit.HabitDao
import br.com.resetlife.domain.habit.Habit
import br.com.resetlife.domain.habit.HabitCreationResult
import br.com.resetlife.domain.habit.HabitGoalType
import br.com.resetlife.domain.habit.HabitLog
import br.com.resetlife.domain.habit.HabitFrequency
import br.com.resetlife.domain.habit.HabitType
import br.com.resetlife.domain.habit.toDomain
import br.com.resetlife.domain.habit.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.UUID

open class HabitRepository(private val dao: HabitDao) {
    fun observeHabits(): Flow<List<Habit>> = dao.observeAll().map { list ->
        list.map { it.toDomain() }
    }

    suspend fun add(
        name: String,
        frequency: HabitFrequency,
        goalType: HabitGoalType,
        targetValue: Int?,
        unit: String?,
        colorHex: String? = null,
        type: HabitType = HabitType.HABIT,
    ): HabitCreationResult {
        val result = Habit.create(
            id = UUID.randomUUID().toString(),
            name = name,
            frequency = frequency,
            goalType = goalType,
            targetValue = targetValue,
            unit = unit,
            createdAt = LocalDate.now().toString(),
            colorHex = colorHex,
            type = type,
        )
        if (result is HabitCreationResult.Created) {
            dao.insert(result.habit.toEntity())
        }
        return result
    }

    suspend fun pause(id: String) {
        val entity = dao.get(id) ?: return
        dao.update(entity.copy(paused = true))
    }

    suspend fun resume(id: String) {
        val entity = dao.get(id) ?: return
        dao.update(entity.copy(paused = false))
    }

    suspend fun archive(id: String) {
        val entity = dao.get(id) ?: return
        dao.update(entity.copy(active = false))
    }

    open suspend fun getHabit(id: String): Habit? = dao.get(id)?.toDomain()

    open fun observeLogs(habitId: String): Flow<List<HabitLog>> =
        dao.observeLogsByHabit(habitId).map { list -> list.map { it.toDomain() } }

    fun observeAllLogsForDate(date: String): Flow<List<HabitLog>> =
        dao.observeLogsByDate(date).map { list -> list.map { it.toDomain() } }

    fun observeAllLogs(): Flow<List<HabitLog>> =
        dao.observeAllLogs().map { list -> list.map { it.toDomain() } }

    suspend fun getLog(habitId: String, date: String): HabitLog? =
        dao.getLog(habitId, date)?.toDomain()

    suspend fun toggleToday(habit: Habit) {
        val date = LocalDate.now().toString()
        val existing = dao.getLog(habit.id, date)
        if (existing != null) {
            dao.deleteLog(habit.id, date)
        } else {
            val done = when (habit.goalType) {
                HabitGoalType.BINARY -> true
                HabitGoalType.QUANTITY -> (habit.targetValue ?: 1) <= 1
            }
            val value = if (habit.goalType == HabitGoalType.BINARY) 1 else (habit.targetValue ?: 1)
            dao.upsertLog(
                HabitLog(
                    id = UUID.randomUUID().toString(),
                    habitId = habit.id,
                    date = date,
                    value = value,
                    done = done,
                ).toEntity(),
            )
        }
    }

    suspend fun setQuantityToday(habit: Habit, value: Int) {
        val date = LocalDate.now().toString()
        val done = value >= (habit.targetValue ?: 1)
        dao.upsertLog(
            HabitLog(
                id = UUID.randomUUID().toString(),
                habitId = habit.id,
                date = date,
                value = value,
                done = done,
            ).toEntity(),
        )
    }
}
