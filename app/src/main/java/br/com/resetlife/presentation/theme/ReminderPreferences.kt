package br.com.resetlife.presentation.theme

import kotlinx.coroutines.flow.Flow

/**
 * Preferências de lembrete local (notificação diária), desacopladas do Android/DataStore
 * para permitir testes de unidade sem emulador.
 */
interface ReminderPreferences {
    val enabled: Flow<Boolean>
    val hour: Flow<Int>
    val minute: Flow<Int>

    suspend fun setEnabled(value: Boolean)
    suspend fun setTime(hour: Int, minute: Int)
}
