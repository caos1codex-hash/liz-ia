package com.example.aichat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aichat.App
import com.example.aichat.domain.models.Chat
import com.example.aichat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla Home (lista de chats).
 */
class HomeViewModel(
    private val repository: ChatRepository
) : ViewModel() {

    val chats: StateFlow<List<Chat>> = repository.observeChats()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun createNewChat(onCreated: (Long) -> Unit) {
        viewModelScope.launch {
            val id = repository.createChat("Nuevo chat")
            onCreated(id)
        }
    }

    fun deleteChat(chatId: Long) {
        viewModelScope.launch {
            repository.deleteChat(chatId)
        }
    }

    companion object {
        fun factory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(App.instance.chatRepository) as T
            }
        }
    }
}
