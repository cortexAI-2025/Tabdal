package com.tabdal.android.domain.models

data class User(
    val id: String,
    val email: String,
    val fullName: String,
    val phone: String,
    val city: String,
    val photoUrl: String? = null,
    val role: UserRole = UserRole.BUYER,
    val createdAt: Long = 0L,
    val isVerified: Boolean = false,
    val isBlocked: Boolean = false
)

enum class UserRole { VISITOR, BUYER, SELLER, ADMIN }
