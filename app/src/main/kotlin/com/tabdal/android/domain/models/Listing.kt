package com.tabdal.android.domain.models

data class Listing(
    val id: String,
    val title: String,
    val description: String,
    val price: Long,
    val isNegotiable: Boolean = false,
    val surface: Double,
    val rooms: Int = 0,
    val bathrooms: Int = 0,
    val floor: Int? = null,
    val yearBuilt: Int? = null,
    val condition: PropertyCondition = PropertyCondition.GOOD,
    val type: PropertyType,
    val transactionType: TransactionType = TransactionType.SALE,
    val status: ListingStatus = ListingStatus.PUBLISHED,
    val city: String,
    val district: String = "",
    val address: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val photoUrls: List<String> = emptyList(),
    val sellerId: String,
    val sellerName: String = "",
    val sellerPhone: String = "",
    val sellerSince: Long = 0L,
    val viewCount: Int = 0,
    val messageCount: Int = 0,
    val isFavorite: Boolean = false,
    val createdAt: Long = 0L,
    val expiresAt: Long = 0L
)

enum class PropertyType(val labelFr: String) {
    APARTMENT("Appartement"),
    HOUSE("Maison"),
    VILLA("Villa"),
    LAND("Terrain"),
    COMMERCIAL("Local commercial"),
    OFFICE("Bureau"),
    OTHER("Autre")
}

enum class TransactionType(val labelFr: String) {
    SALE("À vendre"),
    RENT("À louer")
}

enum class ListingStatus(val labelFr: String) {
    DRAFT("Brouillon"),
    PUBLISHED("Publié"),
    SOLD("Vendu"),
    DISABLED("Désactivé")
}

enum class PropertyCondition(val labelFr: String) {
    NEW("Neuf"),
    GOOD("Bon état"),
    TO_RENOVATE("À rénover")
}
