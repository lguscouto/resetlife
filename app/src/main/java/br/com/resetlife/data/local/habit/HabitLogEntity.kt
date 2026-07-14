package br.com.resetlife.data.local.habit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit_log")
data class HabitLogEntity(
    @PrimaryKey
    val id: String,
    val habitId: String,
    val date: String, // YYYY-MM-DD
    val value: Int,
    val done: Boolean,
)
