package com.example.aichat.ai

import com.example.aichat.domain.models.ChatSettings
import com.example.aichat.domain.models.Message
import com.example.aichat.utils.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Motor de chat. Produce respuestas token a token (streaming simulado).
 *
 * Si [ModelLoader.isLoaded] == false, genera una respuesta simulada
 * para que la app sea funcionalmente utilizable sin un LLM real.
 * Cuando integres tu motor, reemplaza el bloque `simulateResponse`
 * por la llamada a `engine.generate(prompt)`.
 */
class ChatEngine(
    private val modelLoader: ModelLoader = ModelLoader()
) {

    /**
     * Genera una respuesta streaming para el mensaje del usuario.
     * Emite fragmentos parciales (tokens) que el ViewModel irá pintando.
     */
    fun generateResponse(
        history: List<Message>,
        userMessage: String,
        settings: ChatSettings
    ): Flow<String> = flow {
        val prompt = PromptProcessor.buildPrompt(history, userMessage)
        val trimmedHistory = PromptProcessor.trimHistory(history)

        val fullResponse: String = if (modelLoader.isLoaded()) {
            // TODO: reemplazar con llamada real al motor
            //   engineHandle.generate(prompt, settings.temperature, settings.topK, settings.topP)
            simulateResponse(userMessage, settings)
        } else {
            simulateResponse(userMessage, settings)
        }

        // Streaming token a token
        val tokens = tokenize(fullResponse)
        val sb = StringBuilder()
        for (token in tokens) {
            sb.append(token)
            emit(sb.toString())
            delay(Constants.STREAM_DELAY_MS)
        }
    }

    /**
     * Simulación de respuesta cuando no hay modelo cargado.
     * Genera algo plausible según el input del usuario.
     */
    private fun simulateResponse(userMessage: String, settings: ChatSettings): String {
        val msg = userMessage.trim().lowercase()
        return when {
            msg.isEmpty() -> "No recibí ningún mensaje. ¿Puedes repetirlo?"
            msg.startsWith("hola") || msg.startsWith("buenas") || msg.startsWith("hey") ->
                "Hola, soy LIZ IA. Estoy lista para ayudarte. ¿Sobre qué quieres conversar?"
            msg.contains("quien eres") || msg.contains("quién eres") ->
                "Soy LIZ IA, un asistente local que funciona en tu dispositivo Android. Mi lógica de inferencia está lista para integrar un modelo LLM real."
            msg.endsWith("?") ->
                "Buena pregunta. En modo simulación puedo darte una respuesta de ejemplo, pero cuando integres un LLM real en `ChatEngine`, esta respuesta vendrá del modelo cargado en `ModelLoader`."
            msg.contains("gracias") ->
                "¡De nada! Estoy aquí para ayudar."
            else ->
                buildString {
                    append("Recibí tu mensaje: "$userMessage".\n\n")
                    append("• Temperatura actual: ${settings.temperature}\n")
                    append("• Top-K: ${settings.topK}\n")
                    append("• Top-P: ${settings.topP}\n\n")
                    append("Para activar respuestas reales, coloca un modelo en /models/ y conéctalo en ModelLoader + ChatEngine.")
                }
        }
    }

    /**
     * Tokeniza respetando espacios y signos.
     */
    private fun tokenize(text: String): List<String> {
        val result = mutableListOf<String>()
        val sb = StringBuilder()
        for (ch in text) {
            sb.append(ch)
            if (sb.length >= Constants.STREAM_TOKEN_SIZE || ch == ' ' || ch == '\n') {
                result.add(sb.toString())
                sb.clear()
            }
        }
        if (sb.isNotEmpty()) result.add(sb.toString())
        return result
    }
}
