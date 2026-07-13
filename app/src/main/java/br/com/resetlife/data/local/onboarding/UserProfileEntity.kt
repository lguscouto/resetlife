package br.com.resetlife.data.local.onboarding

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String = "default",
    val onboardingCompleted: Boolean = false,
    val resetPlanStart: Long? = null,
    val resetPlanDurationDays: Int? = null,
    val lifeAreasJson: String? = null, // JSON array of prioritized areas
    val dailyAvailabilityMinutes: Int? = null,
)