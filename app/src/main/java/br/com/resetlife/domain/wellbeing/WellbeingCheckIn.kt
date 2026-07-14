package br.com.resetlife.domain.wellbeing

data class WellbeingCheckIn(
    val date: String,
    val mood: Int,
    val energy: Int,
    val stress: Int,
    val sleepPerceived: Int,
    val note: String? = null,
)

fun WellbeingCheckIn.toEntity() = br.com.resetlife.data.local.wellbeing.WellbeingCheckInEntity(
    date = date,
    mood = mood,
    energy = energy,
    stress = stress,
    sleepPerceived = sleepPerceived,
    note = note,
)

fun br.com.resetlife.data.local.wellbeing.WellbeingCheckInEntity.toDomain() = WellbeingCheckIn(
    date = date,
    mood = mood,
    energy = energy,
    stress = stress,
    sleepPerceived = sleepPerceived,
    note = note,
)