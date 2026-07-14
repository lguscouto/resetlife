package br.com.resetlife.presentation.theme

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.pressureDataStore by preferencesDataStore(name = "resetlife_pressure")

private val RELAXED_MODE_KEY = booleanPreferencesKey("relaxed_mode")

class DataStorePressurePreferences(
    private val context: Context,
) : PressurePreferences {

    override val relaxedMode: Flow<Boolean> = context.pressureDataStore.data.map { prefs ->
        prefs[RELAXED_MODE_KEY] ?: false
    }

    override suspend fun setRelaxedMode(enabled: Boolean) {
        context.pressureDataStore.edit { prefs ->
            prefs[RELAXED_MODE_KEY] = enabled
        }
    }
}
