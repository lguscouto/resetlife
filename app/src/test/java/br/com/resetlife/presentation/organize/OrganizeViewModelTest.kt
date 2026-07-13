package br.com.resetlife.presentation.organize

import br.com.resetlife.MainDispatcherRule
import br.com.resetlife.data.today.PriorityStore
import br.com.resetlife.data.organize.OrganizeStore
import br.com.resetlife.domain.organize.Project
import br.com.resetlife.domain.organize.Task
import br.com.resetlife.domain.today.PriorityItem
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OrganizeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `creates a project and a task linked to the selected project`() = runTest {
        val store = OrganizeFakeStore()
        val viewModel = OrganizeViewModel(store, PriorityFakeStore())
        advanceUntilIdle()

        viewModel.onProjectTitleChanged("Casa")
        viewModel.onProjectGoalChanged("Organizar a casa")
        viewModel.addProject()
        advanceUntilIdle()
        val projectId = viewModel.uiState.value.projects.single().id
        viewModel.onProjectSelected(projectId)
        viewModel.onTaskTitleChanged("Separar documentos")
        viewModel.onTaskNoteChanged("Por categoria")
        viewModel.onTaskDueDateChanged("15/07/2026")
        viewModel.onTaskEstimatedMinutesChanged("30")
        viewModel.addTask()
        advanceUntilIdle()

        val task = viewModel.uiState.value.tasks.single()
        assertEquals(projectId, task.projectId)
        assertEquals("Por categoria", task.note)
        assertEquals("2026-07-15", task.dueDate)
        assertEquals(30, task.estimatedMinutes)
    }

    @Test
    fun `filters tasks by local title query`() = runTest {
        val store = OrganizeFakeStore(
            tasks = listOf(
                Task(id = "1", title = "Organizar documentos"),
                Task(id = "2", title = "Ligar para a família"),
            ),
        )
        val viewModel = OrganizeViewModel(store, PriorityFakeStore())
        advanceUntilIdle()
        viewModel.onSearchChanged("DOCUMENTOS")

        assertEquals(listOf("Organizar documentos"), viewModel.uiState.value.filteredTasks.map { it.title })
    }

    @Test
    fun `toggles a task between completed and next action`() = runTest {
        val store = OrganizeFakeStore(tasks = listOf(Task(id = "1", title = "Caminhar")))
        val viewModel = OrganizeViewModel(store, PriorityFakeStore())
        advanceUntilIdle()

        viewModel.toggleTask(viewModel.uiState.value.tasks.single())
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.tasks.single().status.name == "COMPLETED")
    }

    @Test
    fun `rejects an impossible due date without clearing task draft`() = runTest {
        val viewModel = OrganizeViewModel(OrganizeFakeStore(), PriorityFakeStore())
        advanceUntilIdle()
        viewModel.onTaskTitleChanged("Revisar contrato")
        viewModel.onTaskNoteChanged("Ler cláusulas")
        viewModel.onTaskDueDateChanged("30/02/2026")
        viewModel.onTaskEstimatedMinutesChanged("25")

        viewModel.addTask()

        assertEquals(OrganizeFieldError.InvalidDate, viewModel.uiState.value.taskDueDateError)
        assertEquals("Revisar contrato", viewModel.uiState.value.taskTitleInput)
        assertEquals("Ler cláusulas", viewModel.uiState.value.taskNoteInput)
        assertTrue(viewModel.uiState.value.tasks.isEmpty())
    }

    @Test
    fun `accepts brazilian due date and persists canonical iso date`() = runTest {
        val viewModel = OrganizeViewModel(OrganizeFakeStore(), PriorityFakeStore())
        advanceUntilIdle()
        viewModel.onTaskTitleChanged("Revisar contrato")
        viewModel.onTaskDueDateChanged("15/07/2026")

        viewModel.addTask()
        advanceUntilIdle()

        assertEquals("2026-07-15", viewModel.uiState.value.tasks.single().dueDate)
    }

    @Test
    fun `exposes inline duration error and preserves entered fields`() = runTest {
        val viewModel = OrganizeViewModel(OrganizeFakeStore(), PriorityFakeStore())
        advanceUntilIdle()
        viewModel.onTaskTitleChanged("Caminhar")
        viewModel.onTaskNoteChanged("Depois do almoço")
        viewModel.onTaskEstimatedMinutesChanged("zero")

        viewModel.addTask()

        assertEquals(OrganizeFieldError.InvalidDuration, viewModel.uiState.value.taskDurationError)
        assertEquals("Caminhar", viewModel.uiState.value.taskTitleInput)
        assertEquals("Depois do almoço", viewModel.uiState.value.taskNoteInput)
    }

    @Test
    fun `prevents duplicate task submission while first save is pending`() = runTest {
        val saveGate = CompletableDeferred<Unit>()
        val store = OrganizeFakeStore(taskSaveGate = saveGate)
        val viewModel = OrganizeViewModel(store, PriorityFakeStore())
        advanceUntilIdle()
        viewModel.onTaskTitleChanged("Tarefa única")

        viewModel.addTask()
        assertTrue(viewModel.uiState.value.isTaskSaving)
        viewModel.addTask()
        saveGate.complete(Unit)
        advanceUntilIdle()

        assertEquals(listOf("Tarefa única"), viewModel.uiState.value.tasks.map { it.title })
        assertEquals(OrganizeFeedback.TaskCreated, viewModel.uiState.value.feedback)
    }

    @Test
    fun `promotes a task only when a priority slot is available`() = runTest {
        val task = Task(id = "1", title = "Organizar documentos")
        val priorities = PriorityFakeStore()
        val viewModel = OrganizeViewModel(
            OrganizeFakeStore(tasks = listOf(task)),
            priorities,
        )
        advanceUntilIdle()

        viewModel.promoteToToday(task)
        advanceUntilIdle()

        assertEquals(listOf(task.title), priorities.saved.map { it.title })
    }
}

private class OrganizeFakeStore(
    projects: List<Project> = emptyList(),
    tasks: List<Task> = emptyList(),
    private val taskSaveGate: CompletableDeferred<Unit>? = null,
) : OrganizeStore {
    private val projectsState = MutableStateFlow(projects)
    private val tasksState = MutableStateFlow(tasks)

    override fun observeProjects(): Flow<List<Project>> = projectsState
    override fun observeTasks(): Flow<List<Task>> = tasksState

    override suspend fun addProject(project: Project) {
        projectsState.value = projectsState.value + project
    }

    override suspend fun addTask(task: Task) {
        taskSaveGate?.await()
        tasksState.value = tasksState.value + task
    }

    override suspend fun updateTask(task: Task): Boolean {
        tasksState.value = tasksState.value.map { if (it.id == task.id) task else it }
        return true
    }
}

private class PriorityFakeStore(
    initial: List<PriorityItem> = emptyList(),
) : PriorityStore {
    private val state = MutableStateFlow(initial)
    val saved = mutableListOf<PriorityItem>()

    override fun observeToday(): Flow<List<PriorityItem>> = state

    override suspend fun add(priority: PriorityItem) {
        saved += priority
        state.value = state.value + priority
    }

    override suspend fun complete(id: String): Boolean = true
}
