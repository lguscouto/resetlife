package br.com.resetlife.presentation.theme

import kotlinx.coroutines.flow.Flow

/**
 * Armazenamento da preferência de tema do app, desacoplado do Android/DataStore
 * para permitir testes de unidade sem emulador.
 */
interface ThemePreferenceStorage {
    val themeMode: Flow<ThemeMode>
    suspend fun setThemeMode(mode: ThemeMode)
}
