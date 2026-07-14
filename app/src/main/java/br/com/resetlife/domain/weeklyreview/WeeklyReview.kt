package br.com.resetlife.domain.weeklyreview

import br.com.resetlife.data.local.weeklyreview.WeeklyReviewEntity

data class WeeklyReview(
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

fun WeeklyReviewEntity.toDomain(): WeeklyReview = WeeklyReview(
    id = id,
    periodStart = periodStart,
    periodEnd = periodEnd,
    completed = completed,
    pending = pending,
    difficulty = difficulty,
    nextWeekPriorities = nextWeekPriorities,
    habitToAdjust = habitToAdjust,
    createdAt = createdAt,
)

fun WeeklyReview.toEntity(): WeeklyReviewEntity = WeeklyReviewEntity(
    id = id,
    periodStart = periodStart,
    periodEnd = periodEnd,
    completed = completed,
    pending = pending,
    difficulty = difficulty,
    nextWeekPriorities = nextWeekPriorities,
    habitToAdjust = habitToAdjust,
    createdAt = createdAt,
)
