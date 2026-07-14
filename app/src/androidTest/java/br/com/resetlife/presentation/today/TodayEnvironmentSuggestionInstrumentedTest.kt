package br.com.resetlife.presentation.today

import androidx.test.platform.app.InstrumentationRegistry
import br.com.resetlife.ResetLifeApplication
import br.com.resetlife.data.environment.EnvironmentRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class TodayEnvironmentSuggestionInstrumentedTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val app = context.applicationContext as ResetLifeApplication
    private val repo: EnvironmentRepository = app.environmentStore

    @Test
    fun suggestion_appears_when_pending_environment_task_exists(): Unit = runBlocking {
        val spaceId = repo.addSpace("Quarto teste")
        repo.addTask(spaceId, "Limpar criado", 10)
        val spaces = repo.observeSpaces().first()
        val tasks = repo.observeTasksBySpace(spaceId).first()
        val pending = tasks.firstOrNull { !it.done && !it.discardList }
        assertNotNull("esperada tarefa pendente de ambiente", pending)

        // Ao marcar como concluída, a sugestão some
        pending?.let { repo.setTaskDone(it, true) }
        val tasksAfter = repo.observeTasksBySpace(spaceId).first()
        val pendingAfter = tasksAfter.firstOrNull { !it.done && !it.discardList }
        assertNull("após concluir, não deve haver sugestão pendente", pendingAfter)
    }
}
