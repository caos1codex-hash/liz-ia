package com.example.aichat.ui.effects

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.aichat.ui.theme.AuroraGradient
import com.example.aichat.ui.theme.DarkBackground
import com.example.aichat.ui.theme.DarkBackgroundDeep
import com.example.aichat.ui.theme.GlassGradient
import com.example.aichat.ui.theme.NeonBlue
import com.example.aichat.ui.theme.NeonCyan
import com.example.aichat.ui.theme.NeonPink
import com.example.aichat.ui.theme.NeonViolet
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// ===================================================================
// 1. LIQUID GLASS CARD
// ===================================================================

/**
 * Tarjeta con efecto "Liquid Glass":
 *  - fondo translúcido con gradiente vertical
 *  - borde brillante superior (highlight)
 *  - glow opcional
 */
@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    glowColor: Color? = null,
    glowRadius: Dp = 24.dp,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .clip(shape)
            .then(if (glowColor != null) Modifier.glowEffect(glowColor, glowRadius) else Modifier)
            .background(Brush.verticalGradient(GlassGradient))
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.35f),
                        Color.White.copy(alpha = 0.05f),
                        Color.White.copy(alpha = 0.0f)
                    )
                ),
                shape = shape
            )
    ) {
        content()
    }
}

// ===================================================================
// 2. GLOW EFFECT (neon / bloom)
// ===================================================================

fun Modifier.glowEffect(
    color: Color = NeonViolet,
    radius: Dp = 20.dp
): Modifier = this.drawBehind {
    drawIntoCanvas { canvas ->
        val paint = Paint().apply {
            this.color = color
            this.isAntiAlias = true
        }
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.setShadowLayer(
            radius.toPx(),
            0f,
            0f,
            color.toArgb()
        )
        canvas.drawRoundRect(
            left = 0f,
            top = 0f,
            right = size.width,
            bottom = size.height,
            radiusX = 0f,
            radiusY = 0f,
            paint = paint
        )
    }
}

// ===================================================================
// 3. SHIMMER (efecto de brillo deslizante)
// ===================================================================

@Composable
fun Modifier.shimmerEffect(): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val x by transition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerX"
    )
    return this.drawWithContent {
        drawContent()
        val w = size.width
        val brush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0f),
                Color.White.copy(alpha = 0.35f),
                Color.White.copy(alpha = 0f)
            ),
            start = Offset(x * w, 0f),
            end = Offset((x + 0.5f) * w, size.height)
        )
        drawRect(brush = brush)
    }
}

// ===================================================================
// 4. ANIMATED AURORA BACKGROUND
// ===================================================================

/**
 * Fondo tipo "aurora boreal" animado.
 * Capas de gradientes que se desplazan lentamente.
 */
@Composable
fun AuroraBackground(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "aurora")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "auroraT"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackgroundDeep)
    ) {
        // Capa 1 - blob violeta
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cx = w * (0.3f + 0.15f * sin(t * 2 * Math.PI.toFloat()))
            val cy = h * (0.25f + 0.1f * cos(t * 2 * Math.PI.toFloat()))
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF7C4DFF).copy(alpha = 0.55f), Color.Transparent),
                    radius = size.minDimension * 0.55f
                ),
                center = Offset(cx, cy),
                radius = size.minDimension * 0.6f
            )
        }
        // Capa 2 - blob cyan
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cx = w * (0.75f + 0.12f * cos(t * 2 * Math.PI.toFloat()))
            val cy = h * (0.65f + 0.15f * sin(t * 2 * Math.PI.toFloat()))
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF00E5FF).copy(alpha = 0.45f), Color.Transparent),
                    radius = size.minDimension * 0.5f
                ),
                center = Offset(cx, cy),
                radius = size.minDimension * 0.55f
            )
        }
        // Capa 3 - blob pink
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cx = w * (0.5f + 0.2f * sin(t * 2 * Math.PI.toFloat() + 1.5f))
            val cy = h * (0.85f + 0.08f * cos(t * 2 * Math.PI.toFloat() + 1.5f))
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFF4081).copy(alpha = 0.4f), Color.Transparent),
                    radius = size.minDimension * 0.45f
                ),
                center = Offset(cx, cy),
                radius = size.minDimension * 0.5f
            )
        }
        // Overlay sutil
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, DarkBackground.copy(alpha = 0.3f))
                )
            )
        }
    }
}

