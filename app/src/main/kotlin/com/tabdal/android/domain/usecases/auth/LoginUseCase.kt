package com.tabdal.android.domain.usecases.auth

import com.tabdal.android.domain.models.User
import com.tabdal.android.domain.repository.AuthRepository
import com.tabdal.android.utils.Result
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank()) return Result.Error("Email requis")
        if (password.length < 8) return Result.Error("Mot de passe trop court")
        return authRepository.login(email.trim(), password)
    }
}
