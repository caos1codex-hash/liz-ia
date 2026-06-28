package com.example.aichat.data.repository

import com.example.aichat.data.local.ChatDao
import com.example.aichat.data.local.entities.ChatEntity
import com.example.aichat.data.local.entities.MessageEntity
import com.example.aichat.domain.models.Chat
import com.example.aichat.domain.models.Message
import com.example.aichat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación del repositorio de chat.
 * Convierte entidades Room <-> modelos de dominio.
 */
class ChatRepositoryImpl(
    private val dao: ChatDao
) : ChatRepository {

    override fun observeChats(): Flow<List<Chat>> =
        dao.observeChats().map { list -> list.map { it.toDomain() } }

    override fun observeMessages(chatId: Long): Flow<List<Message>> =
        dao.observeMessages(chatId).map { list -> list.map { it.toDomain() } }

    override suspend fun getChat(chatId: Long): Chat? =
        dao.getChat(chatId)?.toDomain()

    override suspend fun createChat(title: String): Long {
        val now = System.currentTimeMillis()
        val chat = ChatEntity(
            title = title,
            createdAt = now,
            updatedAt = now
        )
        return dao.insertChat(chat)
    }

    override suspend fun renameChat(chatId: Long, newTitle: String) {
        dao.renameChat(chatId, newTitle)
    }

    override suspend fun deleteChat(chatId: Long) {
        dao.deleteChat(chatId)
    }

    override suspend fun deleteAllChats() {
        dao.deleteAllChats()
    }

    override suspend fun addMessage(message: Message): Long {
        val entity = MessageEntity(
            chatId = message.chatId,
            role = message.role,
            content = message.content,
            timestamp = message.timestamp,
            status = message.status
        )
        val id = dao.insertMessage(entity)

        // Actualiza el chat padre (preview, contador, timestamp)
        val now = System.currentTimeMillis()
        val count = dao.countMessages(message.chatId)
        val preview = message.content.take(60)
        dao.touchChat(message.chatId, now, count, preview)

        return id
    }

    override suspend fun updateMessageContent(messageId: Long, content: String, status: String) {
        dao.updateMessage(messageId, content, status)
    }

    override suspend fun deleteMessage(messageId: Long) {
        dao.deleteMessage(messageId)
    }

    // ---------- Mappers ----------

    private fun ChatEntity.toDomain(): Chat = Chat(
        id = id,
        title = title,
        createdAt = createdAt,
        updatedAt = updatedAt,
        messageCount = messageCount,
        lastPreview = lastPreview
    )

    private fun MessageEntity.toDomain(): Message = Message(
        id = id,
        chatId = chatId,
        role = role,
        content = content,
        timestamp = timestamp,
        status = status
    )
}
