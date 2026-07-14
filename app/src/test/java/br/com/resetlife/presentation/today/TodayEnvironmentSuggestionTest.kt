package br.com.resetlife.presentation.today

import br.com.resetlife.MainDispatcherRule
import br.com.resetlife.data.environment.EnvironmentRepository
import br.com.resetlife.presentation.today.FakePriorityStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TodayEnvironmentSuggestionTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun repoWithPendingTask(): EnvironmentRepository {
        val spaceEntity = br.com.resetlife.data.local.environment.EnvironmentSpaceEntity(id = "s1", name = "Quarto")
        val taskEntity = br.com.resetlife.data.local.environment.EnvironmentTaskEntity(
            id = "t1", spaceId = "s1", title = "Limpar", estimatedMinutes = 10,
        )
        return object : EnvironmentRepository(
            object : br.com.resetlife.data.local.environment.EnvironmentDao {
                override fun observeSpaces() = flowOf(listOf(spaceEntity))
                override fun observeTasksBySpace(spaceId: String) = flowOf(listOf(taskEntity))
                override fun observeTasksByCustomList(listId: String): kotlinx.coroutines.flow.Flow<List<br.com.resetlife.data.local.environment.EnvironmentTaskEntity>> = flowOf(emptyList())
                override fun observeDiscardList(): kotlinx.coroutines.flow.Flow<List<br.com.resetlife.data.local.environment.EnvironmentTaskEntity>> = flowOf(emptyList())
                override fun observeCustomLists(): kotlinx.coroutines.flow.Flow<List<br.com.resetlife.data.local.environment.CustomListEntity>> = flowOf(emptyList())
                override suspend fun insertSpace(entity: br.com.resetlife.data.local.environment.EnvironmentSpaceEntity) {}
                override suspend fun getSpace(id: String) = null
                override suspend fun insertTask(entity: br.com.resetlife.data.local.environment.EnvironmentTaskEntity) {}
                override suspend fun updateTask(entity: br.com.resetlife.data.local.environment.EnvironmentTaskEntity) {}
                override suspend fun deleteTask(taskId: String) {}
                override suspend fun insertCustomList(entity: br.com.resetlife.data.local.environment.CustomListEntity) {}
                override suspend fun deleteCustomList(id: String) {}
                override fun observeCustomListItems(listId: String): kotlinx.coroutines.flow.Flow<List<br.com.resetlife.data.local.environment.CustomListItemEntity>> = flowOf(emptyList())
                override fun observeAllCustomListItems(): kotlinx.coroutines.flow.Flow<List<br.com.resetlife.data.local.environment.CustomListItemEntity>> = flowOf(emptyList())
                override suspend fun insertCustomListItem(entity: br.com.resetlife.data.local.environment.CustomListItemEntity) {}
                override suspend fun updateCustomListItem(entity: br.com.resetlife.data.local.environment.CustomListItemEntity) {}
                override suspend fun deleteCustomListItem(itemId: String) {}
            },
        ) {}
    }

    @Test
    fun `suggestion is exposed when there is a pending environment task`() = runTest {
        val viewModel = TodayViewModel(FakePriorityStore(), repoWithPendingTask())
        advanceUntilIdle()
        assertEquals("Limpar", viewModel.uiState.value.environmentSuggestion?.title)
    }

    @Test
    fun `suggestion is null when no spaces exist`() = runTest {
        val emptyRepo = object : EnvironmentRepository(
            object : br.com.resetlife.data.local.environment.EnvironmentDao {
                override fun observeSpaces(): kotlinx.coroutines.flow.Flow<List<br.com.resetlife.data.local.environment.EnvironmentSpaceEntity>> = flowOf(emptyList())
                override fun observeTasksBySpace(spaceId: String): kotlinx.coroutines.flow.Flow<List<br.com.resetlife.data.local.environment.EnvironmentTaskEntity>> = flowOf(emptyList())
                override fun observeTasksByCustomList(listId: String): kotlinx.coroutines.flow.Flow<List<br.com.resetlife.data.local.environment.EnvironmentTaskEntity>> = flowOf(emptyList())
                override fun observeDiscardList(): kotlinx.coroutines.flow.Flow<List<br.com.resetlife.data.local.environment.EnvironmentTaskEntity>> = flowOf(emptyList())
                override fun observeCustomLists(): kotlinx.coroutines.flow.Flow<List<br.com.resetlife.data.local.environment.CustomListEntity>> = flowOf(emptyList())
                override suspend fun insertSpace(entity: br.com.resetlife.data.local.environment.EnvironmentSpaceEntity) {}
                override suspend fun getSpace(id: String) = null
                override suspend fun insertTask(entity: br.com.resetlife.data.local.environment.EnvironmentTaskEntity) {}
                override suspend fun updateTask(entity: br.com.resetlife.data.local.environment.EnvironmentTaskEntity) {}
                override suspend fun deleteTask(taskId: String) {}
                override suspend fun insertCustomList(entity: br.com.resetlife.data.local.environment.CustomListEntity) {}
                override suspend fun deleteCustomList(id: String) {}
                override fun observeCustomListItems(listId: String): kotlinx.coroutines.flow.Flow<List<br.com.resetlife.data.local.environment.CustomListItemEntity>> = flowOf(emptyList())
                override fun observeAllCustomListItems(): kotlinx.coroutines.flow.Flow<List<br.com.resetlife.data.local.environment.CustomListItemEntity>> = flowOf(emptyList())
                override suspend fun insertCustomListItem(entity: br.com.resetlife.data.local.environment.CustomListItemEntity) {}
                override suspend fun updateCustomListItem(entity: br.com.resetlife.data.local.environment.CustomListItemEntity) {}
                override suspend fun deleteCustomListItem(itemId: String) {}
            },
        ) {}
        val viewModel = TodayViewModel(FakePriorityStore(), emptyRepo)
        advanceUntilIdle()
        assertNull(viewModel.uiState.value.environmentSuggestion)
    }
}
