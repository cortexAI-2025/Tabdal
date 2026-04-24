package com.tabdal.android.data.repository

import android.net.Uri
import androidx.paging.*
import com.tabdal.android.data.local.TabdalDatabase
import com.tabdal.android.data.local.entities.ListingEntity
import com.tabdal.android.data.remote.api.TabdalApi
import com.tabdal.android.data.remote.dto.ListingDto
import com.tabdal.android.domain.models.*
import com.tabdal.android.domain.repository.ListingRepository
import com.tabdal.android.utils.Constants
import com.tabdal.android.utils.Result
import kotlinx.coroutines.flow.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListingRepositoryImpl @Inject constructor(
    private val api: TabdalApi,
    private val db: TabdalDatabase
) : ListingRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getListings(filter: ListingFilter): Flow<PagingData<Listing>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { db.listingDao().getPublishedListings() }
        ).flow.map { pagingData -> pagingData.map { it.toDomain() } }
    }

    override suspend fun getListingById(id: String): Result<Listing> = runCatching {
        val dto = api.getListingById(id)
        db.listingDao().upsertListing(dto.toEntity())
        Result.Success(dto.toDomain())
    }.getOrElse { Result.Error(it.message ?: "Erreur") }

    override suspend fun getMyListings(): Result<List<Listing>> = runCatching {
        val dtos = api.getMyListings()
        db.listingDao().upsertListings(dtos.map { it.toEntity() })
        Result.Success(dtos.map { it.toDomain() })
    }.getOrElse { Result.Error(it.message ?: "Erreur") }

    override suspend fun createListing(listing: Listing, photoUris: List<Uri>): Result<Listing> = runCatching {
        val json = JSONObject().apply {
            put("title", listing.title)
            put("description", listing.description)
            put("price", listing.price)
            put("is_negotiable", listing.isNegotiable)
            put("surface", listing.surface)
            put("rooms", listing.rooms)
            put("bathrooms", listing.bathrooms)
            put("type", listing.type.name)
            put("transaction_type", listing.transactionType.name)
            put("city", listing.city)
            put("district", listing.district)
            put("address", listing.address)
        }
        val dataBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val photoParts = photoUris.mapIndexed { i, uri ->
            val file = File(uri.path ?: "")
            val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("photos", "photo_$i.jpg", reqFile)
        }
        val dto = api.createListing(dataBody, photoParts)
        db.listingDao().upsertListing(dto.toEntity())
        Result.Success(dto.toDomain())
    }.getOrElse { Result.Error(it.message ?: "Erreur création") }

    override suspend fun updateListing(listing: Listing, newPhotoUris: List<Uri>): Result<Listing> = runCatching {
        val json = JSONObject().apply {
            put("title", listing.title)
            put("description", listing.description)
            put("price", listing.price)
            put("surface", listing.surface)
        }
        val dataBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val dto = api.updateListing(listing.id, dataBody, emptyList())
        db.listingDao().upsertListing(dto.toEntity())
        Result.Success(dto.toDomain())
    }.getOrElse { Result.Error(it.message ?: "Erreur mise à jour") }

    override suspend fun deleteListing(id: String): Result<Unit> = runCatching {
        api.deleteListing(id)
        db.listingDao().deleteListing(id)
        Result.Success(Unit)
    }.getOrElse { Result.Error(it.message ?: "Erreur") }

    override suspend fun markAsSold(id: String): Result<Unit> = runCatching {
        api.markAsSold(id)
        Result.Success(Unit)
    }.getOrElse { Result.Error(it.message ?: "Erreur") }

    override suspend fun renewListing(id: String): Result<Unit> = runCatching {
        api.renewListing(id)
        Result.Success(Unit)
    }.getOrElse { Result.Error(it.message ?: "Erreur") }

    override suspend fun toggleFavorite(listingId: String): Result<Boolean> = runCatching {
        val result = api.toggleFavorite(listingId)
        val isFav = result["is_favorite"] as? Boolean ?: false
        db.listingDao().updateFavorite(listingId, isFav)
        Result.Success(isFav)
    }.getOrElse { Result.Error(it.message ?: "Erreur") }

    override fun getFavorites(): Flow<List<Listing>> =
        db.listingDao().getFavoriteListings().map { list -> list.map { it.toDomain() } }

    override suspend fun reportListing(report: com.tabdal.android.domain.models.Report): Result<Unit> = runCatching {
        api.submitReport(mapOf(
            "target_id" to report.targetId,
            "target_type" to report.targetType.name,
            "reason" to report.reason.name,
            "details" to report.details
        ))
        Result.Success(Unit)
    }.getOrElse { Result.Error(it.message ?: "Erreur") }

    private fun ListingDto.toDomain() = Listing(
        id, title, description, price, isNegotiable, surface, rooms, bathrooms, floor, yearBuilt,
        try { PropertyCondition.valueOf(condition) } catch (e: Exception) { PropertyCondition.GOOD },
        try { PropertyType.valueOf(type) } catch (e: Exception) { PropertyType.APARTMENT },
        try { TransactionType.valueOf(transactionType) } catch (e: Exception) { TransactionType.SALE },
        try { ListingStatus.valueOf(status) } catch (e: Exception) { ListingStatus.PUBLISHED },
        city, district, address, latitude, longitude, photoUrls, sellerId, sellerName,
        sellerPhone, sellerSince, viewCount, messageCount, false, createdAt, expiresAt
    )

    private fun ListingEntity.toDomain() = Listing(
        id, title, description, price, isNegotiable, surface, rooms, bathrooms, floor, yearBuilt,
        try { PropertyCondition.valueOf(condition) } catch (e: Exception) { PropertyCondition.GOOD },
        try { PropertyType.valueOf(type) } catch (e: Exception) { PropertyType.APARTMENT },
        try { TransactionType.valueOf(transactionType) } catch (e: Exception) { TransactionType.SALE },
        try { ListingStatus.valueOf(status) } catch (e: Exception) { ListingStatus.PUBLISHED },
        city, district, address, latitude, longitude,
        try { org.json.JSONArray(photoUrlsJson).let { arr -> (0 until arr.length()).map { arr.getString(it) } } } catch (e: Exception) { emptyList() },
        sellerId, sellerName, "", 0L, viewCount, 0, isFavorite, createdAt, expiresAt
    )

    private fun ListingDto.toEntity() = ListingEntity(
        id, title, description, price, isNegotiable, surface, rooms, bathrooms, floor, yearBuilt,
        condition, type, transactionType, status, city, district, address, latitude, longitude,
        org.json.JSONArray(photoUrls).toString(), sellerId, sellerName, viewCount, false, createdAt, expiresAt
    )
}
