package com.example.aichat.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aichat.R
import com.example.aichat.ui.components.AppTopBar
import com.example.aichat.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val vm: SettingsViewModel = viewModel(factory = SettingsViewModel.factory())
    val settings by vm.settings.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AppTopBar(title = stringResource(R.string.settings), onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Temperatura
            Text(stringResource(R.string.temperature), style = MaterialTheme.typography.titleMedium)
            Text(
                "%.2f".format(settings.temperature),
                color = MaterialTheme.colorScheme.primary
            )
            Slider(
                value = settings.temperature,
                onValueChange = { vm.updateTemperature(it) },
                valueRange = 0f..2f
            )
            Spacer(Modifier.height(16.dp))

            // Top K
            Text(stringResource(R.string.top_k), style = MaterialTheme.typography.titleMedium)
            Text("${settings.topK}", color = MaterialTheme.colorScheme.primary)
            Slider(
                value = settings.topK.toFloat(),
                onValueChange = { vm.updateTopK(it.toInt()) },
                valueRange = 1f..100f
            )
            Spacer(Modifier.height(16.dp))

            // Top P
            Text(stringResource(R.string.top_p), style = MaterialTheme.typography.titleMedium)
            Text("%.2f".format(settings.topP), color = MaterialTheme.colorScheme.primary)
            Slider(
                value = settings.topP,
                onValueChange = { vm.updateTopP(it) },
                valueRange = 0f..1f
            )
            Spacer(Modifier.height(16.dp))

            // Font size
            Text(stringResource(R.string.font_size), style = MaterialTheme.typography.titleMedium)
            Text("${settings.fontSize} sp", color = MaterialTheme.colorScheme.primary)
            Slider(
                value = settings.fontSize.toFloat(),
                onValueChange = { vm.updateFontSize(it.toInt()) },
                valueRange = 12f..28f
            )
            Spacer(Modifier.height(16.dp))

            // Model path
            Text(stringResource(R.string.model_path), style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = settings.modelPath,
                onValueChange = { vm.updateModelPath(it) },
                placeholder = { Text(stringResource(R.string.model_path_placeholder)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))

            // Actions
            OutlinedButton(
                onClick = { vm.clearAllChats() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.clear_chats))
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { vm.clearCache() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.clear_cache))
            }
        }
    }
}
