package br.com.resetlife.domain.organize

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TaskTest {
    @Test
    fun `creates a next action with normalized title and optional metadata`() {
        val result = Task.create(
            id = "task-1",
            title = "  Organizar documentos  ",
            note = "Separar por categoria",
            dueDate = "2026-07-15",
            estimatedMinutes = 30,
            projectId = "project-1",
        )

        assertTrue(result is TaskCreationResult.Created)
        val task = (result as TaskCreationResult.Created).task
        assertEquals("Organizar documentos", task.title)
        assertEquals(TaskStatus.NEXT_ACTION, task.status)
        assertEquals("project-1", task.projectId)
        assertEquals(30, task.estimatedMinutes)
    }

    @Test
    fun `rejects an empty title`() {
        assertEquals(
            TaskCreationResult.EmptyTitle,
            Task.create(id = "task-1", title = "   "),
        )
    }

    @Test
    fun `completes and reopens a task without losing metadata`() {
        val created = Task.create(
            id = "task-1",
            title = "Planejar a semana",
            note = "Domingo à tarde",
            projectId = "project-1",
        ) as TaskCreationResult.Created

        val completed = created.task.complete()
        val reopened = completed.reopen()

        assertEquals(TaskStatus.COMPLETED, completed.status)
        assertEquals(TaskStatus.NEXT_ACTION, reopened.status)
        assertEquals("Domingo à tarde", reopened.note)
        assertEquals("project-1", reopened.projectId)
    }

    @Test
    fun `matches a query ignoring case and outer spaces`() {
        val task = Task.create("task-1", "Organizar documentos") as TaskCreationResult.Created

        assertTrue(task.task.matchesQuery("  DOCUMENTOS "))
    }
}
