package com.example.aichat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import com.example.aichat.ui.effects.glowEffect
import com.example.aichat.ui.effects.pulsingGlow
import com.example.aichat.ui.theme.AuroraGradient
import com.example.aichat.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val vm: SettingsViewModel = viewModel(factory = SettingsViewModel.factory())
    val settings by vm.settings.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxWidth()) {
        AuroraBackground()
        CyberGridOverlay()
        FloatingParticles(count = 25)

        Scaffold(
            containerColor = Color.Transparent,
            topBar = { AppTopBar(title = stringResource(R.string.settings), onBack = onBack) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Spacer(Modifier.height(8.dp))

                // ---- Sección: Parámetros de inferencia ----
                SectionTitle("Parámetros de IA")

                LiquidGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 22.dp,
                    glowColor = NeonViolet.copy(alpha = 0.25f),
                    glowRadius = 20.dp
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        SettingSliderRow(
                            label = stringResource(R.string.temperature),
                            value = settings.temperature,
                            valueText = "%.2f".format(settings.temperature),
                            range = 0f..2f,
                            onValueChange = { vm.updateTemperature(it) }
                        )
                        SettingSliderRow(
                            label = stringResource(R.string.top_k),
                            value = settings.topK.toFloat(),
                            valueText = "${settings.topK}",
                            range = 1f..100f,
                            onValueChange = { vm.updateTopK(it.toInt()) }
                        )
                        SettingSliderRow(
                            label = stringResource(R.string.top_p),
                            value = settings.topP,
                            valueText = "%.2f".format(settings.topP),
                            range = 0f..1f,
                            onValueChange = { vm.updateTopP(it) }
                        )
                    }
                }

                // ---- Sección: Apariencia ----
                SectionTitle("Apariencia")

                LiquidGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 22.dp,
                    glowColor = NeonCyan.copy(alpha = 0.2f),
                    glowRadius = 20.dp
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        SettingSliderRow(
                            label = stringResource(R.string.font_size),
                            value = settings.fontSize.toFloat(),
                            valueText = "${settings.fontSize} sp",
                            range = 12f..28f,
                            onValueChange = { vm.updateFontSize(it.toInt()) }
                        )
                    }
                }

                // ---- Sección: Modelo ----
                SectionTitle("Modelo local")

                LiquidGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 22.dp,
                    glowColor = NeonPink.copy(alpha = 0.2f),
                    glowRadius = 20.dp
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            stringResource(R.string.model_path),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = settings.modelPath,
                            onValueChange = { vm.updateModelPath(it) },
                            placeholder = { Text(stringResource(R.string.model_path_placeholder)) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White.copy(alpha = 0.05f),
                                unfocusedContainerColor = Color.White.copy(alpha = 0.03f),
                                focusedBorderColor = NeonViolet,
                                unfocusedBorderColor = NeonViolet.copy(alpha = 0.4f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = NeonViolet
                            )
                        )
                    }
                }

                // ---- Sección: Datos ----
                SectionTitle("Datos y caché")

                GradientButton(
                    text = stringResource(R.string.clear_chats),
                    onClick = { vm.clearAllChats() }
                )
                GradientButton(
                    text = stringResource(R.string.clear_cache),
                    onClick = { vm.clearCache() }
                )

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        color = NeonCyan,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp
    )
}

@Composable
private fun SettingSliderRow(
    label: String,
    value: Float,
    valueText: String,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = Color.White, fontWeight = FontWeight.Medium)
            Text(valueText, color = NeonViolet, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = NeonViolet,
                activeTrackColor = NeonViolet,
                inactiveTrackColor = NeonViolet.copy(alpha = 0.2f)
            )
        )
    }
}

@Composable
private fun GradientButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.horizontalGradient(AuroraGradient))
            .pulsingGlow(NeonPink.copy(alpha = 0.4f), 22.dp, 6.dp, 2400)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(20.dp),
            content = {
                Text(
                    text,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        )
    }
}
