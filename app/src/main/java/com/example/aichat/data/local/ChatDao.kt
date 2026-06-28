package com.example.aichat.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.aichat.data.local.entities.ChatEntity
import com.example.aichat.data.local.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

/**
 * Acceso a datos Room para chats y mensajes.
 */
@Dao
interface ChatDao {

    // ---------- CHATS ----------

    @Query("SELECT * FROM chats ORDER BY updated_at DESC")
    fun observeChats(): Flow<List<ChatEntity>>

    @Query("SELECT * FROM chats WHERE id = :chatId LIMIT 1")
    suspend fun getChat(chatId: Long): ChatEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatEntity): Long

    @Update
    suspend fun updateChat(chat: ChatEntity)

    @Query("UPDATE chats SET title = :title WHERE id = :chatId")
    suspend fun renameChat(chatId: Long, title: String)

    @Query("UPDATE chats SET updated_at = :updatedAt, message_count = :msgCount, last_preview = :preview WHERE id = :chatId")
    suspend fun touchChat(chatId: Long, updatedAt: Long, msgCount: Int, preview: String)

    @Query("DELETE FROM chats WHERE id = :chatId")
    suspend fun deleteChat(chatId: Long)

    @Query("DELETE FROM chats")
    suspend fun deleteAllChats()

    // ---------- MESSAGES ----------

    @Query("SELECT * FROM messages WHERE chat_id = :chatId ORDER BY timestamp ASC")
    fun observeMessages(chatId: Long): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity): Long

    @Query("UPDATE messages SET content = :content, status = :status WHERE id = :messageId")
    suspend fun updateMessage(messageId: Long, content: String, status: String)

    @Query("DELETE FROM messages WHERE id = :messageId")
    suspend fun deleteMessage(messageId: Long)

    @Query("SELECT COUNT(*) FROM messages WHERE chat_id = :chatId")
    suspend fun countMessages(chatId: Long): Int

    @Delete
    suspend fun deleteMessageEntity(message: MessageEntity)
}
