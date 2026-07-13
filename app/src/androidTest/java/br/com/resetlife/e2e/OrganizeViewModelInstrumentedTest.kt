package br.com.resetlife.e2e

import androidx.test.platform.app.InstrumentationRegistry
import br.com.resetlife.ResetLifeApplication
import br.com.resetlife.data.organize.OrganizeStore
import br.com.resetlife.data.today.PriorityStore
import br.com.resetlife.presentation.organize.OrganizeViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class OrganizeViewModelInstrumentedTest {
    private lateinit var viewModel: OrganizeViewModel
    private lateinit var store: OrganizeStore
    private lateinit var priorityStore: PriorityStore

    @Before
    fun setup() {
        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as ResetLifeApplication
        store = app.organizeStore
        priorityStore = app.priorityStore
        viewModel = OrganizeViewModel(store, priorityStore)
    }

    @Test
    fun addTaskViaViewModelPersistsToDatabase() = runBlocking {
        val before = store.observeTasks().first().size
        viewModel.onTaskTitleChanged("Tarefa VM")
        viewModel.addTask()
        kotlinx.coroutines.delay(2000)
        val after = store.observeTasks().first()
        assertTrue("ViewModel.addTask não persistiu (before=$before, after=${after.size})", after.size > before)
    }
}
