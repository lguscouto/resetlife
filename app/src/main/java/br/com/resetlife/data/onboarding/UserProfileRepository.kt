package br.com.resetlife.data.onboarding

import br.com.resetlife.data.local.onboarding.UserProfileDao
import br.com.resetlife.data.local.onboarding.UserProfileEntity
import br.com.resetlife.domain.onboarding.LifeArea
import br.com.resetlife.domain.onboarding.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserProfileRepository(private val dao: UserProfileDao) {
    fun observe(): Flow<UserProfile?> = dao.observe().map { it?.toDomain() }

    suspend fun upsert(profile: UserProfile) {
        dao.upsert(profile.toEntity())
    }

    suspend fun completeOnboarding(
        areas: List<LifeArea>,
        durationDays: Int,
        dailyMinutes: Int,
    ) {
        val profile = UserProfile(
            onboardingCompleted = true,
            resetPlanStart = System.currentTimeMillis(),
            resetPlanDurationDays = durationDays,
            lifeAreas = areas,
            dailyAvailabilityMinutes = dailyMinutes,
        )
        upsert(profile)
    }
}

fun UserProfile.toEntity() = UserProfileEntity(
    onboardingCompleted = this.onboardingCompleted,
    resetPlanStart = this.resetPlanStart,
    resetPlanDurationDays = this.resetPlanDurationDays,
    lifeAreasJson = this.lifeAreas.joinToString(",") { it.name },
    dailyAvailabilityMinutes = this.dailyAvailabilityMinutes,
)

fun UserProfileEntity.toDomain() = UserProfile(
    onboardingCompleted = this.onboardingCompleted,
    resetPlanStart = this.resetPlanStart,
    resetPlanDurationDays = this.resetPlanDurationDays,
    lifeAreas = this.lifeAreasJson?.split(",")?.mapNotNull { 
        runCatching { LifeArea.valueOf(it) }.getOrNull() 
    }?.toList() ?: emptyList(),
    dailyAvailabilityMinutes = this.dailyAvailabilityMinutes,
)