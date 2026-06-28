package com.example.aichat.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aichat.R
import com.example.aichat.ui.components.AppTopBar
import com.example.aichat.ui.components.ChatInput
import com.example.aichat.ui.components.MessageBubble
import com.example.aichat.utils.Constants
import com.example.aichat.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatId: Long,
    onBack: () -> Unit
) {
    val vm: ChatViewModel = viewModel(factory = ChatViewModel.factory())
    val chat by vm.chat.collectAsStateWithLifecycle()
    val messages by vm.messages.collectAsStateWithLifecycle()
    val isGenerating by vm.isGenerating.collectAsStateWithLifecycle()
    val toast by vm.toast.collectAsStateWithLifecycle()

    LaunchedEffect(chatId) { vm.initChat(chatId) }

    val listState = rememberLazyListState()
    val snackbarHost = remember { SnackbarHostState() }

    // Auto-scroll al último mensaje
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    // Toast transient
    LaunchedEffect(toast) {
        toast?.let {
            snackbarHost.showSnackbar(it)
            vm.consumeToast()
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = chat?.title ?: stringResource(R.string.app_name),
                onBack = onBack
            )
        },
        snackbarHost = { SnackbarHost(snackbarHost) },
        bottomBar = {
            ChatInput(
                enabled = !isGenerating,
                onSend = { vm.sendMessage(it) }
            )
        }
    ) { padding ->
        if (messages.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(
                    stringResource(R.string.empty_chat),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(messages, key = { it.id }) { msg ->
                    val lastAssistantId = messages.lastOrNull {
                        it.role == Constants.ROLE_ASSISTANT
                    }?.id
                    MessageBubble(
                        message = msg,
                        fontSize = 16,
                        isLastAssistant = msg.id == lastAssistantId,
                        onCopy = { vm.onCopyMessage(it) },
                        onDelete = { vm.deleteMessage(it) },
                        onRegenerate = { vm.regenerateLast() }
                    )
                }
            }
        }
    }
}
