package com.example.aichat.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aichat.domain.models.Message
import com.example.aichat.utils.Constants
import com.example.aichat.utils.initials
import com.example.aichat.utils.toTimeString

/**
 * Burbuja de mensaje estilo ChatGPT.
 *  - Usuario: alineado a la derecha, color primario.
 *  - Asistente: alineado a la izquierda, color surface.
 *  - Muestra hora, avatar (iniciales) y acciones (copiar, borrar, regenerar).
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        if (!isUser) {
            Avatar(initials = "LIZ", color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .then(if (isUser) Modifier.wrapContentWidth(Alignment.End) else Modifier)
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        if (isUser) RoundedCornerShape(16.dp, 4.dp, 16.dp, 16.dp)
                        else RoundedCornerShape(4.dp, 16.dp, 16.dp, 16.dp)
                    )
                    .background(
                        if (isUser) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = if (message.content.isEmpty() && message.status == Constants.STATUS_STREAMING) "..."
                           else message.content,
                    color = if (isUser) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurface,
                    fontSize = fontSize.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = message.timestamp.toTimeString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = {
                    clipboard.setText(AnnotatedString(message.content))
                    onCopy(message.content)
                }, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.ContentCopy, contentDescription = "Copiar", modifier = Modifier.size(14.dp))
                }
                IconButton(onClick = { onDelete(message.id) }, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Borrar", modifier = Modifier.size(14.dp))
                }
                if (!isUser && isLastAssistant) {
                    IconButton(onClick = onRegenerate, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Refresh, contentDescription = "Regenerar", modifier = Modifier.size(14.dp))
                    }
                }
            }
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Avatar(initials = "U", color = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Composable
private fun Avatar(initials: String, color: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials.initials().ifEmpty { initials },
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
