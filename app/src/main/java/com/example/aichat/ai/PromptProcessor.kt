package com.example.aichat.ai

import com.example.aichat.domain.models.Message
import com.example.aichat.utils.Constants

/**
 * Procesa el historial de mensajes y construye el prompt final
 * que se le envía al motor de inferencia (LLM).
 *
 * En esta versión stub, simplemente concatena los mensajes con un
 * prefijo por rol. Cuando integres un LLM real, aquí puedes aplicar
 * plantillas específicas (ChatML, Llama-3, Mistral, etc.).
 */
object PromptProcessor {

    /**
     * Construye un prompt en texto plano a partir del historial.
     */
    fun buildPrompt(history: List<Message>, newUserMessage: String): String {
        val sb = StringBuilder()
        sb.appendLine("[Sistema] Eres LIZ IA, un asistente útil, conciso y respetuoso.")
        history.forEach { msg ->
            val tag = when (msg.role) {
                Constants.ROLE_USER -> "Usuario"
                Constants.ROLE_ASSISTANT -> "LIZ"
                else -> "Sistema"
            }
            sb.appendLine("[$tag] ${msg.content}")
        }
        sb.appendLine("[Usuario] $newUserMessage")
        sb.append("[LIZ] ")
        return sb.toString()
    }

    /**
     * Recorta el historial para mantener solo los últimos N turnos
     * y no desbordar la ventana de contexto.
     */
    fun trimHistory(history: List<Message>, maxMessages: Int = 20): List<Message> =
        if (history.size <= maxMessages) history
        else history.takeLast(maxMessages)
}
