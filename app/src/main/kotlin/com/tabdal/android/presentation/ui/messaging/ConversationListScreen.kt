package com.tabdal.android.presentation.ui.messaging

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tabdal.android.domain.models.Conversation
import com.tabdal.android.presentation.ui.components.AvatarInitials
import com.tabdal.android.presentation.ui.components.TabdalTopBar
import com.tabdal.android.presentation.viewmodels.MessageViewModel
import com.tabdal.android.utils.Extensions.toRelativeTime

@Composable
fun ConversationListScreen(
    onConversationClick: (String) -> Unit,
    viewModel: MessageViewModel = hiltViewModel()
) {
    val conversations by viewModel.conversations.collectAsStateWithLifecycle(initialValue = emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        TabdalTopBar(title = "Messages")

        if (conversations.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("💬", style = MaterialTheme.typography.headlineLarge)
                    Spacer(Modifier.height(8.dp))
                    Text("Aucun message", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(4.dp))
                    Text("Contactez un vendeur pour démarrer une conversation.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn {
                items(conversations, key = { it.id }) { conversation ->
                    ConversationItem(conversation = conversation, onClick = { onConversationClick(conversation.id) })
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Composable
private fun ConversationItem(conversation: Conversation, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BadgedBox(badge = {
            if (conversation.unreadCount > 0) {
                Badge { Text("${conversation.unreadCount}") }
            }
        }) {
            AvatarInitials(name = conversation.buyerName, size = 48)
        }
        Column(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = conversation.buyerName,
                    fontWeight = if (conversation.unreadCount > 0) FontWeight.Bold else FontWeight.Normal,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = conversation.lastMessageAt.toRelativeTime(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = conversation.listingTitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )
            if (conversation.lastMessage.isNotBlank()) {
                Text(
                    text = conversation.lastMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private fun Long.toRelativeTime() = com.tabdal.android.utils.Extensions.toRelativeTime(this)

private object Extensions {
    fun Long.toRelativeTime(): String {
        val now = System.currentTimeMillis()
        val diff = now - this
        return when {
            diff < 60_000 -> "À l'instant"
            diff < 3_600_000 -> "${diff / 60_000}min"
            diff < 86_400_000 -> "${diff / 3_600_000}h"
            else -> java.text.SimpleDateFormat("dd/MM", java.util.Locale.FRANCE).format(java.util.Date(this))
        }
    }
}
