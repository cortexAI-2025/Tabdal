package com.tabdal.android.domain.models

data class Message(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val senderName: String,
    val content: String,
    val createdAt: Long,
    val isRead: Boolean = false,
    val isReported: Boolean = false
)

data class Conversation(
    val id: String,
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
