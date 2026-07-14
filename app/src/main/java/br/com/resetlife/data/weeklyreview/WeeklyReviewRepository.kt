package br.com.resetlife.data.weeklyreview

import br.com.resetlife.data.local.weeklyreview.WeeklyReviewDao
import br.com.resetlife.domain.weeklyreview.WeeklyReview
import br.com.resetlife.domain.weeklyreview.toDomain
import br.com.resetlife.domain.weeklyreview.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface WeeklyReviewStore {
    fun observeReviews(): Flow<List<WeeklyReview>>
    suspend fun getByPeriod(periodStart: String): WeeklyReview?
    suspend fun upsert(review: WeeklyReview)
}

class WeeklyReviewRepository(
    private val dao: WeeklyReviewDao,
) : WeeklyReviewStore {
    override fun observeReviews(): Flow<List<WeeklyReview>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getByPeriod(periodStart: String): WeeklyReview? =
        dao.getByPeriod(periodStart)?.toDomain()

    override suspend fun upsert(review: WeeklyReview) {
        dao.upsert(review.toEntity())
    }
}
