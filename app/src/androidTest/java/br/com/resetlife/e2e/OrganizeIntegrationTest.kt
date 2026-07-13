package br.com.resetlife.e2e

import androidx.test.platform.app.InstrumentationRegistry
import br.com.resetlife.ResetLifeApplication
import br.com.resetlife.domain.organize.Task
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class OrganizeIntegrationTest {
    @Test
    fun storePersistsTaskAndObserverEmits() = runBlocking {
        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as ResetLifeApplication
        val store = app.organizeStore
        val result = Task.create(id = "it-${System.currentTimeMillis()}", title = "Tarefa Integração")
        assertTrue("Task.create retornou vazio", result is br.com.resetlife.domain.organize.TaskCreationResult.Created)
        val task = (result as br.com.resetlife.domain.organize.TaskCreationResult.Created).task
        val before = store.observeTasks().first().size
        store.addTask(task)
        val after = store.observeTasks().first()
        assertTrue("Tarefa não persistida pelo store (before=$before, after=${after.size})", after.size > before)
    }
}
