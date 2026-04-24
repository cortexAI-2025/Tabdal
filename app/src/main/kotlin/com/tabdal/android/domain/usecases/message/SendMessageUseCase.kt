package com.tabdal.android.domain.usecases.message

import com.tabdal.android.domain.models.Message
import com.tabdal.android.domain.repository.MessageRepository
import com.tabdal.android.utils.Result
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(private val repo: MessageRepository) {
    suspend operator fun invoke(conversationId: String, listingId: String, content: String): Result<Message> {
        if (content.isBlank()) return Result.Error("Message vide")
        return repo.sendMessage(conversationId, listingId, content.trim())
    }
}
