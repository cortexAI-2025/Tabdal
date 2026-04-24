package com.tabdal.android.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessageDto(
    @Json(name = "_id") val id: String,
    @Json(name = "conversation_id") val conversationId: String,
    @Json(name = "sender_id") val senderId: String,
    @Json(name = "sender_name") val senderName: String,
    val content: String,
    @Json(name = "created_at") val createdAt: Long,
    @Json(name = "is_read") val isRead: Boolean = false
)

@JsonClass(generateAdapter = true)
data class ConversationDto(
    @Json(name = "_id") val id: String,
    @Json(name = "listing_id") val listingId: String,
    @Json(name = "listing_title") val listingTitle: String,
    @Json(name = "listing_photo_url") val listingPhotoUrl: String = "",
    @Json(name = "buyer_id") val buyerId: String,
    @Json(name = "buyer_name") val buyerName: String,
    @Json(name = "seller_id") val sellerId: String,
    @Json(name = "seller_name") val sellerName: String,
    @Json(name = "last_message") val lastMessage: String = "",
    @Json(name = "last_message_at") val lastMessageAt: Long = 0L,
    @Json(name = "unread_count") val unreadCount: Int = 0
)

@JsonClass(generateAdapter = true)
data class SendMessageRequest(
    @Json(name = "listing_id") val listingId: String,
    val content: String
)
