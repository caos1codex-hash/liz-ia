package com.example.aichat.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aichat.domain.models.Message
import com.example.aichat.ui.effects.LiquidGlassCard
import com.example.aichat.ui.effects.NeonCyan
import com.example.aichat.ui.effects.NeonPink
import com.example.aichat.ui.effects.NeonViolet
import com.example.aichat.ui.effects.glowEffect
import com.example.aichat.ui.effects.shimmerEffect
import com.example.aichat.ui.theme.AuroraGradient
import com.example.aichat.ui.theme.UserBubbleGradient
import com.example.aichat.utils.Constants
import com.example.aichat.utils.toTimeString

/**
 * Burbuja de mensaje estilo ChatGPT + Liquid Glass + Glow + Animaciones.
 */
@Composable
fun MessageBubble(
    message: Message,
    fontSize: Int,
    isLastAssistant: Boolean,
    onCopy: (String) -> Unit,
    onDelete: (Long) -> Unit,
    onRegenerate: () -> Unit
) {
    val clipboard = LocalClipboardManager.current
    val isUser = message.role == Constants.ROLE_USER
    var hovered by remember { mutableStateOf(false) }

    val bubbleShape = if (isUser) RoundedCornerShape(20.dp, 6.dp, 20.dp, 20.dp)
                      else RoundedCornerShape(6.dp, 20.dp, 20.dp, 20.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Avatar IA (solo para assistant)
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(AuroraGradient))
                    .glowEffect(NeonViolet, 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .then(if (isUser) Modifier.wrapContentWidth(Alignment.End) else Modifier)
        ) {
            Box(
                modifier = Modifier
                    .clip(bubbleShape)
                    .then(
                        if (isUser) Modifier
                            .background(Brush.horizontalGradient(UserBubbleGradient))
                            .glowEffect(NeonPink.copy(alpha = 0.6f), 14.dp)
                        else Modifier
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.10f),
                                        Color.White.copy(alpha = 0.04f)
                                    )
                                )
                            )
                            .glowEffect(NeonCyan.copy(alpha = 0.25f), 10.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                if (isUser) {
                    Text(
                        text = if (message.content.isEmpty() && message.status == Constants.STATUS_STREAMING) "..."
                               else message.content,
                        color = Color.White,
                        fontSize = fontSize.sp,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    if (message.status == Constants.STATUS_STREAMING && message.content.isEmpty()) {
                        // Indicador "typing" con shimmer
                        Text(
                            text = "LIZ está escribiendo",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            fontSize = 13.sp,
                            modifier = Modifier.shimmerEffect()
                        )
                    } else {
                        Text(
                            text = message.content,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = fontSize.sp,
                            modifier = if (message.status == Constants.STATUS_STREAMING)
                                Modifier.shimmerEffect() else Modifier
                        )
                    }
                }
            }

            // Metadata row: hora + acciones
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.padding(top = 2.dp, start = 4.dp, end = 4.dp)
                ) {
                    Text(
                        text = message.timestamp.toTimeString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    IconButton(onClick = {
                        clipboard.setText(AnnotatedString(message.content))
                        onCopy(message.content)
                    }, modifier = Modifier.size(22.dp)) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "Copiar",
                            modifier = Modifier.size(13.dp),
                            tint = NeonCyan
                        )
                    }
                    IconButton(onClick = { onDelete(message.id) }, modifier = Modifier.size(22.dp)) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Borrar",
                            modifier = Modifier.size(13.dp),
                            tint = NeonPink
                        )
                    }
                    if (!isUser && isLastAssistant) {
                        IconButton(onClick = onRegenerate, modifier = Modifier.size(22.dp)) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Regenerar",
                                modifier = Modifier.size(13.dp),
                                tint = NeonViolet
                            )
                        }
                    }
                }
            }
        }

        // Avatar Usuario (solo para user)
        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFFFF4081), Color(0xFFE040FB))
                        )
                    )
                    .glowEffect(NeonPink, 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "U",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
