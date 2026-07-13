package br.com.resetlife.data.local.organize

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY createdAt ASC")
    fun observeAll(): Flow<List<ProjectEntity>>

    @Upsert
    suspend fun upsert(project: ProjectEntity)
}
