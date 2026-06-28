package com.example.aichat.domain.repository

import com.example.aichat.domain.models.Chat
import com.example.aichat.domain.models.Message
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del repositorio de chat (capa domain).
 */
interface ChatRepository {

    fun observeChats(): Flow<List<Chat>>

    fun observeMessages(chatId: Long): Flow<List<Message>>

    suspend fun getChat(chatId: Long): Chat?

    suspend fun createChat(title: String): Long

    suspend fun renameChat(chatId: Long, newTitle: String)

    suspend fun deleteChat(chatId: Long)

    suspend fun deleteAllChats()

    suspend fun addMessage(message: Message): Long

    suspend fun updateMessageContent(messageId: Long, content: String, status: String)

    suspend fun deleteMessage(messageId: Long)
}
