package br.com.resetlife.e2e

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import br.com.resetlife.MainActivity
import br.com.resetlife.ResetLifeApplication
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Jornada real de interface no emulador: exercita as telas Hoje e Organizar,
 * os campos de texto, os botões de ação e os controles de acessibilidade
 * (content-description dos checkboxes e do botão promover).
 *
 * Usa UiAutomator (UiAutomation) em vez de Espresso para evitar o bug de
 * InputManager.getInstance em imagens Android 15+.
 */
@RunWith(AndroidJUnit4::class)
class AppJourneyUiAutomatorTest {

    private lateinit var device: UiDevice

    private val pkg = "br.com.resetlife"
    private val timeout = 5_000L

    @Before
    fun setup() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.pressHome()
        ActivityScenario.launch(MainActivity::class.java)
        device.wait(Until.hasObject(By.pkg(pkg).depth(0)), timeout)
    }

    private fun waitText(text: String): UiObject2 {
        val node = device.wait(Until.findObject(By.text(text)), timeout)
        assertNotNull("Texto não encontrado na tela: '$text'", node)
        return node!!
    }

    private fun waitDesc(desc: String): UiObject2 {
        val node = device.wait(Until.findObject(By.desc(desc)), timeout)
        assertNotNull("Content-description não encontrado: '$desc'", node)
        return node!!
    }

    private fun clickButtonByText(text: String) {
        val candidates = device.findObjects(By.text(text))
        assertNotNull("Texto não encontrado para clique: '$text'", candidates)
        for (candidate in candidates) {
            var node: UiObject2? = candidate
            while (node != null && !node.isClickable) {
                node = node.parent
            }
            if (node != null && node.isClickable) {
                node.click()
                device.waitForIdle(timeout)
                return
            }
        }
        // Fallback: clica no centro do primeiro candidato (despacha toque nas coordenadas).
        candidates[0].click()
        device.waitForIdle(timeout)
    }

    private fun typeInField(label: String, value: String) {
        val labelNode = device.wait(Until.findObject(By.text(label)), timeout)
        assertNotNull("Label não encontrado: '$label'", labelNode)
        // Sobe até o ancestor que contenha um EditText entre os descendentes diretos.
        var node: UiObject2? = labelNode
        var editText: UiObject2? = null
        while (node != null && editText == null) {
            editText = node.children.firstOrNull { it.className == "android.widget.EditText" }
            node = node.parent
        }
        assertNotNull("EditText para o label '$label' não encontrado", editText)
        editText!!.click()
        device.waitForIdle(timeout)
        // UiAutomator 2 setText usa InputConnection e dispara onValueChange do Compose.
        editText.setText(value)
        device.waitForIdle(timeout)
    }

    @Test
    fun todayScreen_addPriorityAndCompleteIt() {
        // Tela Hoje deve estar visível.
        waitText("Prioridades de hoje")

        // Campo de nova prioridade + botão Adicionar.
        typeInField("Nova prioridade", "PrioridadeE2E")
        clickButtonByText("Adicionar")

        // Feedback de sucesso aparece.
        waitText("Prioridade adicionada.")

        // O checkbox da prioridade tem content-description acessível.
        val checkbox = waitDesc("Concluir prioridade: PrioridadeE2E")
        checkbox.click()
        device.waitForIdle(timeout)

        // Feedback de conclusão confirma a ação (indicador não-baseado em cor
        // é validado separadamente por inspeção de código/acessibilidade).
        waitText("Prioridade concluída.")
    }

    @Test
    fun organizeScreen_createTaskToggleAndPromote() {
        // Navega para Organizar pela barra inferior.
        clickButtonByText("Organizar")
        waitText("Tire as ideias da cabeça e transforme-as em próximos passos.")

        // Criar tarefa.
        typeInField("Título da tarefa *", "TarefaE2E")
        // O botão de ação fica abaixo do formulário; garante visibilidade.
        device.swipe(540, 1800, 540, 600, 20)
        device.waitForIdle(timeout)
        clickButtonByText("Criar tarefa")
        waitText("Tarefa criada.")
        // Aguarda o viewModelScope.launch(Dispatchers.IO) completar o upsert + WAL checkpoint.
        device.waitForIdle(3000)
        // Verifica no banco se a tarefa foi persistida (isolando bug de UI vs store).
        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as ResetLifeApplication
        val tasksInDb = runBlocking { app.organizeStore.observeTasks().first() }
        assertTrue(
            "Tarefa não persistida após clique no botão Criar tarefa (DB não contém TarefaE2E)",
            tasksInDb.any { it.title.contains("TarefaE2E", ignoreCase = true) },
        )

        // Localiza a linha da tarefa pelo título e clica no checkbox (primeiro filho da Row).
        // Rola a lista para baixo para garantir que a tarefa nova esteja visível.
        device.swipe(540, 1200, 540, 400, 10)
        device.waitForIdle(1000)
        val titleNode = device.wait(Until.findObject(By.textContains("TarefaE2E")), timeout)
        if (titleNode == null) {
            val allTexts = device.findObjects(By.textContains("")).mapNotNull { it.text }.distinct()
            throw AssertionError("Tarefa não encontrada. Textos na tela: $allTexts")
        }
        val row = titleNode!!.parent
        val checkbox = row?.children?.firstOrNull()
        assertNotNull("Checkbox da tarefa não encontrado na linha", checkbox)
        checkbox!!.click()
        device.waitForIdle(timeout)
        waitText("Tarefa concluída.")

        // Reabrir para poder promover novamente (mesma linha, checkbox agora reabre).
        val titleNode2 = device.wait(Until.findObject(By.textContains("TarefaE2E")), timeout)
        val row2 = titleNode2!!.parent
        row2?.children?.firstOrNull()?.click()
        device.waitForIdle(timeout)
        waitText("Tarefa reaberta.")

        // Botão promover para Hoje.
        val promote = device.wait(Until.findObject(By.text("Adicionar a Hoje")), timeout)
            ?: device.wait(Until.findObject(By.descStartsWith("Adicionar a Hoje")), timeout)
        assertNotNull("Botão promover não encontrado", promote)
        promote!!.click()
        device.waitForIdle(timeout)
        waitText("Tarefa adicionada às prioridades de Hoje.")
    }

    @Test
    fun navigationBetweenScreensWorks() {
        clickButtonByText("Organizar")
        waitText("Tire as ideias da cabeça e transforme-as em próximos passos.")
        clickButtonByText("Hoje")
        waitText("Prioridades de hoje")
    }
}
