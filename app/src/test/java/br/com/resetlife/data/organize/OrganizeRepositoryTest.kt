package br.com.resetlife.data.organize

import br.com.resetlife.data.local.organize.ProjectDao
import br.com.resetlife.data.local.organize.ProjectEntity
import br.com.resetlife.data.local.organize.TaskDao
import br.com.resetlife.data.local.organize.TaskEntity
import br.com.resetlife.domain.organize.Project
import br.com.resetlife.domain.organize.Task
import br.com.resetlife.domain.organize.TaskStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OrganizeRepositoryTest {
    @Test
    fun `persists a task related to a project and maps status`() = runBlocking {
        val projects = FakeProjectDao()
        val tasks = FakeTaskDao()
        val repository = OrganizeRepository(projects, tasks)
        val project = Project(id = "project-1", title = "Casa", goal = "Organizar a casa")
        val task = Task(
            id = "task-1",
            title = "Separar documentos",
            projectId = project.id,
            status = TaskStatus.NEXT_ACTION,
        )

        repository.addProject(project)
        repository.addTask(task)
        val observed = repository.observeTasks().first()

        assertEquals(project.id, tasks.saved.single().projectId)
        assertEquals(task.title, observed.single().title)
        assertEquals(TaskStatus.NEXT_ACTION, observed.single().status)
    }

    @Test
    fun `completes a task without losing its project`() = runBlocking {
        val projects = FakeProjectDao()
        val tasks = FakeTaskDao(
            initial = listOf(
                TaskEntity("task-1", "Separar documentos", "", null, null, "project-1", "NEXT_ACTION", 0),
            ),
        )
        val repository = OrganizeRepository(projects, tasks)

        assertTrue(repository.updateTask(tasks.saved.first().toDomain().complete()))
        val completed = repository.observeTasks().first().single()

        assertEquals(TaskStatus.COMPLETED, completed.status)
        assertEquals("project-1", completed.projectId)
    }
}

private class FakeProjectDao(
    initial: List<ProjectEntity> = emptyList(),
) : ProjectDao {
    private val state = MutableStateFlow(initial)
    val saved = mutableListOf<ProjectEntity>()

    override fun observeAll(): Flow<List<ProjectEntity>> = state

    override suspend fun upsert(project: ProjectEntity) {
        saved += project
        state.value = state.value.filterNot { it.id == project.id } + project
    }
}

private class FakeTaskDao(
    initial: List<TaskEntity> = emptyList(),
) : TaskDao {
    private val state = MutableStateFlow(initial)
    val saved = initial.toMutableList()

    override fun observeAll(): Flow<List<TaskEntity>> = state

    override suspend fun upsert(task: TaskEntity) {
        saved.removeAll { it.id == task.id }
        saved += task
        state.value = saved.toList()
    }
}

private fun TaskEntity.toDomain(): Task = Task(
    id = id,
    title = title,
    note = note,
    dueDate = dueDate,
    estimatedMinutes = estimatedMinutes,
    projectId = projectId,
    status = TaskStatus.valueOf(status),
)
