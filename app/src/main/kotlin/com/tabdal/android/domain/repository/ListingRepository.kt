package com.tabdal.android.domain.repository

import androidx.paging.PagingData
import com.tabdal.android.domain.models.Listing
import com.tabdal.android.domain.models.ListingFilter
import com.tabdal.android.utils.Result
import kotlinx.coroutines.flow.Flow

interface ListingRepository {
    fun getListings(filter: ListingFilter): Flow<PagingData<Listing>>
    suspend fun getListingById(id: String): Result<Listing>
    suspend fun getMyListings(): Result<List<Listing>>
    suspend fun createListing(listing: Listing, photoUris: List<android.net.Uri>): Result<Listing>
    suspend fun updateListing(listing: Listing, newPhotoUris: List<android.net.Uri>): Result<Listing>
    suspend fun deleteListing(id: String): Result<Unit>
    suspend fun markAsSold(id: String): Result<Unit>
    suspend fun renewListing(id: String): Result<Unit>
    suspend fun toggleFavorite(listingId: String): Result<Boolean>
    fun getFavorites(): Flow<List<Listing>>
    suspend fun reportListing(report: com.tabdal.android.domain.models.Report): Result<Unit>
}
