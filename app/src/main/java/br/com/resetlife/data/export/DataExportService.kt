package br.com.resetlife.data.export

import br.com.resetlife.ResetLifeApplication
import br.com.resetlife.domain.environment.CustomList
import br.com.resetlife.domain.environment.CustomListItem
import br.com.resetlife.domain.environment.EnvironmentSpace
import br.com.resetlife.domain.environment.EnvironmentTask
import br.com.resetlife.domain.habit.Habit
import br.com.resetlife.domain.habit.HabitLog
import br.com.resetlife.domain.onboarding.UserProfile
import br.com.resetlife.domain.organize.Project
import br.com.resetlife.domain.organize.Task
import br.com.resetlife.domain.today.PriorityItem
import br.com.resetlife.domain.weeklyreview.WeeklyReview
import br.com.resetlife.domain.wellbeing.WellbeingCheckIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

/**
 * Fonte de dados para exportação. Implementada pelos repositórios reais (via
 * [ResetLifeApplication]) e por fakes em testes unitários. Mantém a montagem do
 * JSON livre de dependências do Android.
 */
interface DataExportSource {
    fun observePriorities(): Flow<List<PriorityItem>>
    fun observeHabits(): Flow<List<Habit>>
    fun observeHabitLogs(): Flow<List<HabitLog>>
    fun observeProjects(): Flow<List<Project>>
    fun observeTasks(): Flow<List<Task>>
    fun observeCheckIns(): Flow<List<WellbeingCheckIn>>
    fun observeWeeklyReviews(): Flow<List<WeeklyReview>>
    fun observeSpaces(): Flow<List<EnvironmentSpace>>
    fun observeEnvironmentTasks(): Flow<List<EnvironmentTask>>
    fun observeCustomLists(): Flow<List<CustomList>>
    fun observeCustomListItems(): Flow<List<CustomListItem>>
    fun observeUserProfile(): Flow<UserProfile?>
}

/**
 * Monta o backup completo do usuário em um [JSONObject] (formato aberto, offline-first).
 *
 * O serviço recebe as fontes de dados por injeção, então a lógica de montagem pode
 * ser testada sem emulador/Android usando fakes.
 */
class DataExportService(
    private val source: DataExportSource,
) {
    constructor(application: ResetLifeApplication) : this(
        object : DataExportSource {
            override fun observePriorities(): Flow<List<PriorityItem>> =
                application.priorityStore.observeToday()

            override fun observeHabits(): Flow<List<Habit>> =
                application.habitStore.observeHabits()

            override fun observeHabitLogs(): Flow<List<HabitLog>> =
                application.habitStore.observeAllLogs()

            override fun observeProjects(): Flow<List<Project>> =
                application.organizeStore.observeProjects()

            override fun observeTasks(): Flow<List<Task>> =
                application.organizeStore.observeTasks()

            override fun observeCheckIns(): Flow<List<WellbeingCheckIn>> =
                application.wellbeingStore.observeAll()

            override fun observeWeeklyReviews(): Flow<List<WeeklyReview>> =
                application.weeklyReviewStore.observeReviews()

            override fun observeSpaces(): Flow<List<EnvironmentSpace>> =
                application.environmentStore.observeSpaces()

            override fun observeEnvironmentTasks(): Flow<List<EnvironmentTask>> =
                kotlinx.coroutines.flow.flow {
                    val tasks = mutableListOf<EnvironmentTask>()
                    application.environmentStore.observeSpaces().first().forEach { space ->
                        tasks += application.environmentStore.observeTasksBySpace(space.id).first()
                    }
                    tasks += application.environmentStore.observeDiscardList().first()
                    emit(tasks)
                }

            override fun observeCustomLists(): Flow<List<CustomList>> =
                application.environmentStore.observeCustomLists()

            override fun observeCustomListItems(): Flow<List<CustomListItem>> =
                application.environmentStore.observeAllCustomListItems()

            override fun observeUserProfile(): Flow<UserProfile?> =
                application.userProfileStore.observe()
        },
    )

    suspend fun exportAll(): JSONObject = withContext(Dispatchers.IO) {
        val priorities = source.observePriorities().first()
        val habits = source.observeHabits().first()
        val habitLogs = source.observeHabitLogs().first()
        val projects = source.observeProjects().first()
        val tasks = source.observeTasks().first()
        val checkIns = source.observeCheckIns().first()
        val weeklyReviews = source.observeWeeklyReviews().first()
        val spaces = source.observeSpaces().first()
        val environmentTasks = source.observeEnvironmentTasks().first()
        val customLists = source.observeCustomLists().first()
        val customListItems = source.observeCustomListItems().first()
        val userProfile = source.observeUserProfile().first()

        val root = JSONObject()
        root.put("version", 1)
        root.put("app", "ResetLife")
        root.put("priorities", JSONArray(priorities.map { it.toJson() }))
        root.put("habits", JSONArray(habits.map { it.toJson() }))
        root.put("habitLogs", JSONArray(habitLogs.map { it.toJson() }))
        root.put("projects", JSONArray(projects.map { it.toJson() }))
        root.put("tasks", JSONArray(tasks.map { it.toJson() }))
        root.put("checkIns", JSONArray(checkIns.map { it.toJson() }))
        root.put("weeklyReviews", JSONArray(weeklyReviews.map { it.toJson() }))
        root.put("environments", JSONObject().apply {
            put("spaces", JSONArray(spaces.map { it.toJson() }))
            put("tasks", JSONArray(environmentTasks.map { it.toJson() }))
            put("customLists", JSONArray(customLists.map { it.toJson() }))
            put("customListItems", JSONArray(customListItems.map { it.toJson() }))
        })
        root.put("userProfile", userProfile?.toJson() ?: JSONObject.NULL)
        root
    }
}

