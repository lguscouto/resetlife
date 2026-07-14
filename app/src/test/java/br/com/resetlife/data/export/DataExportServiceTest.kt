package br.com.resetlife.data.export

import br.com.resetlife.MainDispatcherRule
import br.com.resetlife.domain.environment.CustomList
import br.com.resetlife.domain.environment.CustomListItem
import br.com.resetlife.domain.environment.EnvironmentSpace
import br.com.resetlife.domain.environment.EnvironmentTask
import br.com.resetlife.domain.habit.Habit
import br.com.resetlife.domain.habit.HabitGoalType
import br.com.resetlife.domain.habit.HabitLog
import br.com.resetlife.domain.onboarding.LifeArea
import br.com.resetlife.domain.onboarding.UserProfile
import br.com.resetlife.domain.organize.Project
import br.com.resetlife.domain.organize.Task
import br.com.resetlife.domain.organize.TaskStatus
import br.com.resetlife.domain.today.PriorityItem
import br.com.resetlife.domain.weeklyreview.WeeklyReview
import br.com.resetlife.domain.wellbeing.WellbeingCheckIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DataExportServiceTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeSource(
        val priorities: List<PriorityItem> = emptyList(),
        val habits: List<Habit> = emptyList(),
        val habitLogs: List<HabitLog> = emptyList(),
        val projects: List<Project> = emptyList(),
        val tasks: List<Task> = emptyList(),
        val checkIns: List<WellbeingCheckIn> = emptyList(),
        val weeklyReviews: List<WeeklyReview> = emptyList(),
        val spaces: List<EnvironmentSpace> = emptyList(),
        val environmentTasks: List<EnvironmentTask> = emptyList(),
        val customLists: List<CustomList> = emptyList(),
        val customListItems: List<CustomListItem> = emptyList(),
        val userProfile: UserProfile? = null,
    ) : DataExportSource {
        override fun observePriorities(): Flow<List<PriorityItem>> = flowOf(priorities)
        override fun observeHabits(): Flow<List<Habit>> = flowOf(habits)
        override fun observeHabitLogs(): Flow<List<HabitLog>> = flowOf(habitLogs)
        override fun observeProjects(): Flow<List<Project>> = flowOf(projects)
        override fun observeTasks(): Flow<List<Task>> = flowOf(tasks)
        override fun observeCheckIns(): Flow<List<WellbeingCheckIn>> = flowOf(checkIns)
        override fun observeWeeklyReviews(): Flow<List<WeeklyReview>> = flowOf(weeklyReviews)
        override fun observeSpaces(): Flow<List<EnvironmentSpace>> = flowOf(spaces)
        override fun observeEnvironmentTasks(): Flow<List<EnvironmentTask>> = flowOf(environmentTasks)
        override fun observeCustomLists(): Flow<List<CustomList>> = flowOf(customLists)
        override fun observeCustomListItems(): Flow<List<CustomListItem>> = flowOf(customListItems)
        override fun observeUserProfile(): Flow<UserProfile?> = flowOf(userProfile)
    }

    @Test
    fun `exportAll returns JSONObject with all expected top-level keys`() = runTest {
        val source = FakeSource()
        val result = DataExportService(source).exportAll()

        val expectedKeys = setOf(
            "version", "app", "priorities", "habits", "habitLogs", "projects",
            "tasks", "checkIns", "weeklyReviews", "environments", "userProfile",
        )
        expectedKeys.forEach { key -> assertTrue("Falta chave: $key", result.has(key)) }
    }

    @Test
    fun `exportAll serializes priorities and habits correctly`() = runTest {
        val priorities = listOf(
            PriorityItem(id = "p1", title = "Estudar", isCompleted = true),
            PriorityItem(id = "p2", title = "Treinar", isCompleted = false),
        )
        val habits = listOf(
            Habit(
                id = "h1",
                name = "Beber água",
                goalType = HabitGoalType.BINARY,
                createdAt = "2026-07-14",
            ),
        )
        val result = DataExportService(FakeSource(priorities = priorities, habits = habits)).exportAll()

        val prioritiesJson = result.getJSONArray("priorities")
        assertEquals(2, prioritiesJson.length())
        assertEquals("p1", prioritiesJson.getJSONObject(0).getString("id"))
        assertEquals(true, prioritiesJson.getJSONObject(0).getBoolean("isCompleted"))
        assertEquals("p2", prioritiesJson.getJSONObject(1).getString("id"))
        assertEquals(false, prioritiesJson.getJSONObject(1).getBoolean("isCompleted"))

        val habitsJson = result.getJSONArray("habits")
        assertEquals(1, habitsJson.length())
        val habitJson = habitsJson.getJSONObject(0)
        assertEquals("h1", habitJson.getString("id"))
        assertEquals("Beber água", habitJson.getString("name"))
        assertEquals("BINARY", habitJson.getString("goalType"))
        assertEquals("2026-07-14", habitJson.getString("createdAt"))
    }

    @Test
    fun `exportAll serializes tasks, projects, checkins and weekly reviews`() = runTest {
        val projects = listOf(Project(id = "pr1", title = "Reset", goal = "Foco"))
        val tasks = listOf(
            Task(
                id = "t1",
                title = "Separar docs",
                note = "Por categoria",
                estimatedMinutes = 15,
                projectId = "pr1",
                status = TaskStatus.NEXT_ACTION,
            ),
        )
        val checkIns = listOf(
            WellbeingCheckIn(date = "2026-07-14", mood = 4, energy = 3, stress = 2, sleepPerceived = 4, note = "ok"),
        )
        val weeklyReviews = listOf(
            WeeklyReview(
                id = "w1",
                periodStart = "2026-07-07",
                periodEnd = "2026-07-13",
                completed = "X",
                pending = "Y",
                difficulty = "Z",
                nextWeekPriorities = "A",
                habitToAdjust = "B",
                createdAt = 123L,
            ),
        )

        val result = DataExportService(
            FakeSource(
                projects = projects,
                tasks = tasks,
                checkIns = checkIns,
                weeklyReviews = weeklyReviews,
            ),
        ).exportAll()

        assertEquals("Reset", result.getJSONArray("projects").getJSONObject(0).getString("title"))
        val taskJson = result.getJSONArray("tasks").getJSONObject(0)
        assertEquals("t1", taskJson.getString("id"))
        assertEquals("Separar docs", taskJson.getString("title"))
        assertEquals("NEXT_ACTION", taskJson.getString("status"))
        assertEquals(15, taskJson.getInt("estimatedMinutes"))
        assertEquals("pr1", taskJson.getString("projectId"))

        assertEquals(4, result.getJSONArray("checkIns").getJSONObject(0).getInt("mood"))
        assertEquals("2026-07-07", result.getJSONArray("weeklyReviews").getJSONObject(0).getString("periodStart"))
    }

    @Test
    fun `exportAll serializes environments (spaces, tasks, customLists, items) and user profile`() = runTest {
        val spaces = listOf(EnvironmentSpace(id = "s1", name = "Quarto", lastOrganizedAt = "2026-07-14"))
        val environmentTasks = listOf(
            EnvironmentTask(
                id = "e1",
                spaceId = "s1",
                title = "Varrer",
                estimatedMinutes = 5,
                discardList = false,
                customListId = null,
            ),
        )
        val customLists = listOf(CustomList(id = "c1", name = "Compras"))
        val customListItems = listOf(CustomListItem(id = "i1", listId = "c1", title = "Leite", done = false))
        val userProfile = UserProfile(
            onboardingCompleted = true,
            resetPlanDurationDays = 7,
            lifeAreas = listOf(LifeArea.Health, LifeArea.Tasks),
            dailyAvailabilityMinutes = 20,
        )

        val result = DataExportService(
            FakeSource(
                spaces = spaces,
                environmentTasks = environmentTasks,
                customLists = customLists,
                customListItems = customListItems,
                userProfile = userProfile,
            ),
        ).exportAll()

        val env = result.getJSONObject("environments")
        assertEquals("s1", env.getJSONArray("spaces").getJSONObject(0).getString("id"))
        assertEquals("Quarto", env.getJSONArray("spaces").getJSONObject(0).getString("name"))
        assertEquals("e1", env.getJSONArray("tasks").getJSONObject(0).getString("id"))
        assertEquals("c1", env.getJSONArray("customLists").getJSONObject(0).getString("id"))
        assertEquals("i1", env.getJSONArray("customListItems").getJSONObject(0).getString("id"))

        val profileJson = result.getJSONObject("userProfile")
        assertEquals(true, profileJson.getBoolean("onboardingCompleted"))
        assertEquals(7, profileJson.getInt("resetPlanDurationDays"))
        val areas = profileJson.getJSONArray("lifeAreas")
        assertEquals(2, areas.length())
        assertEquals("Health", areas.getString(0))
    }

    @Test
    fun `exportAll writes null userProfile when absent`() = runTest {
        val result = DataExportService(FakeSource(userProfile = null)).exportAll()
        assertEquals(JSONObject.NULL, result.get("userProfile"))
    }

    @Test
    fun `exportAll serializes habit logs`() = runTest {
        val logs = listOf(
            HabitLog(id = "l1", habitId = "h1", date = "2026-07-14", value = 1, done = true),
        )
        val result = DataExportService(FakeSource(habitLogs = logs)).exportAll()
        val logsJson = result.getJSONArray("habitLogs")
        assertEquals(1, logsJson.length())
        assertEquals("h1", logsJson.getJSONObject(0).getString("habitId"))
        assertEquals(true, logsJson.getJSONObject(0).getBoolean("done"))
    }
}
