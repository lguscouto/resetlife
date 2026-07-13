package br.com.resetlife.presentation.today

import br.com.resetlife.MainDispatcherRule
import br.com.resetlife.data.today.PriorityStore
import br.com.resetlife.domain.today.PriorityItem
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TodayViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    @Test
    fun `adding a valid priority clears entry and exposes it in state`() = runTest {
        val viewModel = TodayViewModel(FakePriorityStore())
        advanceUntilIdle()

        viewModel.onPriorityInputChanged("Organizar documentos")
        viewModel.addPriority()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("", state.priorityInput)
        assertEquals(listOf("Organizar documentos"), state.priorities.map { it.title })
        assertFalse(state.isAddPriorityEnabled)
    }

    @Test
    fun `exposes feedback when a fourth priority is requested`() = runTest {
        val viewModel = TodayViewModel(FakePriorityStore())
        advanceUntilIdle()

        repeat(3) { index ->
            viewModel.onPriorityInputChanged("Prioridade $index")
            viewModel.addPriority()
            advanceUntilIdle()
        }
        viewModel.onPriorityInputChanged("Uma quarta prioridade")
        viewModel.addPriority()

        assertEquals(TodayFeedback.LimitReached, viewModel.uiState.value.feedback)
    }

    @Test
    fun `prevents duplicate priority submission while first save is pending`() = runTest {
        val saveGate = CompletableDeferred<Unit>()
        val viewModel = TodayViewModel(FakePriorityStore(addGate = saveGate))
        advanceUntilIdle()
        viewModel.onPriorityInputChanged("Prioridade única")

        viewModel.addPriority()
        assertTrue(viewModel.uiState.value.isSavingPriority)
        viewModel.addPriority()
        saveGate.complete(Unit)
        advanceUntilIdle()

        assertEquals(listOf("Prioridade única"), viewModel.uiState.value.priorities.map { it.title })
        assertEquals(TodayFeedback.Added, viewModel.uiState.value.feedback)
    }

    @Test
    fun `completing a priority updates its visible state`() = runTest {
        val viewModel = TodayViewModel(FakePriorityStore())
        advanceUntilIdle()
        viewModel.onPriorityInputChanged("Caminhar 15 minutos")
        viewModel.addPriority()
        advanceUntilIdle()
        val priorityId = viewModel.uiState.value.priorities.single().id

        viewModel.completePriority(priorityId)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.priorities.single().isCompleted)
    }
}

private class FakePriorityStore(
    initial: List<PriorityItem> = emptyList(),
    private val addGate: CompletableDeferred<Unit>? = null,
) : PriorityStore {
    private val state = MutableStateFlow(initial)

    override fun observeToday(): Flow<List<PriorityItem>> = state

    override suspend fun add(priority: PriorityItem) {
        addGate?.await()
        state.value = state.value + priority
    }

    override suspend fun complete(id: String): Boolean {
        if (state.value.none { it.id == id }) return false
        state.value = state.value.map { item ->
            if (item.id == id) item.copy(isCompleted = true) else item
        }
        return true
    }
}
