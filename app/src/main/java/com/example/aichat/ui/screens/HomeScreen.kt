package com.example.aichat.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aichat.R
import com.example.aichat.ui.components.AppTopBar
import com.example.aichat.ui.effects.AuroraBackground
import com.example.aichat.ui.effects.CyberGridOverlay
import com.example.aichat.ui.effects.FloatingParticles
import com.example.aichat.ui.effects.LiquidGlassCard
import com.example.aichat.ui.effects.NeonCyan
import com.example.aichat.ui.effects.NeonPink
import com.example.aichat.ui.effects.NeonViolet
import com.example.aichat.ui.effects.ScanLineOverlay
import com.example.aichat.ui.effects.glowEffect
import com.example.aichat.ui.effects.pulsingGlow
import com.example.aichat.ui.effects.shimmerEffect
import com.example.aichat.ui.theme.AuroraGradient
import com.example.aichat.utils.toRelativeDay
import com.example.aichat.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenChat: (Long) -> Unit,
    onOpenSettings: () -> Unit,
    onCreateChat: (Long) -> Unit
) {
    val vm: HomeViewModel = viewModel(factory = HomeViewModel.factory())
    val chats by vm.chats.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        // Background stack: aurora + grid + particles + scanlines
        AuroraBackground()
        CyberGridOverlay()
        FloatingParticles(count = 35)
        ScanLineOverlay()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                AppTopBar(
                    title = stringResource(R.string.app_name),
                    onSettings = onOpenSettings
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { vm.createNewChat(onCreated = onCreateChat) },
                    containerColor = Color.Transparent,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(AuroraGradient))
                        .pulsingGlow(NeonViolet, 28.dp, 8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.new_chat), tint = Color.White)
                }
            }
        ) { padding ->
            if (chats.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Logo / orb
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Brush.radialGradient(AuroraGradient))
                                .pulsingGlow(NeonViolet, 40.dp, 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(56.dp))
                        }
                        Spacer(Modifier.height(24.dp))
                        Text(
                            stringResource(R.string.welcome_title),
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            modifier = Modifier.shimmerEffect()
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            stringResource(R.string.welcome_subtitle),
                            style = MaterialTheme.typography.bodyLarge,
                            color = NeonCyan.copy(alpha = 0.8f)
                        )
                        Spacer(Modifier.height(32.dp))
                        LiquidGlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            cornerRadius = 24.dp,
                            glowColor = NeonViolet.copy(alpha = 0.3f),
                            glowRadius = 32.dp
                        ) {
                            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Pulsa + para iniciar", color = Color.White, fontWeight = FontWeight.Medium)
                                Text(
                                    "Tu asistente de IA local y privado",
                                    color = NeonCyan,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        start = 12.dp, end = 12.dp, top = 8.dp, bottom = 96.dp
                    )
                ) {
                    items(chats, key = { it.id }) { chat ->
                        AnimatedVisibility(visible = true, enter = fadeIn() + scaleIn(), exit = fadeOut()) {
                            LiquidGlassCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onOpenChat(chat.id) },
                                cornerRadius = 18.dp,
                                glowColor = NeonViolet.copy(alpha = 0.15f),
                                glowRadius = 14.dp
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        chat.title.ifBlank { "Nuevo chat" },
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        chat.lastPreview.ifBlank { "Sin mensajes" },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = NeonCyan.copy(alpha = 0.7f),
                                        maxLines = 1
                                    )
                                    Spacer(Modifier.height(6.dp))
                                    Text(
                                        "${chat.messageCount} mensajes · ${chat.updatedAt.toRelativeDay()}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = NeonPink.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
