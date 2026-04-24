package com.tabdal.android.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tabdal.android.domain.models.User
import com.tabdal.android.domain.repository.AuthRepository
import com.tabdal.android.domain.usecases.auth.LoginUseCase
import com.tabdal.android.domain.usecases.auth.RegisterUseCase
import com.tabdal.android.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val verificationId: String? = null,
    val user: User? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val currentUser = authRepository.currentUser
    val isLoggedIn get() = authRepository.isLoggedIn()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = loginUseCase(email, password)) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, success = true, user = result.data) }
                is Result.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun register(email: String, password: String, confirmPassword: String, fullName: String, phone: String, city: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = registerUseCase(email, password, confirmPassword, fullName, phone, city)) {
                is Result.Success -> sendPhoneVerification(phone)
                is Result.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun sendPhoneVerification(phone: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = authRepository.sendPhoneVerification(phone)) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, verificationId = result.data) }
                is Result.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun verifyOtp(verificationId: String, code: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = authRepository.verifyPhone(verificationId, code)) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, success = true) }
                is Result.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = authRepository.sendPasswordReset(email)) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, success = true) }
                is Result.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun logout() { viewModelScope.launch { authRepository.logout() } }

    fun deleteAccount() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = authRepository.deleteAccount()) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, success = true) }
                is Result.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun clearError()   { _uiState.update { it.copy(error = null) } }
    fun clearSuccess() { _uiState.update { it.copy(success = false, verificationId = null) } }
}
