package com.tabdal.android.domain.repository

import com.tabdal.android.domain.models.Conversation
import com.tabdal.android.domain.models.Message
import com.tabdal.android.domain.models.Report
import com.tabdal.android.utils.Result
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getConversations(): Flow<List<Conversation>>
    fun getMessages(conversationId: String): Flow<List<Message>>
    suspend fun sendMessage(conversationId: String, listingId: String, content: String): Result<Message>
    suspend fun startConversation(listingId: String, sellerId: String, initialMessage: String): Result<Conversation>
    suspend fun markConversationRead(conversationId: String): Result<Unit>
    suspend fun reportMessage(report: Report): Result<Unit>
}
