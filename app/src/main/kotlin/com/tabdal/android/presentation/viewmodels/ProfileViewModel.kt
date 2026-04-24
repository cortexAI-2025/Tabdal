package com.tabdal.android.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tabdal.android.domain.models.User
import com.tabdal.android.domain.repository.AuthRepository
import com.tabdal.android.domain.repository.UserRepository
import com.tabdal.android.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val user: User? = null,
    val exportData: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    val currentUser: Flow<User?> = authRepository.currentUser

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = userRepository.getCurrentUser()) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, user = result.data) }
                is Result.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun updateProfile(user: User, photoUri: Uri?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = userRepository.updateProfile(user, photoUri)) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, success = true, user = result.data) }
                is Result.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = userRepository.changePassword(currentPassword, newPassword)) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, success = true) }
                is Result.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun exportData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = authRepository.exportUserData()) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, exportData = result.data) }
                is Result.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun logout() { viewModelScope.launch { authRepository.logout() } }

    fun clearError()   { _uiState.update { it.copy(error = null) } }
    fun clearSuccess() { _uiState.update { it.copy(success = false) } }
}
