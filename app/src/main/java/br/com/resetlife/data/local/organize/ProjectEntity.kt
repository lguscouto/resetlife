package br.com.resetlife.data.local.organize

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val title: String,
    val goal: String,
    val createdAt: Long,
)
