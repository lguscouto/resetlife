package br.com.resetlife.data.wellbeing

import br.com.resetlife.data.local.wellbeing.WellbeingCheckInDao
import br.com.resetlife.domain.wellbeing.WellbeingCheckIn
import br.com.resetlife.domain.wellbeing.toDomain
import br.com.resetlife.domain.wellbeing.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WellbeingRepository(private val dao: WellbeingCheckInDao) {
    fun observeAll(): Flow<List<WellbeingCheckIn>> = dao.observeAll().map { list ->
        list.map { it.toDomain() }
    }

    fun observeDate(date: String): Flow<WellbeingCheckIn?> =
        dao.observeDate(date).map { it?.toDomain() }

    suspend fun get(date: String): WellbeingCheckIn? = dao.get(date)?.toDomain()

    suspend fun upsert(checkin: WellbeingCheckIn) {
        dao.upsert(checkin.toEntity())
    }
}
