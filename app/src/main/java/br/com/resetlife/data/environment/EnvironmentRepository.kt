package br.com.resetlife.data.environment

import br.com.resetlife.data.local.environment.CustomListItemEntity
import br.com.resetlife.data.local.environment.EnvironmentDao
import br.com.resetlife.domain.environment.CustomList
import br.com.resetlife.domain.environment.CustomListItem
import br.com.resetlife.domain.environment.EnvironmentSpace
import br.com.resetlife.domain.environment.EnvironmentTask
import br.com.resetlife.domain.environment.toDomain
import br.com.resetlife.domain.environment.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.UUID

open class EnvironmentRepository(private val dao: EnvironmentDao) {

    fun observeSpaces(): Flow<List<EnvironmentSpace>> =
        dao.observeSpaces().map { list -> list.map { it.toDomain() } }

    fun observeTasksBySpace(spaceId: String): Flow<List<EnvironmentTask>> =
        dao.observeTasksBySpace(spaceId).map { list -> list.map { it.toDomain() } }

    fun observeDiscardList(): Flow<List<EnvironmentTask>> =
        dao.observeDiscardList().map { list -> list.map { it.toDomain() } }

    fun observeCustomLists(): Flow<List<CustomList>> =
        dao.observeCustomLists().map { list -> list.map { it.toDomain() } }

    suspend fun addSpace(name: String): String {
        val id = UUID.randomUUID().toString()
        dao.insertSpace(EnvironmentSpace(id = id, name = name.trim()).toEntity())
        return id
    }

    suspend fun addTask(
        spaceId: String,
        title: String,
        estimatedMinutes: Int,
        discardList: Boolean = false,
        customListId: String? = null,
    ): String {
        val id = UUID.randomUUID().toString()
        dao.insertTask(
            EnvironmentTask(
                id = id,
                spaceId = spaceId,
                title = title.trim(),
                estimatedMinutes = estimatedMinutes,
                discardList = discardList,
                customListId = customListId,
            ).toEntity(),
        )
        return id
    }

    suspend fun setTaskDone(task: EnvironmentTask, done: Boolean) {
        val updated = task.copy(
            done = done,
            doneAt = if (done) LocalDate.now().toString() else null,
        )
        dao.updateTask(updated.toEntity())
        if (done) {
            val space = dao.getSpace(task.spaceId)
            if (space != null) {
                dao.insertSpace(space.copy(lastOrganizedAt = LocalDate.now().toString()))
            }
        }
    }

    suspend fun deleteTask(taskId: String) {
        dao.deleteTask(taskId)
    }

    suspend fun addCustomList(name: String): String {
        val id = UUID.randomUUID().toString()
        dao.insertCustomList(CustomList(id = id, name = name.trim()).toEntity())
        return id
    }

    suspend fun deleteCustomList(listId: String) {
        dao.deleteCustomList(listId)
    }

    fun observeCustomListItems(listId: String): Flow<List<CustomListItem>> =
        dao.observeCustomListItems(listId).map { list -> list.map { it.toDomain() } }

    fun observeAllCustomListItems(): Flow<List<CustomListItem>> =
        dao.observeAllCustomListItems().map { list -> list.map { it.toDomain() } }

    suspend fun addCustomListItem(listId: String, title: String): String {
        val id = UUID.randomUUID().toString()
        dao.insertCustomListItem(
            CustomListItem(id = id, listId = listId, title = title.trim()).toEntity(),
        )
        return id
    }

    suspend fun setCustomListItemDone(item: CustomListItem, done: Boolean) {
        dao.updateCustomListItem(item.copy(done = done).toEntity())
    }

    suspend fun deleteCustomListItem(itemId: String) {
        dao.deleteCustomListItem(itemId)
    }
}
