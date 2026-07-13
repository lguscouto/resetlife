package br.com.resetlife.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.resetlife.data.local.organize.ProjectDao
import br.com.resetlife.data.local.organize.ProjectEntity
import br.com.resetlife.data.local.organize.TaskDao
import br.com.resetlife.data.local.organize.TaskEntity
import br.com.resetlife.data.local.today.PriorityDao
import br.com.resetlife.data.local.today.PriorityEntity
import br.com.resetlife.data.local.onboarding.UserProfileDao
import br.com.resetlife.data.local.onboarding.UserProfileEntity

@Database(
    entities = [PriorityEntity::class, ProjectEntity::class, TaskEntity::class, UserProfileEntity::class],
    version = 3,
    autoMigrations = [AutoMigration(from = 2, to = 3)],
    exportSchema = true,
)
abstract class ResetLifeDatabase : RoomDatabase() {
    abstract fun priorityDao(): PriorityDao

    abstract fun projectDao(): ProjectDao

    abstract fun taskDao(): TaskDao

    abstract fun userProfileDao(): UserProfileDao
}
