package com.example.aichat.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabla de mensajes de cada conversación.
 */
@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["id"],
            childColumns = ["chat_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["chat_id"])]
)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @androidx.room.ColumnInfo(name = "chat_id")
    val chatId: Long,
    val role: String,            // "user" | "assistant" | "system"
    val content: String,
    val timestamp: Long,
    val status: String = "complete"  // "complete" | "streaming" | "error"
)
