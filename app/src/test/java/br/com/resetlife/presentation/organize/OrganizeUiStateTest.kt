package br.com.resetlife.presentation.organize

import br.com.resetlife.domain.organize.Task
import org.junit.Assert.assertEquals
import org.junit.Test

class OrganizeUiStateTest {
    @Test
    fun `filtered tasks are grouped into open and completed lists`() {
        val open = Task.create("1", "Aberta").let { result ->
            (result as br.com.resetlife.domain.organize.TaskCreationResult.Created).task
        }
        val completed = Task.create("2", "Concluída").let { result ->
            (result as br.com.resetlife.domain.organize.TaskCreationResult.Created).task.complete()
        }

        val state = OrganizeUiState(tasks = listOf(open, completed), isLoading = false)

        assertEquals(listOf(open), state.filteredOpenTasks)
        assertEquals(listOf(completed), state.filteredCompletedTasks)
    }
}
