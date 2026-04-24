package com.tabdal.android.data.remote.api

import com.tabdal.android.data.remote.dto.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface TabdalApi {

    // Auth
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body body: Map<String, String>): Map<String, Any>

    @DELETE("auth/account")
    suspend fun deleteAccount(): Map<String, Any>

    @GET("auth/export")
    suspend fun exportData(): Map<String, Any>

    // Listings
    @GET("listings")
    suspend fun getListings(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
        @Query("city") city: String? = null,
        @Query("district") district: String? = null,
        @Query("type") type: String? = null,
        @Query("transaction_type") transactionType: String? = null,
        @Query("price_min") priceMin: Long? = null,
        @Query("price_max") priceMax: Long? = null,
        @Query("surface_min") surfaceMin: Double? = null,
        @Query("rooms_min") roomsMin: Int? = null,
        @Query("sort") sort: String = "createdAt_desc"
    ): ListingsPage

    @GET("listings/{id}")
    suspend fun getListingById(@Path("id") id: String): ListingDto

    @GET("listings/my")
    suspend fun getMyListings(): List<ListingDto>

    @Multipart
    @POST("listings")
    suspend fun createListing(
        @Part("data") data: RequestBody,
        @Part photos: List<MultipartBody.Part>
    ): ListingDto

    @Multipart
    @PUT("listings/{id}")
    suspend fun updateListing(
        @Path("id") id: String,
        @Part("data") data: RequestBody,
        @Part photos: List<MultipartBody.Part>
    ): ListingDto

    @DELETE("listings/{id}")
    suspend fun deleteListing(@Path("id") id: String): Map<String, Any>

    @PUT("listings/{id}/sold")
    suspend fun markAsSold(@Path("id") id: String): ListingDto

    @PUT("listings/{id}/renew")
    suspend fun renewListing(@Path("id") id: String): ListingDto

    @POST("listings/{id}/favorite")
    suspend fun toggleFavorite(@Path("id") id: String): Map<String, Any>

    @GET("listings/favorites")
    suspend fun getFavorites(): List<ListingDto>

    // Messages
    @GET("conversations")
    suspend fun getConversations(): List<ConversationDto>

    @GET("conversations/{id}/messages")
    suspend fun getMessages(@Path("id") conversationId: String): List<MessageDto>

    @POST("conversations/{id}/messages")
    suspend fun sendMessage(
        @Path("id") conversationId: String,
        @Body request: SendMessageRequest
    ): MessageDto

    @POST("conversations")
    suspend fun startConversation(@Body request: SendMessageRequest): ConversationDto

    @PUT("conversations/{id}/read")
    suspend fun markConversationRead(@Path("id") conversationId: String): Map<String, Any>

    // Reports
    @POST("reports")
    suspend fun submitReport(@Body report: Map<String, Any>): Map<String, Any>

    // User profile
    @GET("users/me")
    suspend fun getProfile(): UserDto

    @Multipart
    @PUT("users/me")
    suspend fun updateProfile(
        @Part("data") data: RequestBody,
        @Part photo: MultipartBody.Part?
    ): UserDto

    @PUT("users/me/password")
    suspend fun changePassword(@Body body: Map<String, String>): Map<String, Any>

    // Admin
    @GET("admin/stats")
    suspend fun getAdminStats(): Map<String, Any>

    @PUT("admin/users/{id}/block")
    suspend fun blockUser(@Path("id") userId: String): Map<String, Any>

    @DELETE("admin/users/{id}")
    suspend fun deleteUser(@Path("id") userId: String): Map<String, Any>
}
