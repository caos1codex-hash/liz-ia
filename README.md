# LIZ IA - Chat IA Local (Android)

Aplicación Android tipo ChatGPT offline con IA local.

## Stack

- Kotlin + Jetpack Compose
- Material 3
- MVVM + Clean Architecture
- Navigation Compose
- Coroutines + Flow
- Room Database
- ViewModel

## Estructura

```
app/src/main/java/com/example/aichat/
├── MainActivity.kt
├── App.kt
├── ui/            # screens + components + theme
├── viewmodel/     # ChatViewModel, SettingsViewModel
├── data/          # local (Room) + repository impl
├── domain/        # models + repository interfaces
├── ai/            # ModelLoader, ChatEngine, PromptProcessor (stubs)
├── utils/         # Constants, Extensions
└── core/          # navigation
```

## Cómo abrir

1. Clona el repositorio.
2. Abre la carpeta en Android Studio (Hedgehog o superior).
3. Ejecuta `gradle wrapper` para regenerar el wrapper si falta.
4. Sync Gradle y ejecuta en emulador o dispositivo.

## Notas

- La carpeta `/models/` está reservada para integrar un LLM real después.
- `ChatEngine` contiene un stub que simula respuestas. Reemplázalo con tu motor de inferencia favorito (llama.cpp, MLC LLM, etc.).
