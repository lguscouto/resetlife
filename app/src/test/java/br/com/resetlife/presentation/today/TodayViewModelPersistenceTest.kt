package br.com.resetlife.presentation.today

import br.com.resetlife.MainDispatcherRule
import br.com.resetlife.data.today.PriorityStore
import br.com.resetlife.presentation.today.FakeEnvironmentRepository
import br.com.resetlife.domain.today.PriorityItem
import java.util.Collections
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TodayViewModelPersistenceTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    @Test
    fun `loads priorities persisted by the repository`() = runTest {
        val store = PersistenceFakePriorityStore(
            initial = listOf(PriorityItem(id = "saved", title = "Prioridade persistida")),
        )
        val viewModel = TodayViewModel(store, FakeEnvironmentRepository())

        advanceUntilIdle()

        assertEquals(listOf("Prioridade persistida"), viewModel.uiState.value.priorities.map { it.title })
        assertFalse(viewModel.uiState.value.isLoading)
    }
}

private class PersistenceFakePriorityStore(
    initial: List<PriorityItem> = emptyList(),
) : PriorityStore {
    private val state = MutableStateFlow(initial)
    private val saved = Collections.synchronizedList(initial.toMutableList())

    override fun observeToday(): Flow<List<PriorityItem>> = state

    override suspend fun add(priority: PriorityItem) {
        saved += priority
        state.value = saved.toList()
    }

    override suspend fun complete(id: String): Boolean {
        val index = saved.indexOfFirst { it.id == id }
        if (index == -1) return false
        saved[index] = saved[index].copy(isCompleted = true)
        state.value = saved.toList()
        return true
    }
}
