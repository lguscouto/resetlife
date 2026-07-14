package br.com.resetlife

import android.app.Application
import androidx.room.Room
import br.com.resetlife.data.local.ResetLifeDatabase
import br.com.resetlife.data.organize.OrganizeRepository
import br.com.resetlife.data.today.PriorityRepository
import br.com.resetlife.data.onboarding.UserProfileRepository
import br.com.resetlife.data.wellbeing.WellbeingRepository
import br.com.resetlife.data.weeklyreview.WeeklyReviewRepository
import br.com.resetlife.data.habit.HabitRepository
import br.com.resetlife.data.environment.EnvironmentRepository
import br.com.resetlife.presentation.theme.DataStoreThemePreferenceStorage
import br.com.resetlife.presentation.theme.DataStoreReminderPreferences
import br.com.resetlife.presentation.theme.ReminderPreferences
import br.com.resetlife.presentation.theme.ThemeManager

class ResetLifeApplication : Application() {
    private val database by lazy {
        Room.databaseBuilder(
            this,
            ResetLifeDatabase::class.java,
            "resetlife.db",
        ).build()
    }

    val priorityStore by lazy { PriorityRepository(database.priorityDao()) }

    val organizeStore by lazy {
        OrganizeRepository(database.projectDao(), database.taskDao())
    }

    val userProfileStore by lazy {
        UserProfileRepository(database.userProfileDao())
    }

    val wellbeingStore by lazy {
        WellbeingRepository(database.wellbeingCheckInDao())
    }

    val weeklyReviewStore by lazy {
        WeeklyReviewRepository(database.weeklyReviewDao())
    }

    val habitStore by lazy {
        HabitRepository(database.habitDao())
    }

    val environmentStore by lazy {
        EnvironmentRepository(database.environmentDao())
    }

    val themeManager by lazy {
        ThemeManager(DataStoreThemePreferenceStorage(this))
    }

    val reminderPreferences: ReminderPreferences by lazy {
        DataStoreReminderPreferences(this)
    }
}
