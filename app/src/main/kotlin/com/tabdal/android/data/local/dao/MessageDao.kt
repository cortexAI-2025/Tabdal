package com.tabdal.android.data.local.dao

import androidx.room.*
import com.tabdal.android.data.local.entities.ConversationEntity
import com.tabdal.android.data.local.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM conversations ORDER BY lastMessageAt DESC")
    fun getConversations(): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY createdAt ASC")
    fun getMessages(conversationId: String): Flow<List<MessageEntity>>

    @Upsert
    suspend fun upsertConversations(conversations: List<ConversationEntity>)

    @Upsert
    suspend fun upsertConversation(conversation: ConversationEntity)

    @Upsert
    suspend fun upsertMessages(messages: List<MessageEntity>)

    @Upsert
    suspend fun upsertMessage(message: MessageEntity)

    @Query("UPDATE conversations SET unreadCount = 0 WHERE id = :conversationId")
    suspend fun markConversationRead(conversationId: String)
}
