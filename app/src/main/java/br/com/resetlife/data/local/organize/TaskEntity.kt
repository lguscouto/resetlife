package br.com.resetlife.data.local.organize

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val note: String,
    val dueDate: String?,
    val estimatedMinutes: Int?,
    val projectId: String?,
    val status: String,
    val createdAt: Long,
)