private fun PriorityItem.toJson() = JSONObject().apply {
    put("id", id)
    put("title", title)
    put("isCompleted", isCompleted)
}

private fun Habit.toJson() = JSONObject().apply {
    put("id", id)
    put("name", name)
    put("frequency", frequency.name)
    put("goalType", goalType.name)
    put("targetValue", targetValue)
    put("unit", unit)
    put("active", active)
    put("paused", paused)
    put("createdAt", createdAt)
    put("colorHex", colorHex)
    put("type", type.name)
}

private fun HabitLog.toJson() = JSONObject().apply {
    put("id", id)
    put("habitId", habitId)
    put("date", date)
    put("value", value)
    put("done", done)
}

private fun Project.toJson() = JSONObject().apply {
    put("id", id)
    put("title", title)
    put("goal", goal)
}

private fun Task.toJson() = JSONObject().apply {
    put("id", id)
    put("title", title)
    put("note", note)
    put("dueDate", dueDate)
    put("estimatedMinutes", estimatedMinutes)
    put("projectId", projectId)
    put("status", status.name)
}

private fun WellbeingCheckIn.toJson() = JSONObject().apply {
    put("date", date)
    put("mood", mood)
    put("energy", energy)
    put("stress", stress)
    put("sleepPerceived", sleepPerceived)
    put("note", note)
}

private fun WeeklyReview.toJson() = JSONObject().apply {
    put("id", id)
    put("periodStart", periodStart)
    put("periodEnd", periodEnd)
    put("completed", completed)
    put("pending", pending)
    put("difficulty", difficulty)
    put("nextWeekPriorities", nextWeekPriorities)
    put("habitToAdjust", habitToAdjust)
    put("createdAt", createdAt)
}

private fun EnvironmentSpace.toJson() = JSONObject().apply {
    put("id", id)
    put("name", name)
    put("lastOrganizedAt", lastOrganizedAt)
}

private fun EnvironmentTask.toJson() = JSONObject().apply {
    put("id", id)
    put("spaceId", spaceId)
    put("title", title)
    put("estimatedMinutes", estimatedMinutes)
    put("done", done)
    put("doneAt", doneAt)
    put("discardList", discardList)
    put("customListId", customListId)
}

private fun CustomList.toJson() = JSONObject().apply {
    put("id", id)
    put("name", name)
}

private fun CustomListItem.toJson() = JSONObject().apply {
    put("id", id)
    put("listId", listId)
    put("title", title)
    put("done", done)
}

private fun UserProfile.toJson() = JSONObject().apply {
    put("onboardingCompleted", onboardingCompleted)
    put("resetPlanStart", resetPlanStart)
    put("resetPlanDurationDays", resetPlanDurationDays)
    put("lifeAreas", JSONArray(lifeAreas.map { it.name }))
    put("dailyAvailabilityMinutes", dailyAvailabilityMinutes)
}
