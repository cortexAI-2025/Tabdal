package com.tabdal.android.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "_id") val id: String,
    val email: String,
    @Json(name = "full_name") val fullName: String,
    val phone: String,
    val city: String,
    @Json(name = "photo_url") val photoUrl: String? = null,
    val role: String = "BUYER",
    @Json(name = "created_at") val createdAt: Long = 0L,
    @Json(name = "is_verified") val isVerified: Boolean = false
)

@JsonClass(generateAdapter = true)
data class LoginRequest(val email: String, val password: String)

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    val email: String,
    val password: String,
    @Json(name = "full_name") val fullName: String,
    val phone: String,
    val city: String
)

@JsonClass(generateAdapter = true)
data class AuthResponse(val token: String, val user: UserDto)
