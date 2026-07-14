package br.com.resetlife.presentation.theme

import kotlinx.coroutines.flow.Flow

/**
 * Gerencia a preferência de tema (Claro/Escuro/Sistema) persistida do app.
 * Recebe um [ThemePreferenceStorage] para manter a lógica testável sem Android.
 */
class ThemeManager(
    private val storage: ThemePreferenceStorage,
) {
    val themeMode: Flow<ThemeMode> = storage.themeMode

    suspend fun setThemeMode(mode: ThemeMode) = storage.setThemeMode(mode)
}
