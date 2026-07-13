package br.com.resetlife.data.local.today

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "priorities")
data class PriorityEntity(
    @PrimaryKey val id: String,
    val title: String,
    val isCompleted: Boolean,
    val dayKey: String,
    val createdAt: Long,
)
