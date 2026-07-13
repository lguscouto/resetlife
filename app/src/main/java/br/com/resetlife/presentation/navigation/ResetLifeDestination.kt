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
        symbol = "⌂",
    ),
    Organize(
        key = "organize",
        labelRes = R.string.organize_nav,
        contentDescriptionRes = R.string.organize_nav_description,
        symbol = "✓",
    ),
    Onboarding(
        key = "onboarding",
        labelRes = R.string.today_nav, // temp
        contentDescriptionRes = R.string.today_nav_description, // temp
        symbol = "👤",
    ),
}
