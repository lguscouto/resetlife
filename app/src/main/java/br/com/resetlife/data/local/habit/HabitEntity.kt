package br.com.resetlife.data.local.habit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit")
data class HabitEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val frequency: String, // DAILY | WEEKDAYS
    val goalType: String, // BINARY | QUANTITY
    val targetValue: Int?,
    val unit: String?,
    val active: Boolean,
    val paused: Boolean,
    val createdAt: String,
    val colorHex: String? = null, // cor opcional (#RRGGBB); null = cor padrão do tema
    @ColumnInfo(defaultValue = "HABIT")
    val type: String = "HABIT", // "HABIT" | "AVOID"
)
