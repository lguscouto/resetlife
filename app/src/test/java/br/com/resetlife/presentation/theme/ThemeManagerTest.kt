package br.com.resetlife.presentation.theme

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class InMemoryThemePreferenceStorage : ThemePreferenceStorage {
    private val state = MutableStateFlow(ThemeMode.SYSTEM)
    override val themeMode = state
    override suspend fun setThemeMode(mode: ThemeMode) {
        state.value = mode
    }
}

class ThemeManagerTest {

    @Test
    fun `exposes system as default theme mode`() = runTest {
        val manager = ThemeManager(InMemoryThemePreferenceStorage())
        assertEquals(ThemeMode.SYSTEM, manager.themeMode.first())
    }

    @Test
    fun `persists light theme mode`() = runTest {
        val manager = ThemeManager(InMemoryThemePreferenceStorage())
        manager.setThemeMode(ThemeMode.LIGHT)
        assertEquals(ThemeMode.LIGHT, manager.themeMode.first())
    }

    @Test
    fun `persists dark theme mode`() = runTest {
        val manager = ThemeManager(InMemoryThemePreferenceStorage())
        manager.setThemeMode(ThemeMode.DARK)
        assertEquals(ThemeMode.DARK, manager.themeMode.first())
    }

    @Test
    fun `persists system theme mode after change`() = runTest {
        val manager = ThemeManager(InMemoryThemePreferenceStorage())
        manager.setThemeMode(ThemeMode.DARK)
        manager.setThemeMode(ThemeMode.SYSTEM)
        assertEquals(ThemeMode.SYSTEM, manager.themeMode.first())
    }
}
