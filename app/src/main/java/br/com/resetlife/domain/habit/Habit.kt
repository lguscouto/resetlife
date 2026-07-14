package br.com.resetlife.domain.habit

enum class HabitFrequency {
    DAILY,
    WEEKDAYS, // seg-sex
}

enum class HabitGoalType {
    BINARY, // feito/não feito
    QUANTITY, // valor com meta
}

data class Habit(
    val id: String,
    val name: String,
    val frequency: HabitFrequency = HabitFrequency.DAILY,
    val goalType: HabitGoalType = HabitGoalType.BINARY,
    val targetValue: Int? = null,
    val unit: String? = null,
    val active: Boolean = true,
    val paused: Boolean = false,
    val createdAt: String,
    val colorHex: String? = null, // cor opcional (#RRGGBB); null = cor padrão do tema
) {
    fun isLoggableOn(date: String): Boolean {
        if (!active || paused) return false
        if (frequency == HabitFrequency.DAILY) return true
        // WEEKDAYS: seg(1) a sex(5) em LocalDate
        val dayOfWeek = java.time.LocalDate.parse(date).dayOfWeek.value
        return dayOfWeek in 1..5
    }

    companion object {
        fun create(
            id: String,
            name: String,
            frequency: HabitFrequency = HabitFrequency.DAILY,
            goalType: HabitGoalType = HabitGoalType.BINARY,
            targetValue: Int? = null,
            unit: String? = null,
            createdAt: String,
            colorHex: String? = null,
        ): HabitCreationResult {
            val normalized = name.trim()
            if (normalized.isEmpty()) return HabitCreationResult.EmptyName
            if (goalType == HabitGoalType.QUANTITY) {
                val target = targetValue ?: 1
                if (target <= 0) return HabitCreationResult.InvalidTarget
            }
            return HabitCreationResult.Created(
                Habit(
                    id = id,
                    name = normalized,
                    frequency = frequency,
                    goalType = goalType,
                    targetValue = if (goalType == HabitGoalType.QUANTITY) (targetValue ?: 1) else null,
                    unit = if (goalType == HabitGoalType.QUANTITY) unit?.trim()?.takeIf { it.isNotEmpty() } else null,
                    createdAt = createdAt,
                    colorHex = colorHex?.trim()?.takeIf { it.isNotEmpty() },
                ),
            )
        }
    }
}

sealed interface HabitCreationResult {
    data class Created(val habit: Habit) : HabitCreationResult
    data object EmptyName : HabitCreationResult
    data object InvalidTarget : HabitCreationResult
}
