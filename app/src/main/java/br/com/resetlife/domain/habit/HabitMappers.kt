package br.com.resetlife.domain.habit

import br.com.resetlife.data.local.habit.HabitEntity
import br.com.resetlife.data.local.habit.HabitLogEntity

fun HabitEntity.toDomain(): Habit = Habit(
    id = id,
    name = name,
    frequency = if (frequency == "WEEKDAYS") HabitFrequency.WEEKDAYS else HabitFrequency.DAILY,
    goalType = if (goalType == "QUANTITY") HabitGoalType.QUANTITY else HabitGoalType.BINARY,
    targetValue = targetValue,
    unit = unit,
    active = active,
    paused = paused,
    createdAt = createdAt,
    colorHex = colorHex,
)

fun Habit.toEntity(): HabitEntity = HabitEntity(
    id = id,
    name = name,
    frequency = if (frequency == HabitFrequency.WEEKDAYS) "WEEKDAYS" else "DAILY",
    goalType = if (goalType == HabitGoalType.QUANTITY) "QUANTITY" else "BINARY",
    targetValue = targetValue,
    unit = unit,
    active = active,
    paused = paused,
    createdAt = createdAt,
    colorHex = colorHex,
)

fun HabitLogEntity.toDomain(): HabitLog = HabitLog(
    id = id,
    habitId = habitId,
    date = date,
    value = value,
    done = done,
)

fun HabitLog.toEntity(): HabitLogEntity = HabitLogEntity(
    id = id,
    habitId = habitId,
    date = date,
    value = value,
    done = done,
)