// ===================================================================
// 5. FLOATING PARTICLES
// ===================================================================

data class Particle(
    val x: Float,
    val y: Float,
    val radius: Float,
    val speed: Float,
    val color: Color,
    val phase: Float
)

/**
 * Campo de partículas flotantes con efecto twinkle (parpadeo).
 */
@Composable
fun FloatingParticles(
    modifier: Modifier = Modifier,
    count: Int = 40
) {
    val transition = rememberInfiniteTransition(label = "particles")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particlesT"
    )
    val particles = remember {
        val rnd = Random(42)
        val colors = listOf(NeonViolet, NeonCyan, NeonPink, NeonBlue)
        List(count) {
            Particle(
                x = rnd.nextFloat(),
                y = rnd.nextFloat(),
                radius = 1.5f + rnd.nextFloat() * 3.5f,
                speed = 0.2f + rnd.nextFloat() * 0.8f,
                color = colors[rnd.nextInt(colors.size)],
                phase = rnd.nextFloat() * 2f * Math.PI.toFloat()
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        particles.forEach { p ->
            val yPos = ((p.y - t * p.speed) % 1f + 1f) % 1f
            val twinkle = 0.4f + 0.6f * (0.5f + 0.5f * sin(t * 6f * Math.PI.toFloat() + p.phase))
            drawCircle(
                color = p.color.copy(alpha = twinkle * 0.85f),
                radius = p.radius,
                center = Offset(p.x * w, yPos * h)
            )
            // Halo
            drawCircle(
                color = p.color.copy(alpha = twinkle * 0.15f),
                radius = p.radius * 3f,
                center = Offset(p.x * w, yPos * h)
            )
        }
    }
}

// ===================================================================
// 6. GRID MESH OVERLAY (cyberpunk)
// ===================================================================

@Composable
fun CyberGridOverlay(
    modifier: Modifier = Modifier,
    gridSize: Dp = 32.dp,
    color: Color = NeonViolet.copy(alpha = 0.08f)
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val step = gridSize.toPx()
        var x = 0f
        while (x <= size.width) {
            drawLine(
                color = color,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 1f
            )
            x += step
        }
        var y = 0f
        while (y <= size.height) {
            drawLine(
                color = color,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f
            )
            y += step
        }
    }
}

// ===================================================================
// 7. PULSING GLOW (para FAB / botones)
// ===================================================================

@Composable
fun Modifier.pulsingGlow(
    color: Color = NeonViolet,
    maxRadius: Dp = 28.dp,
    minRadius: Dp = 10.dp,
    periodMs: Int = 1800
): Modifier {
    val transition = rememberInfiniteTransition(label = "pulse")
    val r by transition.animateFloat(
        initialValue = minRadius.value,
        targetValue = maxRadius.value,
        animationSpec = infiniteRepeatable(
            animation = tween(periodMs, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseR"
    )
    return this.glowEffect(color, r.dp)
}

// ===================================================================
// 8. ANIMATED GRADIENT TEXT
// ===================================================================

@Composable
fun AnimatedGradientText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.headlineMedium,
    colors: List<Color> = AuroraGradient
) {
    val transition = rememberInfiniteTransition(label = "gradText")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradTextT"
    )
    val offsetX = t
    Text(
        text = text,
        modifier = modifier,
        style = style.copy(
            brush = Brush.linearGradient(
                colors = colors,
                start = Offset(offsetX * 400f, 0f),
                end = Offset(offsetX * 400f + 400f, 200f)
            )
        )
    )
}

// ===================================================================
// 9. NEON DIVIDER
// ===================================================================

@Composable
fun NeonDivider(
    modifier: Modifier = Modifier,
    color: Color = NeonViolet,
    thickness: Dp = 1.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        color.copy(alpha = 0.7f),
                        color,
                        color.copy(alpha = 0.7f),
                        Color.Transparent
                    )
                )
            )
    )
}

// ===================================================================
// 10. SCAN LINE EFFECT (sci-fi overlay)
// ===================================================================

@Composable
fun ScanLineOverlay(
    modifier: Modifier = Modifier,
    color: Color = NeonCyan.copy(alpha = 0.05f)
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val step = 4f
        var y = 0f
        while (y <= size.height) {
            drawLine(
                color = color,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f
            )
            y += step
        }
    }
}
