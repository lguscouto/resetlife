package br.com.resetlife.data.local.weeklyreview

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeeklyReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: WeeklyReviewEntity)

    @Query("SELECT * FROM weekly_review ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<WeeklyReviewEntity>>

    @Query("SELECT * FROM weekly_review WHERE periodStart = :periodStart LIMIT 1")
    suspend fun getByPeriod(periodStart: String): WeeklyReviewEntity?
}
