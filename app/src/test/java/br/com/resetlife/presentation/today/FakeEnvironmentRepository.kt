package br.com.resetlife.presentation.today

import br.com.resetlife.data.environment.EnvironmentRepository
import br.com.resetlife.domain.environment.EnvironmentSpace
import br.com.resetlife.domain.environment.EnvironmentTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeEnvironmentRepository : EnvironmentRepository(
    object : br.com.resetlife.data.local.environment.EnvironmentDao {
        override fun observeSpaces(): Flow<List<br.com.resetlife.data.local.environment.EnvironmentSpaceEntity>> =
            flowOf(emptyList())
        override fun observeTasksBySpace(spaceId: String): Flow<List<br.com.resetlife.data.local.environment.EnvironmentTaskEntity>> =
            flowOf(emptyList())
        override fun observeTasksByCustomList(listId: String): Flow<List<br.com.resetlife.data.local.environment.EnvironmentTaskEntity>> =
            flowOf(emptyList())
        override fun observeDiscardList(): Flow<List<br.com.resetlife.data.local.environment.EnvironmentTaskEntity>> =
            flowOf(emptyList())
        override fun observeCustomLists(): Flow<List<br.com.resetlife.data.local.environment.CustomListEntity>> =
            flowOf(emptyList())
        override suspend fun insertSpace(entity: br.com.resetlife.data.local.environment.EnvironmentSpaceEntity) {}
        override suspend fun getSpace(id: String): br.com.resetlife.data.local.environment.EnvironmentSpaceEntity? = null
        override suspend fun insertTask(entity: br.com.resetlife.data.local.environment.EnvironmentTaskEntity) {}
        override suspend fun updateTask(entity: br.com.resetlife.data.local.environment.EnvironmentTaskEntity) {}
        override suspend fun deleteTask(taskId: String) {}
        override suspend fun insertCustomList(entity: br.com.resetlife.data.local.environment.CustomListEntity) {}
        override suspend fun deleteCustomList(id: String) {}
        override fun observeCustomListItems(listId: String): Flow<List<br.com.resetlife.data.local.environment.CustomListItemEntity>> =
            flowOf(emptyList())
        override suspend fun insertCustomListItem(entity: br.com.resetlife.data.local.environment.CustomListItemEntity) {}
        override suspend fun updateCustomListItem(entity: br.com.resetlife.data.local.environment.CustomListItemEntity) {}
        override suspend fun deleteCustomListItem(itemId: String) {}
    },
) {
    val completedTaskIds = mutableListOf<String>()
}
