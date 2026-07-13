package br.com.resetlife.data.today

import br.com.resetlife.data.local.today.PriorityDao
import br.com.resetlife.data.local.today.PriorityEntity
import br.com.resetlife.domain.today.PriorityItem
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class PriorityRepositoryTest {
    private val clock = Clock.fixed(
        Instant.parse("2026-07-12T12:00:00Z"),
        ZoneOffset.UTC,
    )

    @Test
    fun `saves a priority using the current day`() = runBlocking {
        val dao = FakePriorityDao()
        val repository = PriorityRepository(dao = dao, clock = clock)

        repository.add(PriorityItem(id = "p1", title = "Organizar documentos"))

        assertEquals("2026-07-12", dao.saved.single().dayKey)
        assertEquals("Organizar documentos", dao.saved.single().title)
        assertEquals(false, dao.saved.single().isCompleted)
    }

    @Test
    fun `completes an existing priority for the current day`() = runBlocking {
        val dao = FakePriorityDao()
        val repository = PriorityRepository(dao = dao, clock = clock)
        repository.add(PriorityItem(id = "p1", title = "Organizar documentos"))

        val completed = repository.complete(id = "p1")

        assertEquals(true, completed)
        assertEquals(true, dao.saved.last().isCompleted)
    }

    @Test
    fun `observes current day and maps rows in creation order`() = runBlocking {
        val dao = FakePriorityDao(
            initial = listOf(
                PriorityEntity("p1", "Primeira", false, "2026-07-12", 1),
                PriorityEntity("p2", "Segunda", true, "2026-07-12", 2),
                PriorityEntity("old", "Ontem", false, "2026-07-11", 3),
            ),
        )
        val repository = PriorityRepository(dao = dao, clock = clock)

        val priorities = repository.observeToday().first()

        assertEquals(listOf("Primeira", "Segunda"), priorities.map { it.title })
        assertEquals(true, priorities[1].isCompleted)
    }
}

private class FakePriorityDao(
    initial: List<PriorityEntity> = emptyList(),
) : PriorityDao {
    private val state = MutableStateFlow(initial)
    val saved = mutableListOf<PriorityEntity>()

    override fun observeByDay(dayKey: String): Flow<List<PriorityEntity>> =
        state.map { entities -> entities.filter { it.dayKey == dayKey } }

    override suspend fun findByDay(dayKey: String): List<PriorityEntity> =
        state.value.filter { it.dayKey == dayKey }

    override suspend fun upsert(priority: PriorityEntity) {
        saved += priority
        state.value = (state.value.filterNot { it.id == priority.id } + priority)
    }
}
