package com.tabdal.android.presentation.ui.messaging

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tabdal.android.domain.models.Message
import com.tabdal.android.domain.models.Report
import com.tabdal.android.domain.models.ReportReason
import com.tabdal.android.domain.models.ReportTarget
import com.tabdal.android.presentation.ui.components.TabdalTopBar
import com.tabdal.android.presentation.viewmodels.MessageViewModel

@Composable
fun MessageScreen(
    conversationId: String,
    listingId: String,
    sellerName: String,
    onBack: () -> Unit,
    viewModel: MessageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val messages by viewModel.getMessages(conversationId).collectAsStateWithLifecycle(initialValue = emptyList())
    val currentUserId = viewModel.currentUserId() ?: ""
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(conversationId) { viewModel.markConversationRead(conversationId) }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabdalTopBar(title = sellerName, onBack = onBack)

        // Messages list
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages, key = { it.id }) { message ->
                MessageBubble(
                    message = message,
                    isOwnMessage = message.senderId == currentUserId,
                    onReport = {
                        viewModel.reportMessage(Report(
                            targetId = message.id, targetType = ReportTarget.MESSAGE,
                            reporterId = currentUserId, reason = ReportReason.OTHER
                        ))
                    }
                )
            }
        }

        // Input bar
        Surface(shadowElevation = 8.dp) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Votre message…") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = false,
                    maxLines = 4
                )
                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            viewModel.sendMessage(conversationId, listingId, messageText)
                            messageText = ""
                        }
                    },
                    enabled = messageText.isNotBlank() && !uiState.isLoading
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, "Envoyer",
                        tint = if (messageText.isNotBlank()) MaterialTheme.colorScheme.primary
                               else MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: Message, isOwnMessage: Boolean, onReport: () -> Unit) {
    val alignment = if (isOwnMessage) Alignment.End else Alignment.Start
    val bubbleColor = if (isOwnMessage) MaterialTheme.colorScheme.primary
                      else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (isOwnMessage) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant
    val shape = if (isOwnMessage)
        RoundedCornerShape(16.dp, 4.dp, 16.dp, 16.dp)
    else
        RoundedCornerShape(4.dp, 16.dp, 16.dp, 16.dp)

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alignment) {
        if (!isOwnMessage) {
            Text(message.senderName, style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 4.dp, bottom = 2.dp))
        }
        Surface(shape = shape, color = bubbleColor) {
            Text(message.content, color = textColor, modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyMedium)
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            val time = java.text.SimpleDateFormat("HH:mm", java.util.Locale.FRANCE)
                .format(java.util.Date(message.createdAt))
            Text(time, style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
            if (!isOwnMessage) {
                IconButton(onClick = onReport, modifier = Modifier.size(16.dp)) {
                    Icon(Icons.Default.Flag, "Signaler", modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
