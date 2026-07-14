package br.com.resetlife.data.local.habit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habit WHERE id = :id")
    suspend fun get(id: String): HabitEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: HabitEntity)

    @Update
    suspend fun update(habit: HabitEntity)

    @Query("SELECT * FROM habit_log WHERE habitId = :habitId AND date = :date")
    suspend fun getLog(habitId: String, date: String): HabitLogEntity?

    @Query("SELECT * FROM habit_log WHERE habitId = :habitId ORDER BY date DESC")
    fun observeLogsByHabit(habitId: String): Flow<List<HabitLogEntity>>

    @Query("SELECT * FROM habit_log WHERE date = :date")
    fun observeLogsByDate(date: String): Flow<List<HabitLogEntity>>

    @Query("SELECT * FROM habit_log WHERE date = :date")
    suspend fun getLogsByDate(date: String): List<HabitLogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLog(log: HabitLogEntity)

    @Query("DELETE FROM habit_log WHERE habitId = :habitId AND date = :date")
    suspend fun deleteLog(habitId: String, date: String)

    @Transaction
    suspend fun logForDate(habitId: String, date: String): HabitLogEntity? =
        getLog(habitId, date)
}
