package br.com.resetlife.domain.environment

import br.com.resetlife.data.local.environment.CustomListEntity
import br.com.resetlife.data.local.environment.EnvironmentSpaceEntity
import br.com.resetlife.data.local.environment.EnvironmentTaskEntity

fun EnvironmentSpaceEntity.toDomain(): EnvironmentSpace = EnvironmentSpace(
    id = id,
    name = name,
    lastOrganizedAt = lastOrganizedAt,
)

fun EnvironmentSpace.toEntity(): EnvironmentSpaceEntity = EnvironmentSpaceEntity(
    id = id,
    name = name,
    lastOrganizedAt = lastOrganizedAt,
)

fun EnvironmentTaskEntity.toDomain(): EnvironmentTask = EnvironmentTask(
    id = id,
    spaceId = spaceId,
    title = title,
    estimatedMinutes = estimatedMinutes,
    done = done,
    doneAt = doneAt,
    discardList = discardList,
    customListId = customListId,
)

fun EnvironmentTask.toEntity(): EnvironmentTaskEntity = EnvironmentTaskEntity(
    id = id,
    spaceId = spaceId,
    title = title,
    estimatedMinutes = estimatedMinutes,
    done = done,
    doneAt = doneAt,
    discardList = discardList,
    customListId = customListId,
)

fun CustomListEntity.toDomain(): CustomList = CustomList(
    id = id,
    name = name,
)

fun CustomList.toEntity(): CustomListEntity = CustomListEntity(
    id = id,
    name = name,
)
