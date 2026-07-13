package br.com.resetlife.presentation.organize

import br.com.resetlife.MainDispatcherRule
import br.com.resetlife.data.organize.OrganizeStore
import br.com.resetlife.data.today.PriorityStore
import br.com.resetlife.domain.organize.Project
import br.com.resetlife.domain.organize.Task
import br.com.resetlife.domain.today.PriorityItem
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
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
    fun `prevents a second task update while persistence is pending and confirms it`() = runTest {
        val updateGate = CompletableDeferred<Unit>()
        val task = Task(id = "task-1", title = "Caminhar")
        val store = OrganizeFakeStore(
            tasks = listOf(task),
            taskUpdateGate = updateGate,
        )
        val viewModel = OrganizeViewModel(store, PriorityFakeStore())
        advanceUntilIdle()

        viewModel.toggleTask(task)
        runCurrent()
        viewModel.toggleTask(task)
        updateGate.complete(Unit)
        advanceUntilIdle()

        assertEquals(1, store.updateCalls)
        assertTrue(viewModel.uiState.value.tasks.single().status.name == "COMPLETED")
        assertTrue(viewModel.uiState.value.feedback != null)
    }

    @Test
    fun `retries failed task save without losing draft`() = runTest {
        val store = OrganizeFakeStore(taskSaveFailures = 1)
        val viewModel = OrganizeViewModel(store, PriorityFakeStore())
        advanceUntilIdle()
        viewModel.onTaskTitleChanged("Tarefa recuperável")

        viewModel.addTask()
        advanceUntilIdle()

        assertEquals(OrganizeFeedback.StorageError, viewModel.uiState.value.feedback)
        assertEquals("Tarefa recuperável", viewModel.uiState.value.taskTitleInput)
        assertTrue(viewModel.uiState.value.canRetry)

        viewModel.retryStorageOperation()
        advanceUntilIdle()

        assertEquals(OrganizeFeedback.TaskCreated, viewModel.uiState.value.feedback)
        assertEquals(listOf("Tarefa recuperável"), viewModel.uiState.value.tasks.map { it.title })
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

    @Test
    fun `prevents repeated promotion while first priority save is pending`() = runTest {
        val addGate = CompletableDeferred<Unit>()
        val task = Task(id = "task-1", title = "Organizar documentos")
        val priorities = PriorityFakeStore(addGate = addGate)
        val viewModel = OrganizeViewModel(
            OrganizeFakeStore(tasks = listOf(task)),
            priorities,
        )
        advanceUntilIdle()

        viewModel.promoteToToday(task)
        runCurrent()
        viewModel.promoteToToday(task)
        addGate.complete(Unit)
        advanceUntilIdle()

        assertEquals(1, priorities.addCalls)
        assertEquals(OrganizeFeedback.Promoted, viewModel.uiState.value.feedback)
    }

    @Test
    fun `retries a transient organize loading failure`() = runTest {
        val viewModel = OrganizeViewModel(
            OrganizeFakeStore(
                tasks = listOf(Task(id = "task-1", title = "Caminhar")),
                projectObserveFailures = 1,
            ),
            PriorityFakeStore(),
        )
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.loadError)

        viewModel.retryStorageOperation()
        advanceUntilIdle()

        assertTrue(!viewModel.uiState.value.loadError)
        assertEquals(listOf("Caminhar"), viewModel.uiState.value.tasks.map { it.title })
    }
}

private class OrganizeFakeStore(
    projects: List<Project> = emptyList(),
    tasks: List<Task> = emptyList(),
    private val taskSaveGate: CompletableDeferred<Unit>? = null,
    private val taskUpdateGate: CompletableDeferred<Unit>? = null,
    private var taskSaveFailures: Int = 0,
    private var projectObserveFailures: Int = 0,
) : OrganizeStore {
    private val projectsState = MutableStateFlow(projects)
    private val tasksState = MutableStateFlow(tasks)
    var updateCalls = 0

    override fun observeProjects(): Flow<List<Project>> {
        if (projectObserveFailures > 0) {
            projectObserveFailures -= 1
            return flow { error("transient loading failure") }
        }
        return projectsState
    }

    override fun observeTasks(): Flow<List<Task>> = tasksState

    override suspend fun addProject(project: Project) {
        projectsState.value = projectsState.value + project
    }

    override suspend fun addTask(task: Task) {
        taskSaveGate?.await()
        if (taskSaveFailures > 0) {
            taskSaveFailures -= 1
            error("transient storage failure")
        }
        tasksState.value = tasksState.value + task
    }

    override suspend fun updateTask(task: Task): Boolean {
        updateCalls += 1
        taskUpdateGate?.await()
        tasksState.value = tasksState.value.map { if (it.id == task.id) task else it }
        return true
    }
}

private class PriorityFakeStore(
    initial: List<PriorityItem> = emptyList(),
    private val addGate: CompletableDeferred<Unit>? = null,
) : PriorityStore {
    private val state = MutableStateFlow(initial)
    val saved = mutableListOf<PriorityItem>()
    var addCalls = 0

    override fun observeToday(): Flow<List<PriorityItem>> = state

    override suspend fun add(priority: PriorityItem) {
        addCalls += 1
        addGate?.await()
        saved += priority
        state.value = state.value + priority
    }

    override suspend fun complete(id: String): Boolean = true
}
