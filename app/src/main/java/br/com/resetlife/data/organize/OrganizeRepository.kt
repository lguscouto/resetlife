package br.com.resetlife.data.organize

import br.com.resetlife.data.local.organize.ProjectDao
import br.com.resetlife.data.local.organize.ProjectEntity
import br.com.resetlife.data.local.organize.TaskDao
import br.com.resetlife.data.local.organize.TaskEntity
import br.com.resetlife.domain.organize.Project
import br.com.resetlife.domain.organize.Task
import br.com.resetlife.domain.organize.TaskStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface OrganizeStore {
    fun observeProjects(): Flow<List<Project>>

    fun observeTasks(): Flow<List<Task>>

    suspend fun addProject(project: Project)

    suspend fun addTask(task: Task)

    suspend fun updateTask(task: Task): Boolean
}

class OrganizeRepository(
    private val projectDao: ProjectDao,
    private val taskDao: TaskDao,
) : OrganizeStore {
    override fun observeProjects(): Flow<List<Project>> =
        projectDao.observeAll().map { projects -> projects.map { it.toDomain() } }

    override fun observeTasks(): Flow<List<Task>> =
        taskDao.observeAll().map { tasks -> tasks.map { it.toDomain() } }

    override suspend fun addProject(project: Project) {
        projectDao.upsert(project.toEntity(createdAt = System.currentTimeMillis()))
    }

    override suspend fun addTask(task: Task) {
        taskDao.upsert(task.toEntity(createdAt = System.currentTimeMillis()))
    }

    override suspend fun updateTask(task: Task): Boolean {
        taskDao.upsert(task.toEntity(createdAt = System.currentTimeMillis()))
        return true
    }
}

private fun ProjectEntity.toDomain(): Project = Project(id = id, title = title, goal = goal)

private fun Project.toEntity(createdAt: Long): ProjectEntity =
    ProjectEntity(id = id, title = title, goal = goal, createdAt = createdAt)

private fun TaskEntity.toDomain(): Task =
    Task(
        id = id,
        title = title,
        note = note,
        dueDate = dueDate,
        estimatedMinutes = estimatedMinutes,
        projectId = projectId,
        status = TaskStatus.valueOf(status),
    )

private fun Task.toEntity(createdAt: Long): TaskEntity =
    TaskEntity(
        id = id,
        title = title,
        note = note,
        dueDate = dueDate,
        estimatedMinutes = estimatedMinutes,
        projectId = projectId,
        status = status.name,
        createdAt = createdAt,
    )
