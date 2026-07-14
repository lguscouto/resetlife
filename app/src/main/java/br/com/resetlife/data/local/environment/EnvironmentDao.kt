package br.com.resetlife.data.local.environment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.resetlife.data.local.environment.CustomListItemEntity
import br.com.resetlife.data.local.environment.CustomListEntity
import br.com.resetlife.data.local.environment.EnvironmentSpaceEntity
import br.com.resetlife.data.local.environment.EnvironmentTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EnvironmentDao {
    @Query("SELECT * FROM environment_space ORDER BY name")
    fun observeSpaces(): Flow<List<EnvironmentSpaceEntity>>

    @Query("SELECT * FROM environment_space WHERE id = :id")
    suspend fun getSpace(id: String): EnvironmentSpaceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpace(space: EnvironmentSpaceEntity)

    @Query("SELECT * FROM environment_task WHERE spaceId = :spaceId ORDER BY estimatedMinutes, title")
    fun observeTasksBySpace(spaceId: String): Flow<List<EnvironmentTaskEntity>>

    @Query("SELECT * FROM environment_task WHERE discardList = 1 ORDER BY title")
    fun observeDiscardList(): Flow<List<EnvironmentTaskEntity>>

    @Query("SELECT * FROM environment_task WHERE customListId = :listId ORDER BY title")
    fun observeTasksByCustomList(listId: String): Flow<List<EnvironmentTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: EnvironmentTaskEntity)

    @Update
    suspend fun updateTask(task: EnvironmentTaskEntity)

    @Query("DELETE FROM environment_task WHERE id = :id")
    suspend fun deleteTask(id: String)

    @Query("SELECT * FROM custom_list ORDER BY name")
    fun observeCustomLists(): Flow<List<CustomListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomList(list: CustomListEntity)

    @Query("DELETE FROM custom_list WHERE id = :id")
    suspend fun deleteCustomList(id: String)

    @Query("SELECT * FROM custom_list_item WHERE listId = :listId ORDER BY title")
    fun observeCustomListItems(listId: String): Flow<List<CustomListItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomListItem(item: CustomListItemEntity)

    @Update
    suspend fun updateCustomListItem(item: CustomListItemEntity)

    @Query("DELETE FROM custom_list_item WHERE id = :id")
    suspend fun deleteCustomListItem(id: String)
}
