package br.com.resetlife.data.local.wellbeing

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface WellbeingCheckInDao {
    @Query("SELECT * FROM wellbeing_checkin WHERE date = :date")
    suspend fun get(date: String): WellbeingCheckInEntity?

    @Query("SELECT * FROM wellbeing_checkin WHERE date = :date")
    fun observeDate(date: String): Flow<WellbeingCheckInEntity?>

    @Query("SELECT * FROM wellbeing_checkin ORDER BY date DESC")
    fun observeAll(): Flow<List<WellbeingCheckInEntity>>

    @Upsert
    suspend fun upsert(checkin: WellbeingCheckInEntity)
}
