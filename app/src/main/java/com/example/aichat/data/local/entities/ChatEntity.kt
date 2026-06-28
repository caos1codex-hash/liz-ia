package com.example.aichat.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabla de conversaciones.
 */
@Entity(
    tableName = "chats",
    indices = [Index(value = ["updated_at"])]
)
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    @androidx.room.ColumnInfo(name = "created_at")
    val createdAt: Long,
    @androidx.room.ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @androidx.room.ColumnInfo(name = "message_count")
    val messageCount: Int = 0,
    @androidx.room.ColumnInfo(name = "last_preview")
    val lastPreview: String = ""
)
