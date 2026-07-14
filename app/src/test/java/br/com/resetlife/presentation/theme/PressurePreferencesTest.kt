package br.com.resetlife.presentation.theme

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Implementação in-memory de [PressurePreferences] para testar o contrato sem Android.
 */
private class InMemoryPressurePreferences : PressurePreferences {
    private val _relaxedMode = MutableStateFlow(false)
    override val relaxedMode: Flow<Boolean> = _relaxedMode.asStateFlow()

    override suspend fun setRelaxedMode(enabled: Boolean) {
        _relaxedMode.value = enabled
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class PressurePreferencesTest {

    @Test
    fun `relaxed mode defaults to false`() = runTest {
        val prefs = InMemoryPressurePreferences()

        assertFalse(prefs.relaxedMode.first())
    }

    @Test
    fun `setRelaxedMode true emits true`() = runTest {
        val prefs = InMemoryPressurePreferences()

        prefs.setRelaxedMode(true)

        assertTrue(prefs.relaxedMode.first())
    }

    @Test
    fun `setRelaxedMode can be toggled back to false`() = runTest {
        val prefs = InMemoryPressurePreferences()

        prefs.setRelaxedMode(true)
        prefs.setRelaxedMode(false)

        assertEquals(false, prefs.relaxedMode.first())
    }
}
