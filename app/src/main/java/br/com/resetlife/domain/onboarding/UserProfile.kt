package br.com.resetlife.domain.onboarding

import kotlin.time.Duration.Companion.days

data class UserProfile(
    val onboardingCompleted: Boolean = false,
    val resetPlanStart: Long? = null,
    val resetPlanDurationDays: Int? = null,
    val lifeAreas: List<LifeArea> = emptyList(),
    val dailyAvailabilityMinutes: Int? = null,
)

enum class LifeArea {
    Health, // Saúde física
    Tasks, // Tarefas/domínio
    Home, // Organização do ambiente
    Finances, // Finanças pessoais
    Relationships, // Relacionamentos
    Mindset, // Mentalidade/foco
    Energy, // Energia/bem-estar
}