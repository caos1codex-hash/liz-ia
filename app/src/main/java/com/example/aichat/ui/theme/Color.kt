package com.example.aichat.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ===================================================================
// PALETA AAA - LIZ IA
// Inspirada en: aurora boreal + neón cyber + glassmorphism premium
// ===================================================================

// --- Core brand ---
val LizPrimary = Color(0xFF7C4DFF)         // Violeta eléctrico
val LizPrimaryDark = Color(0xFF3D1FB8)
val LizSecondary = Color(0xFF00E5FF)       // Cyan neón
val LizTertiary = Color(0xFFFF4081)        // Rosa magenta
val LizAccent = Color(0xFFB388FF)          // Lavanda luminosa

// --- Light scheme ---
val LightBackground = Color(0xFFF4F2FF)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFE8E4FF)
val LightOnBackground = Color(0xFF1A1033)
val LightOnSurface = Color(0xFF2A1F4D)
val LightGlass = Color(0xCCFFFFFF)         // 80% white
val LightGlassBorder = Color(0x33FFFFFF)

// --- Dark scheme (modo showcase) ---
val DarkBackground = Color(0xFF070314)
val DarkBackgroundDeep = Color(0xFF02010A)
val DarkSurface = Color(0xFF100828)
val DarkSurfaceVariant = Color(0xFF1A0F3D)
val DarkOnBackground = Color(0xFFEDE7FF)
val DarkOnSurface = Color(0xFFD8CEFF)
val DarkGlass = Color(0x1AFFFFFF)          // 10% white
val DarkGlassBorder = Color(0x33FFFFFF)

// --- Neon accents (glow) ---
val NeonViolet = Color(0xFFB388FF)
val NeonCyan = Color(0xFF18FFFF)
val NeonPink = Color(0xFFFF80AB)
val NeonGreen = Color(0xFF69F0AE)
val NeonBlue = Color(0xFF82B1FF)

// --- Gradients ---
val PrimaryGradient = listOf(
    Color(0xFF7C4DFF),
    Color(0xFFE040FB),
    Color(0xFFFF4081)
)

val AuroraGradient = listOf(
    Color(0xFF00E5FF),
    Color(0xFF7C4DFF),
    Color(0xFFE040FB),
    Color(0xFFFF4081)
)

val CosmicGradient = listOf(
    Color(0xFF3D1FB8),
    Color(0xFF7C4DFF),
    Color(0xFFB388FF),
    Color(0xFF18FFFF)
)

val SunsetGradient = listOf(
    Color(0xFFFF6B6B),
    Color(0xFFFFB86C),
    Color(0xFFFDFFB6)
)

val UserBubbleGradient = listOf(
    Color(0xFF7C4DFF),
    Color(0xFFE040FB)
)

val AiBubbleGradient = listOf(
    Color(0x1AFFFFFF),
    Color(0x14FFFFFF)
)

val GlassGradient = listOf(
    Color(0x33FFFFFF),
    Color(0x14FFFFFF),
    Color(0x0DFFFFFF)
)

val TopBarGradient = listOf(
    Color(0x66100828),
    Color(0x33070314),
    Color(0x00000000)
)

// --- Brush factories ---
fun auroraBrush(): Brush = Brush.linearGradient(AuroraGradient)
fun cosmicBrush(): Brush = Brush.linearGradient(CosmicGradient)
fun userBubbleBrush(): Brush = Brush.horizontalGradient(UserBubbleGradient)
fun aiBubbleBrush(): Brush = Brush.horizontalGradient(AiBubbleGradient)
fun glassBrush(): Brush = Brush.verticalGradient(GlassGradient)
fun primaryGradientBrush(): Brush = Brush.linearGradient(PrimaryGradient)
