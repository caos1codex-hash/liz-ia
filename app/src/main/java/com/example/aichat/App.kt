package com.example.aichat

import android.app.Application
import com.example.aichat.data.local.ChatDatabase
import com.example.aichat.data.repository.ChatRepositoryImpl
import com.example.aichat.domain.repository.ChatRepository

/**
 * Application class. Punto de entrada de la app.
 * Inicializa la base de datos Room y el repositorio.
 */
class App : Application() {

    val database: ChatDatabase by lazy { ChatDatabase.getInstance(this) }

    val chatRepository: ChatRepository by lazy {
        ChatRepositoryImpl(database.chatDao())
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
            private set
    }
}
