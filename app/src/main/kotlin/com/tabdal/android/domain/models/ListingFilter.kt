package com.tabdal.android.domain.models

data class ListingFilter(
    val city: String = "",
    val district: String = "",
    val propertyType: PropertyType? = null,
    val transactionType: TransactionType? = null,
    val priceMin: Long? = null,
    val priceMax: Long? = null,
    val surfaceMin: Double? = null,
    val roomsMin: Int? = null,
    val sortBy: SortOption = SortOption.RECENT
)

enum class SortOption(val labelFr: String, val apiValue: String) {
    RECENT("Plus récent", "createdAt_desc"),
    PRICE_ASC("Prix croissant", "price_asc"),
    PRICE_DESC("Prix décroissant", "price_desc")
}
