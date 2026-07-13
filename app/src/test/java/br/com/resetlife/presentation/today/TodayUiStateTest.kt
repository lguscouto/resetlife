package br.com.resetlife.presentation.today

import br.com.resetlife.domain.today.PriorityItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TodayUiStateTest {
    @Test
    fun `partitions active and completed priorities without changing order`() {
        val first = PriorityItem(id = "1", title = "Primeira")
        val completed = PriorityItem(id = "2", title = "Concluída", isCompleted = true)
        val last = PriorityItem(id = "3", title = "Última")

        val state = TodayUiState(priorities = listOf(first, completed, last), isLoading = false)

        assertEquals(listOf(first, last), state.activePriorities)
        assertEquals(listOf(completed), state.completedPriorities)
        assertEquals(2, state.activePriorityCount)
    }

    @Test
    fun `exposes retry when loading or storage fails`() {
        assertTrue(TodayUiState(loadError = true).canRetry)
        assertTrue(TodayUiState(feedback = TodayFeedback.StorageError).canRetry)
        assertTrue(!TodayUiState(isLoading = false).canRetry)
    }
}
