package com.tabdal.android.presentation.navigation

sealed class Screen(val route: String) {
    object Login          : Screen("login")
    object Register       : Screen("register")
    object Otp            : Screen("otp/{phone}/{verificationId}") {
        fun createRoute(phone: String, vid: String) = "otp/$phone/$vid"
    }
    object ForgotPassword : Screen("forgot_password")
    object Home           : Screen("home")
    object ListingDetail  : Screen("listing/{id}") {
        fun createRoute(id: String) = "listing/$id"
    }
    object CreateListing  : Screen("create_listing")
    object EditListing    : Screen("edit_listing/{id}") {
        fun createRoute(id: String) = "edit_listing/$id"
    }
    object Filter         : Screen("filter")
    object Favorites      : Screen("favorites")
    object Conversations  : Screen("conversations")
    object Messages       : Screen("messages/{conversationId}") {
        fun createRoute(conversationId: String) = "messages/$conversationId"
    }
    object Profile        : Screen("profile")
    object EditProfile    : Screen("edit_profile")
    object MyListings     : Screen("my_listings")
    object ChangePassword : Screen("change_password")
    object Admin          : Screen("admin")
}
