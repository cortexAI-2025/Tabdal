package com.tabdal.android.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListingDto(
    @Json(name = "_id") val id: String,
    val title: String,
    val description: String,
    val price: Long,
    @Json(name = "is_negotiable") val isNegotiable: Boolean = false,
    val surface: Double,
    val rooms: Int = 0,
    val bathrooms: Int = 0,
    val floor: Int? = null,
    @Json(name = "year_built") val yearBuilt: Int? = null,
    val condition: String = "GOOD",
    val type: String,
    @Json(name = "transaction_type") val transactionType: String = "SALE",
    val status: String = "PUBLISHED",
    val city: String,
    val district: String = "",
    val address: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    @Json(name = "photo_urls") val photoUrls: List<String> = emptyList(),
    @Json(name = "seller_id") val sellerId: String,
    @Json(name = "seller_name") val sellerName: String = "",
    @Json(name = "seller_phone") val sellerPhone: String = "",
    @Json(name = "seller_since") val sellerSince: Long = 0L,
    @Json(name = "view_count") val viewCount: Int = 0,
    @Json(name = "message_count") val messageCount: Int = 0,
    @Json(name = "created_at") val createdAt: Long = 0L,
    @Json(name = "expires_at") val expiresAt: Long = 0L
)

@JsonClass(generateAdapter = true)
data class ListingsPage(
    val data: List<ListingDto>,
    val total: Int,
    val page: Int,
    @Json(name = "page_size") val pageSize: Int
)
