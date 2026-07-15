package br.com.resetlife.presentation.theme

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

/**
 * DataStore único e compartilhado do app (preferências diversas: tema, lembretes, etc).
 * Declarado uma única vez para evitar múltiplas instâncias ativas do mesmo arquivo
 * (que causaria IllegalStateException ao abrir telas que leem mais de uma preferência).
 */
val Context.dataStore by preferencesDataStore(name = "resetlife_settings")
