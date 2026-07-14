package br.com.resetlife.presentation.theme

import kotlinx.coroutines.flow.Flow

/**
 * Preferências do modo "sem pressão" de hábitos (estilo Finch), desacopladas do
 * Android/DataStore para permitir testes de unidade sem emulador.
 *
 * Quando [relaxedMode] está ativo, a UI oculta a contagem de sequência (streak)
 * e exibe uma mensagem gentil, evitando linguagem de pressão ou fracasso.
 */
interface PressurePreferences {
    val relaxedMode: Flow<Boolean>

    suspend fun setRelaxedMode(enabled: Boolean)
}
