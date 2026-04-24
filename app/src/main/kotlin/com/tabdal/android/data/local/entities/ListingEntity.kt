package com.tabdal.android.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "listings")
data class ListingEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val price: Long,
    val isNegotiable: Boolean = false,
    val surface: Double,
    val rooms: Int = 0,
    val bathrooms: Int = 0,
    val floor: Int? = null,
    val yearBuilt: Int? = null,
    val condition: String = "GOOD",
    val type: String,
    val transactionType: String = "SALE",
    val status: String = "PUBLISHED",
    val city: String,
    val district: String = "",
    val address: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val photoUrlsJson: String = "[]",
    val sellerId: String,
    val sellerName: String = "",
    val viewCount: Int = 0,
    val isFavorite: Boolean = false,
    val createdAt: Long = 0L,
    val expiresAt: Long = 0L
)
