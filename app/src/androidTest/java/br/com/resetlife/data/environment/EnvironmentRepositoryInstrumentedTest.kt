package br.com.resetlife.data.environment

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import br.com.resetlife.data.local.ResetLifeDatabase
import br.com.resetlife.domain.environment.EnvironmentTask
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class EnvironmentRepositoryInstrumentedTest {

    private lateinit var db: ResetLifeDatabase
    private lateinit var repository: EnvironmentRepository

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ResetLifeDatabase::class.java,
        ).allowMainThreadQueries().build()
        repository = EnvironmentRepository(db.environmentDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun addSpaceAndTask_thenObserve() = runBlocking {
        val spaceId = repository.addSpace("Quarto")
        val taskId = repository.addTask(spaceId, "Tirar poeira", 15)

        val spaces = repository.observeSpaces().first()
        assertEquals(1, spaces.size)
        assertEquals("Quarto", spaces.first().name)

        val tasks = repository.observeTasksBySpace(spaceId).first()
        assertEquals(1, tasks.size)
        assertEquals("Tirar poeira", tasks.first().title)
        assertEquals(15, tasks.first().estimatedMinutes)
    }

    @Test
    fun setTaskDone_marksLastOrganized() = runBlocking {
        val spaceId = repository.addSpace("Cozinha")
        val taskId = repository.addTask(spaceId, "Lavar louça", 30)
        val task = repository.observeTasksBySpace(spaceId).first().first()

        repository.setTaskDone(task, true)
        val updated = repository.observeTasksBySpace(spaceId).first().first()
        assertTrue(updated.done)
        assertNotNull(updated.doneAt)

        val space = repository.observeSpaces().first().first()
        assertNotNull(space.lastOrganizedAt)
    }

    @Test
    fun addToDiscardList_appearsInDiscard() = runBlocking {
        val spaceId = repository.addSpace("Quarto")
        repository.addTask(spaceId, "Doar livros", 5, discardList = true)

        val discard = repository.observeDiscardList().first()
        assertEquals(1, discard.size)
        assertTrue(discard.first().discardList)
    }
}
