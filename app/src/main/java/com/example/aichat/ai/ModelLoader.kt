package com.example.aichat.ai

/**
 * Cargador del modelo LLM local.
 *
 * Stub: la integración real depende del motor elegido
 * (llama.cpp, MLC LLM, ONNX Runtime, TensorFlow Lite, etc.).
 *
 * Flujo esperado cuando integres un motor real:
 *   1. Llamar a `loadModel(path)` al iniciar la app o al abrir un chat.
 *   2. Mantener una referencia nativa / instancia del motor en `engineHandle`.
 *   3. `ChatEngine.generate()` usará esa referencia para inferir tokens.
 *   4. Llamar a `unload()` al cerrar la app o cambiar de modelo.
 */
class ModelLoader {

    @Volatile
    private var loaded: Boolean = false

    @Volatile
    private var modelPath: String = ""

    /**
     * True si hay un modelo cargado en memoria.
     */
    fun isLoaded(): Boolean = loaded

    /**
     * Ruta del modelo cargado actualmente.
     */
    fun currentPath(): String = modelPath

    /**
     * Carga un modelo desde una ruta absoluta en el dispositivo.
     * Devuelve true si la carga fue exitosa.
     *
     * En esta versión stub, solo valida que la ruta no esté vacía.
     */
    suspend fun loadModel(path: String): Boolean {
        // TODO: Integrar motor real. Ejemplo conceptual con llama.cpp:
        //   val params = LlamaModelParams()
        //   val model = LlamaModel.load(path, params)
        //   engineHandle = model
        return try {
            require(path.isNotBlank()) { "Ruta de modelo vacía" }
            // Simula latencia de carga
            kotlinx.coroutines.delay(300)
            modelPath = path
            loaded = true
            true
        } catch (t: Throwable) {
            loaded = false
            false
        }
    }

    /**
     * Descarga el modelo actual y libera recursos.
     */
    fun unload() {
        // TODO: liberar engineHandle del motor real
        loaded = false
        modelPath = ""
    }
}
