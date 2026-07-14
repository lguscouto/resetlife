package br.com.resetlife.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Today
import androidx.compose.ui.graphics.vector.ImageVector
import br.com.resetlife.R

enum class ResetLifeDestination(
    val key: String,
    @StringRes val labelRes: Int,
    @StringRes val contentDescriptionRes: Int,
    val icon: ImageVector,
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
        icon = Icons.Filled.Today,
    ),
    Organize(
        key = "organize",
        labelRes = R.string.organize_nav,
        contentDescriptionRes = R.string.organize_nav_description,
        icon = Icons.Filled.Reorder,
    ),
    Life(
        key = "life",
        labelRes = R.string.life_nav,
        contentDescriptionRes = R.string.life_nav_description,
        icon = Icons.Filled.Favorite,
    ),
    Profile(
        key = "profile",
        labelRes = R.string.profile_nav,
        contentDescriptionRes = R.string.profile_nav_description,
        icon = Icons.Filled.Person,
    ),
    Habits(
        key = "habits",
        labelRes = R.string.habits_nav,
        contentDescriptionRes = R.string.habits_nav_description,
        icon = Icons.Filled.CheckCircle,
        isBottomTab = false,
        parentTab = Life,
    ),
    Environment(
        key = "environment",
        labelRes = R.string.environment_nav,
        contentDescriptionRes = R.string.environment_nav_description,
        icon = Icons.Filled.Eco,
        isBottomTab = false,
        parentTab = Life,
    ),
    CustomLists(
        key = "custom_lists",
        labelRes = R.string.custom_lists_nav,
        contentDescriptionRes = R.string.custom_lists_nav_description,
        icon = Icons.Filled.List,
        isBottomTab = false,
        parentTab = Life,
    ),
    Wellbeing(
        key = "wellbeing",
        labelRes = R.string.wellbeing_nav,
        contentDescriptionRes = R.string.wellbeing_nav_description,
        icon = Icons.Filled.SelfImprovement,
        isBottomTab = false,
        parentTab = Life,
    ),
    WeeklyReview(
        key = "weekly_review",
        labelRes = R.string.weekly_review_nav,
        contentDescriptionRes = R.string.weekly_review_nav_description,
        icon = Icons.Filled.AutoAwesome,
        isBottomTab = false,
        parentTab = Profile,
    ),
    HabitDetail(
        key = "habit_detail",
        labelRes = R.string.habit_detail_title,
        contentDescriptionRes = R.string.habit_detail_title,
        icon = Icons.Filled.CheckCircle,
        isBottomTab = false,
    ),
    Onboarding(
        key = "onboarding",
        labelRes = R.string.onboarding_nav,
        contentDescriptionRes = R.string.onboarding_nav_desc,
        icon = Icons.Filled.Flag,
        isBottomTab = false,
    ),
}

/** Abas efetivamente exibidas na barra inferior (no máximo 4). */
val bottomTabs: List<ResetLifeDestination> =
    ResetLifeDestination.entries.filter { it.isBottomTab }

/** Devolve a aba da barra que deve aparecer selecionada para o destino atual. */
fun ResetLifeDestination.bottomTabFor(): ResetLifeDestination =
    if (isBottomTab) this else (parentTab ?: ResetLifeDestination.Today)
