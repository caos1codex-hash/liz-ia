package com.example.aichat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aichat.App
import com.example.aichat.ai.ChatEngine
import com.example.aichat.domain.models.Chat
import com.example.aichat.domain.models.ChatSettings
import com.example.aichat.domain.models.Message
import com.example.aichat.domain.repository.ChatRepository
import com.example.aichat.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Chat.
 *
 * Responsabilidades:
 *  - Cargar / crear el chat activo.
 *  - Observar mensajes del chat.
 *  - Enviar mensaje de usuario y generar respuesta streaming.
 *  - Copiar / borrar / regenerar mensajes.
 */
class ChatViewModel(
    private val repository: ChatRepository,
    private val engine: ChatEngine,
    private val settingsFlow: StateFlow<ChatSettings>
) : ViewModel() {

    private val _chat = MutableStateFlow<Chat?>(null)
    val chat: StateFlow<Chat?> = _chat.asStateFlow()

    private var currentChatId: Long = Constants.NEW_CHAT_ID

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _toast = MutableStateFlow<String?>(null)
    val toast: StateFlow<String?> = _toast.asStateFlow()

    /**
     * Inicializa el chat: si chatId == NEW_CHAT_ID crea uno nuevo.
     * Si ya existe, lo carga y observa sus mensajes.
     */
    fun initChat(chatId: Long) {
        currentChatId = chatId
        viewModelScope.launch {
            val effectiveId = if (chatId == Constants.NEW_CHAT_ID) {
                repository.createChat("Nuevo chat")
            } else {
                chatId
            }
            currentChatId = effectiveId
            _chat.value = repository.getChat(effectiveId)

            repository.observeMessages(effectiveId)
                .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
                .collect { list -> _messages.value = list }
        }
    }

    /**
     * Envía un mensaje del usuario y dispara la generación de la IA.
     */
    fun sendMessage(text: String) {
        if (text.isBlank() || _isGenerating.value) return

        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val userMsg = Message(
                chatId = currentChatId,
                role = Constants.ROLE_USER,
                content = text.trim(),
                timestamp = now
            )
            repository.addMessage(userMsg)

            // Renombrar el chat si es el primer mensaje
            val chat = _chat.value
            if (chat != null && (chat.title == "Nuevo chat" || chat.title.isBlank())) {
                val newTitle = text.take(30).replace("\n", " ")
                repository.renameChat(currentChatId, newTitle)
                _chat.value = chat.copy(title = newTitle)
            }

            generateAssistantReply(text)
        }
    }

    /**
     * Regenera la última respuesta del asistente.
     */
    fun regenerateLast() {
        if (_isGenerating.value) return
        val msgs = _messages.value
        val lastAssistant = msgs.lastOrNull { it.role == Constants.ROLE_ASSISTANT } ?: return
        val lastUser = msgs.lastOrNull { it.role == Constants.ROLE_USER } ?: return

        viewModelScope.launch {
            repository.deleteMessage(lastAssistant.id)
            generateAssistantReply(lastUser.content)
        }
    }

    /**
     * Borra un mensaje por id.
     */
    fun deleteMessage(messageId: Long) {
        viewModelScope.launch {
            repository.deleteMessage(messageId)
            _toast.value = "Mensaje borrado"
        }
    }

    /**
     * Copia el contenido de un mensaje al portapapeles (manejado por la UI).
     */
    fun onCopyMessage(content: String) {
        _toast.value = "Copiado: ${content.take(40)}..."
    }

    fun consumeToast() { _toast.value = null }

    // ---------- Internal ----------

    private suspend fun generateAssistantReply(userText: String) {
        _isGenerating.value = true
        val now = System.currentTimeMillis()

        // Insertamos un mensaje placeholder "streaming"
        val placeholder = Message(
            chatId = currentChatId,
            role = Constants.ROLE_ASSISTANT,
            content = "",
            timestamp = now,
            status = Constants.STATUS_STREAMING
        )
        val msgId = repository.addMessage(placeholder)

        val settings = settingsFlow.value
        val history = _messages.value.filter { it.id != msgId }

        try {
            engine.generateResponse(history, userText, settings).collect { partial ->
                repository.updateMessageContent(msgId, partial, Constants.STATUS_STREAMING)
            }
            // Marca como completo con el último contenido conocido
            val finalContent = _messages.value.firstOrNull { it.id == msgId }?.content ?: ""
            repository.updateMessageContent(msgId, finalContent, Constants.STATUS_COMPLETE)
        } catch (t: Throwable) {
            repository.updateMessageContent(msgId, "Error: ${t.message}", Constants.STATUS_ERROR)
        } finally {
            _isGenerating.value = false
        }
    }

    // ---------- Factory ----------

    companion object {
        fun factory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = App.instance
                val settingsStateFlow = kotlinx.coroutines.flow.MutableStateFlow(
                    com.example.aichat.domain.models.ChatSettings()
                )
                // Carga inicial y mantiene sincronizado
                kotlinx.coroutines.GlobalScope.launch {
                    val store = com.example.aichat.data.local.SettingsStore(app)
                    store.settings.collect { settingsStateFlow.value = it }
                }
                return ChatViewModel(
                    repository = app.chatRepository,
                    engine = ChatEngine(),
                    settingsFlow = settingsStateFlow
                ) as T
            }
        }
    }
}
