package com.example.aichat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aichat.App
import com.example.aichat.data.local.SettingsStore
import com.example.aichat.domain.models.ChatSettings
import com.example.aichat.utils.Constants
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Settings.
 */
class SettingsViewModel(
    private val store: SettingsStore,
    private val onClearChats: suspend () -> Unit
) : ViewModel() {

    val settings: StateFlow<ChatSettings> = store.settings
        .stateIn(viewModelScope, SharingStarted.Lazily, ChatSettings())

    fun updateTemperature(value: Float) = viewModelScope.launch {
        store.updateTemperature(value)
    }

    fun updateTopK(value: Int) = viewModelScope.launch {
        store.updateTopK(value)
    }

    fun updateTopP(value: Float) = viewModelScope.launch {
        store.updateTopP(value)
    }

    fun updateFontSize(value: Int) = viewModelScope.launch {
        store.updateFontSize(value)
    }

    fun updateModelPath(value: String) = viewModelScope.launch {
        store.updateModelPath(value)
    }

    fun clearAllChats() = viewModelScope.launch {
        onClearChats()
    }

    fun clearCache() = viewModelScope.launch {
        store.resetAll()
    }

    companion object {
        fun factory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = App.instance
                val store = SettingsStore(app)
                return SettingsViewModel(
                    store = store,
                    onClearChats = { app.chatRepository.deleteAllChats() }
                ) as T
            }
        }
    }
}
