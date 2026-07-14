package br.com.resetlife.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.resetlife.data.local.onboarding.UserProfileDao
import br.com.resetlife.data.local.onboarding.UserProfileEntity
import br.com.resetlife.data.local.organize.ProjectDao
import br.com.resetlife.data.local.organize.ProjectEntity
import br.com.resetlife.data.local.organize.TaskDao
import br.com.resetlife.data.local.organize.TaskEntity
import br.com.resetlife.data.local.today.PriorityDao
import br.com.resetlife.data.local.today.PriorityEntity
import br.com.resetlife.data.local.wellbeing.WellbeingCheckInDao
import br.com.resetlife.data.local.wellbeing.WellbeingCheckInEntity
import br.com.resetlife.data.local.weeklyreview.WeeklyReviewDao
import br.com.resetlife.data.local.weeklyreview.WeeklyReviewEntity
import br.com.resetlife.data.local.habit.HabitDao
import br.com.resetlife.data.local.habit.HabitEntity
import br.com.resetlife.data.local.habit.HabitLogEntity
import br.com.resetlife.data.local.environment.EnvironmentDao
import br.com.resetlife.data.local.environment.EnvironmentSpaceEntity
import br.com.resetlife.data.local.environment.EnvironmentTaskEntity
import br.com.resetlife.data.local.environment.CustomListEntity

@Database(
    entities = [
        PriorityEntity::class,
        ProjectEntity::class,
        TaskEntity::class,
        UserProfileEntity::class,
        WellbeingCheckInEntity::class,
        WeeklyReviewEntity::class,
        HabitEntity::class,
        HabitLogEntity::class,
        EnvironmentSpaceEntity::class,
        EnvironmentTaskEntity::class,
        CustomListEntity::class,
    ],
    version = 7,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
    ],
    exportSchema = true,
)
abstract class ResetLifeDatabase : RoomDatabase() {
    abstract fun priorityDao(): PriorityDao
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun wellbeingCheckInDao(): WellbeingCheckInDao
    abstract fun weeklyReviewDao(): WeeklyReviewDao
    abstract fun habitDao(): HabitDao
    abstract fun environmentDao(): EnvironmentDao
}
