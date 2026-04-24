package com.tabdal.android.domain.repository

import android.net.Uri
import com.tabdal.android.domain.models.User
import com.tabdal.android.utils.Result

interface UserRepository {
    suspend fun getCurrentUser(): Result<User>
    suspend fun updateProfile(user: User, photoUri: Uri?): Result<User>
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit>
    suspend fun getAdminStats(): Result<Map<String, Any>>
    suspend fun blockUser(userId: String): Result<Unit>
    suspend fun deleteUser(userId: String): Result<Unit>
}
