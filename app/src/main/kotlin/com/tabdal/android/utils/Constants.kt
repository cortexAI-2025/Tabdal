package com.tabdal.android.utils

object Constants {
    const val PAGE_SIZE = 20
    const val MAX_PHOTOS = 10
    const val LISTING_DURATION_DAYS = 60
    const val OTP_LENGTH = 6
    const val MIN_PASSWORD_LENGTH = 8
    const val IMAGE_MAX_DIMENSION = 1280
    const val IMAGE_QUALITY = 85

    object Prefs {
        const val AUTH_TOKEN = "auth_token"
        const val USER_ID = "user_id"
        const val NOTIFICATIONS_ENABLED = "notifications_enabled"
        const val DARK_MODE = "dark_mode"
        const val ONBOARDING_DONE = "onboarding_done"
    }

    object Routes {
        const val SPLASH = "splash"
        const val ONBOARDING = "onboarding"
        const val LOGIN = "login"
        const val REGISTER = "register"
        const val OTP = "otp/{phone}/{verificationId}"
        const val FORGOT_PASSWORD = "forgot_password"
        const val HOME = "home"
        const val LISTING_DETAIL = "listing/{id}"
        const val CREATE_LISTING = "create_listing"
        const val EDIT_LISTING = "edit_listing/{id}"
        const val FILTER = "filter"
        const val FAVORITES = "favorites"
        const val CONVERSATIONS = "conversations"
        const val MESSAGES = "messages/{conversationId}"
        const val PROFILE = "profile"
        const val EDIT_PROFILE = "edit_profile"
        const val MY_LISTINGS = "my_listings"
        const val CHANGE_PASSWORD = "change_password"
        const val ADMIN = "admin"
    }
}
