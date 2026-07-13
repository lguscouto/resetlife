package br.com.resetlife.presentation.today

import br.com.resetlife.MainDispatcherRule
import br.com.resetlife.data.today.PriorityStore
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
    fun `prevents duplicate completion while persistence is pending and confirms it`() = runTest {
        val completeGate = CompletableDeferred<Unit>()
        val store = FakePriorityStore(
            initial = listOf(PriorityItem(id = "priority-1", title = "Caminhar")),
            completeGate = completeGate,
        )
        val viewModel = TodayViewModel(store)
        advanceUntilIdle()

        viewModel.completePriority("priority-1")
        runCurrent()
        viewModel.completePriority("priority-1")
        completeGate.complete(Unit)
        advanceUntilIdle()

        assertEquals(1, store.completeCalls)
        assertTrue(viewModel.uiState.value.priorities.single().isCompleted)
        assertTrue(viewModel.uiState.value.feedback != null)
    }

    @Test
    fun `retries a transient priority loading failure`() = runTest {
        val viewModel = TodayViewModel(
            FakePriorityStore(
                initial = listOf(PriorityItem(id = "priority-1", title = "Caminhar")),
                observeFailures = 1,
            ),
        )
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.loadError)

        viewModel.retryStorageOperation()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.loadError)
        assertEquals(listOf("Caminhar"), viewModel.uiState.value.priorities.map { it.title })
    }

    @Test
    fun `retries failed priority save without losing draft`() = runTest {
        val store = FakePriorityStore(addFailures = 1)
        val viewModel = TodayViewModel(store)
        advanceUntilIdle()
        viewModel.onPriorityInputChanged("Prioridade recuperável")

        viewModel.addPriority()
        advanceUntilIdle()

        assertEquals(TodayFeedback.StorageError, viewModel.uiState.value.feedback)
        assertEquals("Prioridade recuperável", viewModel.uiState.value.priorityInput)
        assertTrue(viewModel.uiState.value.canRetry)

        viewModel.retryStorageOperation()
        advanceUntilIdle()

        assertEquals(TodayFeedback.Added, viewModel.uiState.value.feedback)
        assertEquals(listOf("Prioridade recuperável"), viewModel.uiState.value.priorities.map { it.title })
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
    private val completeGate: CompletableDeferred<Unit>? = null,
    private var addFailures: Int = 0,
    private var observeFailures: Int = 0,
) : PriorityStore {
    private val state = MutableStateFlow(initial)
    var completeCalls = 0

    override fun observeToday(): Flow<List<PriorityItem>> {
        if (observeFailures > 0) {
            observeFailures -= 1
            return flow { error("transient loading failure") }
        }
        return state
    }

    override suspend fun add(priority: PriorityItem) {
        addGate?.await()
        if (addFailures > 0) {
            addFailures -= 1
            error("transient storage failure")
        }
        state.value = state.value + priority
    }

    override suspend fun complete(id: String): Boolean {
        completeCalls += 1
        completeGate?.await()
        if (state.value.none { it.id == id }) return false
        state.value = state.value.map { item ->
            if (item.id == id) item.copy(isCompleted = true) else item
        }
        return true
    }
}
