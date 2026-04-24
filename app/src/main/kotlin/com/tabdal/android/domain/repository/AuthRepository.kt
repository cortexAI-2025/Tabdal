package com.tabdal.android.domain.repository

import com.tabdal.android.domain.models.User
import com.tabdal.android.utils.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, password: String, fullName: String, phone: String, city: String): Result<User>
    suspend fun sendPhoneVerification(phoneNumber: String): Result<String>
    suspend fun verifyPhone(verificationId: String, code: String): Result<Unit>
    suspend fun sendPasswordReset(email: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>
    suspend fun exportUserData(): Result<String>
    fun isLoggedIn(): Boolean
    fun getCurrentUserId(): String?
}
