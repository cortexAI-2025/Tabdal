package com.tabdal.android.data.repository

import android.net.Uri
import com.tabdal.android.data.local.dao.UserDao
import com.tabdal.android.data.local.entities.UserEntity
import com.tabdal.android.data.remote.api.TabdalApi
import com.tabdal.android.data.remote.dto.UserDto
import com.tabdal.android.domain.models.User
import com.tabdal.android.domain.models.UserRole
import com.tabdal.android.domain.repository.UserRepository
import com.tabdal.android.utils.Result
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: TabdalApi,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getCurrentUser(): Result<User> = runCatching {
        val dto = api.getProfile()
        userDao.upsertUser(dto.toEntity())
        Result.Success(dto.toDomain())
    }.getOrElse { Result.Error(it.message ?: "Erreur") }

    override suspend fun updateProfile(user: User, photoUri: Uri?): Result<User> = runCatching {
        val json = JSONObject().apply {
            put("full_name", user.fullName)
            put("phone", user.phone)
            put("city", user.city)
        }
        val dataBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val dto = api.updateProfile(dataBody, null)
        userDao.upsertUser(dto.toEntity())
        Result.Success(dto.toDomain())
    }.getOrElse { Result.Error(it.message ?: "Erreur") }

    override suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> = runCatching {
        api.changePassword(mapOf("current_password" to currentPassword, "new_password" to newPassword))
        Result.Success(Unit)
    }.getOrElse { Result.Error(it.message ?: "Erreur") }

    override suspend fun getAdminStats(): Result<Map<String, Any>> = runCatching {
        Result.Success(api.getAdminStats())
    }.getOrElse { Result.Error(it.message ?: "Erreur") }

    override suspend fun blockUser(userId: String): Result<Unit> = runCatching {
        api.blockUser(userId)
        Result.Success(Unit)
    }.getOrElse { Result.Error(it.message ?: "Erreur") }

    override suspend fun deleteUser(userId: String): Result<Unit> = runCatching {
        api.deleteUser(userId)
        Result.Success(Unit)
    }.getOrElse { Result.Error(it.message ?: "Erreur") }

    private fun UserDto.toDomain() = User(
        id, email, fullName, phone, city, photoUrl,
        try { UserRole.valueOf(role) } catch (e: Exception) { UserRole.BUYER },
        createdAt, isVerified
    )

    private fun UserDto.toEntity() = UserEntity(id, email, fullName, phone, city, photoUrl, role, createdAt, isVerified)
}
