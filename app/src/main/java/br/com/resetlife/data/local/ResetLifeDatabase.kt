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

@Database(
    entities = [PriorityEntity::class, ProjectEntity::class, TaskEntity::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)],
    exportSchema = true,
)
abstract class ResetLifeDatabase : RoomDatabase() {
    abstract fun priorityDao(): PriorityDao

    abstract fun projectDao(): ProjectDao

    abstract fun taskDao(): TaskDao
}
