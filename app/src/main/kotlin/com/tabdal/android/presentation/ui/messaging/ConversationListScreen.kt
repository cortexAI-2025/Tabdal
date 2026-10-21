package com.tabdal.android.presentation.ui.messaging

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.ChatBubbleOutline
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
import com.tabdal.android.presentation.ui.components.EmptyState
import com.tabdal.android.presentation.ui.components.ScreenTitle
import com.tabdal.android.presentation.viewmodels.MessageViewModel
import com.tabdal.android.utils.toRelativeTime

@Composable
fun ConversationListScreen(
    onConversationClick: (String) -> Unit,
    viewModel: MessageViewModel = hiltViewModel()
) {
    val conversations by viewModel.conversations.collectAsStateWithLifecycle(initialValue = emptyList())

    var filter by remember { mutableIntStateOf(0) }
    val filters = listOf("Tout", "Non lus", "Ventes")
    val shown = when (filter) {
        1 -> conversations.filter { it.unreadCount > 0 }
        2 -> conversations.filter { it.sellerId == viewModel.currentUserId() }
        else -> conversations
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ScreenTitle("Mes messages")
        Row(Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            filters.forEachIndexed { i, label ->
                FilterChip(
                    selected = filter == i, onClick = { filter = i }, label = { Text(label) },
                    shape = RoundedCornerShape(50),
                    leadingIcon = if (filter == i) { { Icon(Icons.Default.Check, null, Modifier.size(18.dp)) } } else null
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        if (shown.isEmpty()) {
            EmptyState(
                Icons.Outlined.ChatBubbleOutline,
                if (filter == 1) "Bravo ! Vous n'avez pas de messages non lus." else "Aucun message",
                if (filter == 1) "Assurez-vous d'avoir répondu aux questions de tous vos contacts."
                else "Contactez un vendeur pour démarrer une conversation."
            )
        } else {
            LazyColumn {
                items(shown, key = { it.id }) { conversation ->
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
