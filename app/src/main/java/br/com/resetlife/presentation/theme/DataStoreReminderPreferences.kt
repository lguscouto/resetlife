package br.com.resetlife.presentation.theme

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val REMINDER_ENABLED_KEY = booleanPreferencesKey("reminder_enabled")
private val REMINDER_HOUR_KEY = intPreferencesKey("reminder_hour")
private val REMINDER_MINUTE_KEY = intPreferencesKey("reminder_minute")

class DataStoreReminderPreferences(
    private val context: Context,
) : ReminderPreferences {

    override val enabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[REMINDER_ENABLED_KEY] ?: false
    }

    override val hour: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[REMINDER_HOUR_KEY] ?: 20
    }

    override val minute: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[REMINDER_MINUTE_KEY] ?: 0
    }

    override suspend fun setEnabled(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[REMINDER_ENABLED_KEY] = value
        }
    }

    override suspend fun setTime(hour: Int, minute: Int) {
        context.dataStore.edit { prefs ->
            prefs[REMINDER_HOUR_KEY] = hour
            prefs[REMINDER_MINUTE_KEY] = minute
        }
    }
}
