package com.tabdal.android.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val senderId: String,
    val senderName: String,
    val content: String,
    val createdAt: Long,
    val isRead: Boolean = false
)

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey val id: String,
    val listingId: String,
    val listingTitle: String,
    val listingPhotoUrl: String = "",
    val buyerId: String,
    val buyerName: String,
    val sellerId: String,
    val sellerName: String,
    val lastMessage: String = "",
    val lastMessageAt: Long = 0L,
    val unreadCount: Int = 0
)
