package com.tabdal.android.domain.usecases.auth

import com.tabdal.android.domain.models.User
import com.tabdal.android.domain.repository.AuthRepository
import com.tabdal.android.utils.Result
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        email: String, password: String, confirmPassword: String,
        fullName: String, phone: String, city: String
    ): Result<User> {
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return Result.Error("Email invalide")
        if (password.length < 8) return Result.Error("Mot de passe trop court (min. 8 caractères)")
        if (password != confirmPassword) return Result.Error("Les mots de passe ne correspondent pas")
        if (fullName.isBlank()) return Result.Error("Nom requis")
        if (phone.isBlank()) return Result.Error("Téléphone requis")
        if (city.isBlank()) return Result.Error("Ville requise")
        return authRepository.register(email.trim(), password, fullName.trim(), phone.trim(), city.trim())
    }
}
