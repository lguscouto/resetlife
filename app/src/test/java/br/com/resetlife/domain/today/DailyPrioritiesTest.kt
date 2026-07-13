package br.com.resetlife.domain.today

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DailyPrioritiesTest {
    @Test
    fun `rejects a fourth active priority`() {
        var priorities = DailyPriorities.empty()

        priorities = priorities.addOrFail(id = "1", title = "Organizar documentos")
        priorities = priorities.addOrFail(id = "2", title = "Caminhar 15 minutos")
        priorities = priorities.addOrFail(id = "3", title = "Planejar a semana")

        val result = priorities.add(id = "4", title = "Ligar para a família")

        assertEquals(3, priorities.activeCount)
        assertEquals(AddPriorityResult.LimitReached, result)
    }

    @Test
    fun `rejects a blank priority title`() {
        val result = DailyPriorities.empty().add(id = "1", title = "   ")

        assertEquals(AddPriorityResult.EmptyTitle, result)
    }

    @Test
    fun `concluding a priority frees a daily slot`() {
        var priorities = DailyPriorities.empty()
        priorities = priorities.addOrFail(id = "1", title = "Organizar documentos")
        priorities = priorities.addOrFail(id = "2", title = "Caminhar 15 minutos")
        priorities = priorities.addOrFail(id = "3", title = "Planejar a semana")

        val completion = priorities.complete(id = "2")
        assertTrue("A prioridade existente deveria ser concluída", completion is CompletionResult.Updated)
        priorities = (completion as CompletionResult.Updated).priorities
        val added = priorities.add(id = "4", title = "Ligar para a família")

        assertEquals(2, priorities.activeCount)
        assertTrue("A vaga liberada deveria aceitar nova prioridade", added is AddPriorityResult.Added)
    }

    @Test
    fun `rejects completion when priority does not exist`() {
        val result = DailyPriorities.empty().complete(id = "ausente")

        assertEquals(CompletionResult.NotFound, result)
    }

    @Test
    fun `restores persisted priorities before applying the daily limit`() {
        val priorities = DailyPriorities.from(
            listOf(
                PriorityItem(id = "1", title = "Concluída", isCompleted = true),
                PriorityItem(id = "2", title = "Ativa 1"),
                PriorityItem(id = "3", title = "Ativa 2"),
                PriorityItem(id = "4", title = "Ativa 3"),
            ),
        )

        assertEquals(3, priorities.activeCount)
        assertEquals(AddPriorityResult.LimitReached, priorities.add("5", "Nova"))
    }

    private fun DailyPriorities.addOrFail(id: String, title: String): DailyPriorities {
        val result = add(id = id, title = title)
        assertTrue("A prioridade deveria ser aceita", result is AddPriorityResult.Added)
        return (result as AddPriorityResult.Added).priorities
    }
}
