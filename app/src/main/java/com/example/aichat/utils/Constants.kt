package com.example.aichat.utils

/**
 * Constantes globales de la app.
 */
object Constants {
    const val DB_NAME = "aichat.db"

    // Roles de mensaje
    const val ROLE_USER = "user"
    const val ROLE_ASSISTANT = "assistant"
    const val ROLE_SYSTEM = "system"

    // Estados de mensaje
    const val STATUS_COMPLETE = "complete"
    const val STATUS_STREAMING = "streaming"
    const val STATUS_ERROR = "error"

    // Default settings
    const val DEFAULT_TEMPERATURE = 0.7f
    const val DEFAULT_TOP_K = 40
    const val DEFAULT_TOP_P = 0.9f
    const val DEFAULT_FONT_SIZE = 16
    const val DEFAULT_MODEL_PATH = "/models/model.bin"

    // Streaming simulation
    const val STREAM_DELAY_MS = 25L
    const val STREAM_TOKEN_SIZE = 3

    // Navigation
    const val NEW_CHAT_ID = -1L
}
