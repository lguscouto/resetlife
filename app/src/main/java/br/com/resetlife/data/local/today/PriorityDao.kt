package br.com.resetlife.data.local.today

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PriorityDao {
    @Query("SELECT * FROM priorities WHERE dayKey = :dayKey ORDER BY createdAt ASC")
    fun observeByDay(dayKey: String): Flow<List<PriorityEntity>>

    @Query("SELECT * FROM priorities WHERE dayKey = :dayKey ORDER BY createdAt ASC")
    suspend fun findByDay(dayKey: String): List<PriorityEntity>

    @Upsert
    suspend fun upsert(priority: PriorityEntity)
}
