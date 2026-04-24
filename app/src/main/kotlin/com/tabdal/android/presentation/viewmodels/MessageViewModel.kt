package com.tabdal.android.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tabdal.android.domain.models.Conversation
import com.tabdal.android.domain.models.Message
import com.tabdal.android.domain.models.Report
import com.tabdal.android.domain.repository.AuthRepository
import com.tabdal.android.domain.repository.MessageRepository
import com.tabdal.android.domain.usecases.message.GetConversationsUseCase
import com.tabdal.android.domain.usecases.message.SendMessageUseCase
import com.tabdal.android.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MessageUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val messageSent: Boolean = false,
    val conversations: List<Conversation> = emptyList(),
    val messages: List<Message> = emptyList()
)

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val getConversationsUseCase: GetConversationsUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val messageRepository: MessageRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessageUiState())
    val uiState: StateFlow<MessageUiState> = _uiState.asStateFlow()

    val conversations: Flow<List<Conversation>> = getConversationsUseCase()

    fun getMessages(conversationId: String): Flow<List<Message>> =
        messageRepository.getMessages(conversationId)

    fun sendMessage(conversationId: String, listingId: String, content: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = sendMessageUseCase(conversationId, listingId, content)) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, messageSent = true) }
                is Result.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun startConversation(listingId: String, sellerId: String, message: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = messageRepository.startConversation(listingId, sellerId, message)) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, messageSent = true) }
                is Result.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun markConversationRead(conversationId: String) {
        viewModelScope.launch { messageRepository.markConversationRead(conversationId) }
    }

    fun reportMessage(report: Report) {
        viewModelScope.launch { messageRepository.reportMessage(report) }
    }

    fun clearMessageSent() { _uiState.update { it.copy(messageSent = false) } }
    fun clearError() { _uiState.update { it.copy(error = null) } }
    fun currentUserId() = authRepository.getCurrentUserId()
}
