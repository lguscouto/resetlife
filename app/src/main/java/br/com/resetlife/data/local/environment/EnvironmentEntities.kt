package br.com.resetlife.data.local.environment

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "environment_space")
data class EnvironmentSpaceEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val lastOrganizedAt: String? = null,
)

@Entity(tableName = "environment_task")
data class EnvironmentTaskEntity(
    @PrimaryKey
    val id: String,
    val spaceId: String,
    val title: String,
    val estimatedMinutes: Int,
    val done: Boolean = false,
    val doneAt: String? = null,
    val discardList: Boolean = false,
    val customListId: String? = null,
)

@Entity(tableName = "custom_list")
data class CustomListEntity(
    @PrimaryKey
    val id: String,
    val name: String,
)

@Entity(
    tableName = "custom_list_item",
    foreignKeys = [
        ForeignKey(
            entity = CustomListEntity::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class CustomListItemEntity(
    @PrimaryKey
    val id: String,
    val listId: String,
    val title: String,
    val done: Boolean = false,
)
