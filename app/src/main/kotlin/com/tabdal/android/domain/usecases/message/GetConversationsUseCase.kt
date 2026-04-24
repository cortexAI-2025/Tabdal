package com.tabdal.android.domain.usecases.message

import com.tabdal.android.domain.models.Conversation
import com.tabdal.android.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetConversationsUseCase @Inject constructor(private val repo: MessageRepository) {
    operator fun invoke(): Flow<List<Conversation>> = repo.getConversations()
}
