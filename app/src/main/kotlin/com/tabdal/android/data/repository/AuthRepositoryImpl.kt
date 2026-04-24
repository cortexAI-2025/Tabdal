package com.tabdal.android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.tabdal.android.data.local.dao.UserDao
import com.tabdal.android.data.local.entities.UserEntity
import com.tabdal.android.data.remote.api.TabdalApi
import com.tabdal.android.data.remote.dto.LoginRequest
import com.tabdal.android.data.remote.dto.RegisterRequest
import com.tabdal.android.domain.models.User
import com.tabdal.android.domain.models.UserRole
import com.tabdal.android.domain.repository.AuthRepository
import com.tabdal.android.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: TabdalApi,
    private val userDao: UserDao,
    private val firebaseAuth: FirebaseAuth,
    private val dataStore: DataStore<Preferences>
) : AuthRepository {

    private val tokenKey = stringPreferencesKey("auth_token")
    private val userIdKey = stringPreferencesKey("user_id")

    override val currentUser: Flow<User?> = dataStore.data.map { prefs ->
        val userId = prefs[userIdKey] ?: return@map null
        userDao.getUserById(userId).first()?.toDomain()
    }

    override suspend fun login(email: String, password: String): Result<User> = runCatching {
        val response = api.login(LoginRequest(email, password))
        dataStore.edit { prefs ->
            prefs[tokenKey] = response.token
            prefs[userIdKey] = response.user.id
        }
        val user = response.user
        userDao.upsertUser(UserEntity(user.id, user.email, user.fullName, user.phone, user.city, user.photoUrl, user.role, user.createdAt, user.isVerified))
        Result.Success(user.toDomain())
    }.getOrElse { Result.Error(it.message ?: "Erreur de connexion", it) }

    override suspend fun register(email: String, password: String, fullName: String, phone: String, city: String): Result<User> = runCatching {
        val response = api.register(RegisterRequest(email, password, fullName, phone, city))
        dataStore.edit { prefs ->
            prefs[tokenKey] = response.token
            prefs[userIdKey] = response.user.id
        }
        val user = response.user
        userDao.upsertUser(UserEntity(user.id, user.email, user.fullName, user.phone, user.city, user.photoUrl, user.role, user.createdAt, user.isVerified))
        Result.Success(user.toDomain())
    }.getOrElse { Result.Error(it.message ?: "Erreur d'inscription", it) }

    override suspend fun sendPhoneVerification(phoneNumber: String): Result<String> {
        return Result.Success("mock_verification_id_$phoneNumber")
    }

    override suspend fun verifyPhone(verificationId: String, code: String): Result<Unit> = runCatching {
        if (verificationId.startsWith("mock_")) {
            Result.Success(Unit)
        } else {
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            firebaseAuth.signInWithCredential(credential).await()
            Result.Success(Unit)
        }
    }.getOrElse { Result.Error(it.message ?: "Code invalide", it) }

    override suspend fun sendPasswordReset(email: String): Result<Unit> = runCatching {
        firebaseAuth.sendPasswordResetEmail(email).await()
        Result.Success(Unit)
    }.getOrElse { Result.Error(it.message ?: "Erreur", it) }

    override suspend fun logout(): Result<Unit> = runCatching {
        dataStore.edit { it.clear() }
        firebaseAuth.signOut()
        Result.Success(Unit)
    }.getOrElse { Result.Error(it.message ?: "Erreur", it) }

    override suspend fun deleteAccount(): Result<Unit> = runCatching {
        api.deleteAccount()
        dataStore.edit { it.clear() }
        firebaseAuth.currentUser?.delete()?.await()
        Result.Success(Unit)
    }.getOrElse { Result.Error(it.message ?: "Erreur", it) }

    override suspend fun exportUserData(): Result<String> = runCatching {
        val data = api.exportData()
        Result.Success(data.toString())
    }.getOrElse { Result.Error(it.message ?: "Erreur", it) }

    override fun isLoggedIn(): Boolean = runBlocking {
        dataStore.data.first()[tokenKey] != null
    }

    override fun getCurrentUserId(): String? = runBlocking {
        dataStore.data.first()[userIdKey]
    }

    private fun com.tabdal.android.data.remote.dto.UserDto.toDomain() = User(
        id, email, fullName, phone, city, photoUrl,
        try { UserRole.valueOf(role) } catch (e: Exception) { UserRole.BUYER },
        createdAt, isVerified
    )

    private fun UserEntity.toDomain() = User(
        id, email, fullName, phone, city, photoUrl,
        try { UserRole.valueOf(role) } catch (e: Exception) { UserRole.BUYER },
        createdAt, isVerified
    )
}
