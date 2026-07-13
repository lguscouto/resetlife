package br.com.resetlife.data.today

import br.com.resetlife.data.local.today.PriorityDao
import br.com.resetlife.data.local.today.PriorityEntity
import br.com.resetlife.domain.today.PriorityItem
import java.time.Clock
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PriorityStore {
    fun observeToday(): Flow<List<PriorityItem>>

    suspend fun add(priority: PriorityItem)

    suspend fun complete(id: String): Boolean
}

class PriorityRepository(
    private val dao: PriorityDao,
    private val clock: Clock = Clock.systemDefaultZone(),
) : PriorityStore {
    override fun observeToday(): Flow<List<PriorityItem>> =
        dao.observeByDay(todayKey()).map { entities ->
            entities.map { entity -> entity.toDomain() }
        }

    override suspend fun add(priority: PriorityItem) {
        val dayKey = todayKey()
        val nextOrder = dao.findByDay(dayKey).maxOfOrNull { it.createdAt }?.plus(1) ?: 0L
        dao.upsert(priority.toEntity(dayKey = dayKey, createdAt = nextOrder))
    }

    override suspend fun complete(id: String): Boolean {
        val dayKey = todayKey()
        val existing = dao.findByDay(dayKey).firstOrNull { it.id == id } ?: return false
        dao.upsert(existing.copy(isCompleted = true))
        return true
    }

    private fun todayKey(): String = LocalDate.now(clock).toString()
}

private fun PriorityEntity.toDomain(): PriorityItem =
    PriorityItem(
        id = id,
        title = title,
        isCompleted = isCompleted,
    )

private fun PriorityItem.toEntity(dayKey: String, createdAt: Long): PriorityEntity =
    PriorityEntity(
        id = id,
        title = title,
        isCompleted = isCompleted,
        dayKey = dayKey,
        createdAt = createdAt,
    )
