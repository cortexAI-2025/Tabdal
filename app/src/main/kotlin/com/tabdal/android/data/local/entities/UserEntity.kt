package com.tabdal.android.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val email: String,
    val fullName: String,
    val phone: String,
    val city: String,
    val photoUrl: String? = null,
    val role: String = "BUYER",
    val createdAt: Long = 0L,
    val isVerified: Boolean = false
)
