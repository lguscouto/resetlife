package br.com.resetlife.presentation.navigation

import androidx.annotation.StringRes
import br.com.resetlife.R

enum class ResetLifeDestination(
    val key: String,
    @StringRes val labelRes: Int,
    @StringRes val contentDescriptionRes: Int,
    val symbol: String,
    /**
     * Quando este destino é uma aba da barra inferior.
     * Destinos que são filhos de um hub (Habits, Wellbeing, WeeklyReview)
     * apontam para a aba pai correspondente.
     */
    val isBottomTab: Boolean = true,
    val parentTab: ResetLifeDestination? = null,
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
    Life(
        key = "life",
        labelRes = R.string.life_nav,
        contentDescriptionRes = R.string.life_nav_description,
        symbol = "♥",
    ),
    Profile(
        key = "profile",
        labelRes = R.string.profile_nav,
        contentDescriptionRes = R.string.profile_nav_description,
        symbol = "◔",
    ),
    Habits(
        key = "habits",
        labelRes = R.string.habits_nav,
        contentDescriptionRes = R.string.habits_nav_description,
        symbol = "✓",
        isBottomTab = false,
        parentTab = Life,
    ),
    Wellbeing(
        key = "wellbeing",
        labelRes = R.string.wellbeing_nav,
        contentDescriptionRes = R.string.wellbeing_nav_description,
        symbol = "❤",
        isBottomTab = false,
        parentTab = Life,
    ),
    WeeklyReview(
        key = "weekly_review",
        labelRes = R.string.weekly_review_nav,
        contentDescriptionRes = R.string.weekly_review_nav_description,
        symbol = "☷",
        isBottomTab = false,
        parentTab = Profile,
    ),
    Onboarding(
        key = "onboarding",
        labelRes = R.string.onboarding_nav,
        contentDescriptionRes = R.string.onboarding_nav_desc,
        symbol = "◔",
        isBottomTab = false,
    ),
}

/** Abas efetivamente exibidas na barra inferior (no máximo 4). */
val bottomTabs: List<ResetLifeDestination> =
    ResetLifeDestination.entries.filter { it.isBottomTab }

/** Devolve a aba da barra que deve aparecer selecionada para o destino atual. */
fun ResetLifeDestination.bottomTabFor(): ResetLifeDestination =
    if (isBottomTab) this else (parentTab ?: ResetLifeDestination.Today)
