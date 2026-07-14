package br.com.resetlife.data.local.weeklyreview

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weekly_review")
data class WeeklyReviewEntity(
    @PrimaryKey
    val id: String,
    val periodStart: String,
    val periodEnd: String,
    val completed: String,
    val pending: String,
    val difficulty: String,
    val nextWeekPriorities: String,
    val habitToAdjust: String,
    val createdAt: Long,
)
