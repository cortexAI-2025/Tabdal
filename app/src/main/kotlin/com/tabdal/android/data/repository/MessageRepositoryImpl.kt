package com.tabdal.android.data.repository

import com.tabdal.android.data.local.TabdalDatabase
import com.tabdal.android.data.local.entities.ConversationEntity
import com.tabdal.android.data.local.entities.MessageEntity
import com.tabdal.android.data.remote.api.TabdalApi
import com.tabdal.android.data.remote.dto.ConversationDto
import com.tabdal.android.data.remote.dto.MessageDto
import com.tabdal.android.data.remote.dto.SendMessageRequest
import com.tabdal.android.domain.models.Conversation
import com.tabdal.android.domain.models.Message
import com.tabdal.android.domain.models.Report
import com.tabdal.android.domain.repository.MessageRepository
import com.tabdal.android.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepositoryImpl @Inject constructor(
    private val api: TabdalApi,
    private val db: TabdalDatabase
) : MessageRepository {

    override fun getConversations(): Flow<List<Conversation>> =
        db.messageDao().getConversations().map { list -> list.map { it.toDomain() } }

    override fun getMessages(conversationId: String): Flow<List<Message>> =
        db.messageDao().getMessages(conversationId).map { list -> list.map { it.toDomain() } }

    override suspend fun sendMessage(conversationId: String, listingId: String, content: String): Result<Message> = runCatching {
        val dto = api.sendMessage(conversationId, SendMessageRequest(listingId, content))
        db.messageDao().upsertMessage(dto.toEntity())
        Result.Success(dto.toDomain())
    }.getOrElse { Result.Error(it.message ?: "Erreur envoi") }

    override suspend fun startConversation(listingId: String, sellerId: String, initialMessage: String): Result<Conversation> = runCatching {
        val dto = api.startConversation(SendMessageRequest(listingId, initialMessage))
        db.messageDao().upsertConversation(dto.toEntity())
        Result.Success(dto.toDomain())
    }.getOrElse { Result.Error(it.message ?: "Erreur") }

    override suspend fun markConversationRead(conversationId: String): Result<Unit> = runCatching {
        api.markConversationRead(conversationId)
        db.messageDao().markConversationRead(conversationId)
        Result.Success(Unit)
    }.getOrElse { Result.Error(it.message ?: "Erreur") }

    override suspend fun reportMessage(report: Report): Result<Unit> = runCatching {
        api.submitReport(mapOf(
            "target_id" to report.targetId,
            "target_type" to report.targetType.name,
            "reason" to report.reason.name,
            "details" to report.details
        ))
        Result.Success(Unit)
    }.getOrElse { Result.Error(it.message ?: "Erreur") }

    private fun ConversationDto.toDomain() = Conversation(id, listingId, listingTitle, listingPhotoUrl, buyerId, buyerName, sellerId, sellerName, lastMessage, lastMessageAt, unreadCount)
    private fun MessageDto.toDomain() = Message(id, conversationId, senderId, senderName, content, createdAt, isRead)
    private fun ConversationDto.toEntity() = ConversationEntity(id, listingId, listingTitle, listingPhotoUrl, buyerId, buyerName, sellerId, sellerName, lastMessage, lastMessageAt, unreadCount)
    private fun MessageDto.toEntity() = MessageEntity(id, conversationId, senderId, senderName, content, createdAt, isRead)
    private fun ConversationEntity.toDomain() = Conversation(id, listingId, listingTitle, listingPhotoUrl, buyerId, buyerName, sellerId, sellerName, lastMessage, lastMessageAt, unreadCount)
    private fun MessageEntity.toDomain() = Message(id, conversationId, senderId, senderName, content, createdAt, isRead)
}
