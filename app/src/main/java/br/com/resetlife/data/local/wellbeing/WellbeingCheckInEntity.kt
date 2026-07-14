package br.com.resetlife.data.local.wellbeing

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wellbeing_checkin")
data class WellbeingCheckInEntity(
    @PrimaryKey val date: String, // ISO date "2024-01-15"
    val mood: Int, // 1-5
    val energy: Int, // 1-5
    val stress: Int, // 1-5
    val sleepPerceived: Int, // 1-5
    val note: String? = null,
)