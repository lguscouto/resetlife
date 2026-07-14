package br.com.resetlife.presentation.navigation

import androidx.annotation.StringRes
import br.com.resetlife.R

enum class ResetLifeDestination(
    val key: String,
    @StringRes val labelRes: Int,
    @StringRes val contentDescriptionRes: Int,
    val symbol: String,
) {
    Today(
        key = "today",
        labelRes = R.string.today_nav,
        contentDescriptionRes = R.string.today_nav_description,
        symbol = "☀",
    ),
    Organize(
        key = "organize",
        labelRes = R.string.organize_nav,
        contentDescriptionRes = R.string.organize_nav_description,
        symbol = "▤",
    ),
    Habits(
        key = "habits",
        labelRes = R.string.habits_nav,
        contentDescriptionRes = R.string.habits_nav_description,
        symbol = "✓",
    ),
    WeeklyReview(
        key = "weekly_review",
        labelRes = R.string.weekly_review_nav,
        contentDescriptionRes = R.string.weekly_review_nav_description,
        symbol = "☷",
    ),
    Onboarding(
        key = "onboarding",
        labelRes = R.string.onboarding_nav,
        contentDescriptionRes = R.string.onboarding_nav_desc,
        symbol = "◔",
    ),
    Wellbeing(
        key = "wellbeing",
        labelRes = R.string.wellbeing_nav,
        contentDescriptionRes = R.string.wellbeing_nav_description,
        symbol = "♥",
    ),
}
