package com.example.aichat.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.aichat.domain.models.ChatSettings
import com.example.aichat.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "liz_settings")

/**
 * Persistencia ligera de settings de usuario usando DataStore.
 */
class SettingsStore(private val context: Context) {

    private object Keys {
        val TEMPERATURE = floatPreferencesKey("temperature")
        val TOP_K = intPreferencesKey("top_k")
        val TOP_P = floatPreferencesKey("top_p")
        val FONT_SIZE = intPreferencesKey("font_size")
        val MODEL_PATH = stringPreferencesKey("model_path")
    }

    val settings: Flow<ChatSettings> = context.settingsDataStore.data.map { prefs ->
        ChatSettings(
            temperature = prefs[Keys.TEMPERATURE] ?: Constants.DEFAULT_TEMPERATURE,
            topK = prefs[Keys.TOP_K] ?: Constants.DEFAULT_TOP_K,
            topP = prefs[Keys.TOP_P] ?: Constants.DEFAULT_TOP_P,
            fontSize = prefs[Keys.FONT_SIZE] ?: Constants.DEFAULT_FONT_SIZE,
            modelPath = prefs[Keys.MODEL_PATH] ?: Constants.DEFAULT_MODEL_PATH
        )
    }

    suspend fun updateTemperature(value: Float) {
        context.settingsDataStore.edit { it[Keys.TEMPERATURE] = value }
    }

    suspend fun updateTopK(value: Int) {
        context.settingsDataStore.edit { it[Keys.TOP_K] = value }
    }

    suspend fun updateTopP(value: Float) {
        context.settingsDataStore.edit { it[Keys.TOP_P] = value }
    }

    suspend fun updateFontSize(value: Int) {
        context.settingsDataStore.edit { it[Keys.FONT_SIZE] = value }
    }

    suspend fun updateModelPath(value: String) {
        context.settingsDataStore.edit { it[Keys.MODEL_PATH] = value }
    }

    suspend fun resetAll() {
        context.settingsDataStore.edit { it.clear() }
    }
}
