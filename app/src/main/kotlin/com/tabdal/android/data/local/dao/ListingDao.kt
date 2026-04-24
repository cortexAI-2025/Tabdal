package com.tabdal.android.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.tabdal.android.data.local.entities.ListingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListingDao {
    @Query("SELECT * FROM listings WHERE status = 'PUBLISHED' ORDER BY createdAt DESC")
    fun getPublishedListings(): PagingSource<Int, ListingEntity>

    @Query("SELECT * FROM listings WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteListings(): Flow<List<ListingEntity>>

    @Query("SELECT * FROM listings WHERE sellerId = :sellerId ORDER BY createdAt DESC")
    fun getMyListings(sellerId: String): Flow<List<ListingEntity>>

    @Query("SELECT * FROM listings WHERE id = :id")
    suspend fun getListingById(id: String): ListingEntity?

    @Upsert
    suspend fun upsertListings(listings: List<ListingEntity>)

    @Upsert
    suspend fun upsertListing(listing: ListingEntity)

    @Query("UPDATE listings SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: String, isFavorite: Boolean)

    @Query("DELETE FROM listings WHERE id = :id")
    suspend fun deleteListing(id: String)

    @Query("DELETE FROM listings WHERE status = 'PUBLISHED'")
    suspend fun clearPublishedListings()
}
