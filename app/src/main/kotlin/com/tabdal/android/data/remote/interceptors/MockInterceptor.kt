package com.tabdal.android.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val path   = chain.request().url.encodedPath.removePrefix("/v1/")
        val method = chain.request().method

        val json = when {
            method == "POST" && path == "auth/login"    -> AUTH_RESPONSE
            method == "POST" && path == "auth/register" -> AUTH_RESPONSE
            method == "DELETE" && path == "auth/account" -> OK
            method == "GET"  && path == "auth/export"   -> EXPORT_DATA
            method == "PUT"  && path == "users/me/password" -> OK
            method == "GET"  && path == "users/me"      -> USER_PROFILE
            method == "PUT"  && path.startsWith("users/me") -> USER_PROFILE
            method == "GET"  && path == "listings/my"   -> MY_LISTINGS
            method == "GET"  && path == "listings/favorites" -> EMPTY_LISTINGS_LIST
            method == "POST" && path == "listings"      -> LISTING_1
            method == "GET"  && path.startsWith("listings/") && !path.contains("/sold") && !path.contains("/renew") && !path.contains("/favorite") -> LISTING_1
            method == "PUT"  && path.endsWith("/sold")  -> LISTING_1
            method == "PUT"  && path.endsWith("/renew") -> LISTING_1
            method == "POST" && path.endsWith("/favorite") -> FAVORITE_TOGGLE
            method == "DELETE" && path.startsWith("listings/") -> OK
            method == "GET"  && path == "listings"      -> LISTINGS_PAGE
            method == "GET"  && path == "conversations" -> CONVERSATIONS
            method == "GET"  && path.contains("/messages") -> MESSAGES
            method == "POST" && path.contains("/messages") -> MESSAGE_SENT
            method == "POST" && path == "conversations" -> CONVERSATION_1
            method == "PUT"  && path.endsWith("/read")  -> OK
            method == "GET"  && path == "admin/stats"   -> ADMIN_STATS
            method == "PUT"  && path.contains("/block") -> OK
            method == "DELETE" && path.startsWith("admin/") -> OK
            method == "POST" && path == "reports"       -> OK
            else -> OK
        }

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .body(json.toResponseBody("application/json".toMediaTypeOrNull()))
            .addHeader("content-type", "application/json")
            .build()
    }

    companion object {
        private val AUTH_RESPONSE = """
        {
          "token": "mock_token_tabdal_demo_2024",
          "user": {
            "_id": "user_demo_001",
            "email": "demo@tabdal.ma",
            "full_name": "Utilisateur Demo",
            "phone": "0661234567",
            "city": "Casablanca",
            "photo_url": null,
            "role": "BUYER",
            "created_at": 1700000000000,
            "is_verified": true
          }
        }""".trimIndent()

        private val USER_PROFILE = """
        {
          "_id": "user_demo_001",
          "email": "demo@tabdal.ma",
          "full_name": "Utilisateur Demo",
          "phone": "0661234567",
          "city": "Casablanca",
          "photo_url": null,
          "role": "BUYER",
          "created_at": 1700000000000,
          "is_verified": true
        }""".trimIndent()

        private val LISTING_1 = """
        {
          "_id": "listing_001",
          "title": "Appartement moderne au Maarif",
          "description": "Bel appartement lumineux en plein cœur du Maarif, proche de tous les commerces. Cuisine équipée, salon spacieux, 3 chambres avec placards encastrés. Résidence sécurisée avec gardien et parking.",
          "price": 1250000,
          "is_negotiable": true,
          "surface": 85.0,
          "rooms": 3,
          "bathrooms": 2,
          "floor": 4,
          "year_built": 2016,
          "condition": "GOOD",
          "type": "APARTMENT",
          "transaction_type": "SALE",
          "status": "PUBLISHED",
          "city": "Casablanca",
          "district": "Maarif",
          "address": "Rue Jean Jaurès, Maarif",
          "latitude": 33.5731,
          "longitude": -7.5898,
          "photo_urls": [],
          "seller_id": "user_seller_001",
          "seller_name": "Mohammed Alami",
          "seller_phone": "0662345678",
          "seller_since": 1680000000000,
          "view_count": 48,
          "message_count": 5,
          "created_at": 1700000000000,
          "expires_at": 1715000000000
        }""".trimIndent()

        private val LISTINGS_PAGE = """
        {
          "data": [
            {
              "_id": "listing_001",
              "title": "Appartement moderne au Maarif",
              "description": "Bel appartement lumineux en plein cœur du Maarif, proche de tous les commerces.",
              "price": 1250000,
              "is_negotiable": true,
              "surface": 85.0,
              "rooms": 3,
              "bathrooms": 2,
              "floor": 4,
              "year_built": 2016,
              "condition": "GOOD",
              "type": "APARTMENT",
              "transaction_type": "SALE",
              "status": "PUBLISHED",
              "city": "Casablanca",
              "district": "Maarif",
              "address": "Rue Jean Jaurès",
              "photo_urls": [],
              "seller_id": "user_seller_001",
              "seller_name": "Mohammed Alami",
              "seller_phone": "0662345678",
              "seller_since": 1680000000000,
              "view_count": 48,
              "message_count": 5,
              "created_at": 1700000000000,
              "expires_at": 1715000000000
            },
            {
              "_id": "listing_002",
              "title": "Villa avec piscine à Ain Diab",
              "description": "Magnifique villa contemporaine avec piscine privée, jardin paysager et vue mer. 5 chambres, dressing, buanderie. Quartier résidentiel calme.",
              "price": 8500000,
              "is_negotiable": true,
              "surface": 350.0,
              "rooms": 5,
              "bathrooms": 3,
              "floor": 0,
              "year_built": 2019,
              "condition": "NEW",
              "type": "VILLA",
              "transaction_type": "SALE",
              "status": "PUBLISHED",
              "city": "Casablanca",
              "district": "Ain Diab",
              "address": "Boulevard de la Corniche",
              "photo_urls": [],
              "seller_id": "user_seller_002",
              "seller_name": "Fatima Benali",
              "seller_phone": "0663456789",
              "seller_since": 1650000000000,
              "view_count": 132,
              "message_count": 12,
              "created_at": 1700100000000,
              "expires_at": 1715100000000
            },
            {
              "_id": "listing_003",
              "title": "Studio meublé en location - Agdal",
              "description": "Studio entièrement meublé et équipé au cœur d'Agdal. Idéal pour étudiant ou jeune professionnel. Toutes charges comprises.",
              "price": 5500,
              "is_negotiable": false,
              "surface": 35.0,
              "rooms": 1,
              "bathrooms": 1,
              "floor": 2,
              "year_built": 2010,
              "condition": "GOOD",
              "type": "APARTMENT",
              "transaction_type": "RENT",
              "status": "PUBLISHED",
              "city": "Rabat",
              "district": "Agdal",
              "address": "Avenue Allal Al Fassi",
              "photo_urls": [],
              "seller_id": "user_seller_003",
              "seller_name": "Karim Tazi",
              "seller_phone": "0664567890",
              "seller_since": 1670000000000,
              "view_count": 76,
              "message_count": 9,
              "created_at": 1700200000000,
              "expires_at": 1715200000000
            },
            {
              "_id": "listing_004",
              "title": "Terrain constructible à Bouskoura",
              "description": "Terrain plat de 500 m² dans une zone résidentielle en plein développement. Viabilisé eau et électricité. Titre foncier propre.",
              "price": 950000,
              "is_negotiable": true,
              "surface": 500.0,
              "rooms": 0,
              "bathrooms": 0,
              "condition": "GOOD",
              "type": "LAND",
              "transaction_type": "SALE",
              "status": "PUBLISHED",
              "city": "Casablanca",
              "district": "Bouskoura",
              "address": "Lotissement Al Wafa",
              "photo_urls": [],
              "seller_id": "user_seller_004",
              "seller_name": "Samir Hajji",
              "seller_phone": "0665678901",
              "seller_since": 1660000000000,
              "view_count": 29,
              "message_count": 2,
              "created_at": 1700300000000,
              "expires_at": 1715300000000
            },
            {
              "_id": "listing_005",
              "title": "Bureau professionnel - Guéliz",
              "description": "Bureau au 5ème étage d'un immeuble professionnel au centre de Guéliz. Espace open space + 2 bureaux fermés + salle de réunion.",
              "price": 18000,
              "is_negotiable": true,
              "surface": 120.0,
              "rooms": 3,
              "bathrooms": 2,
              "floor": 5,
              "year_built": 2012,
              "condition": "RENOVATED",
              "type": "COMMERCIAL",
              "transaction_type": "RENT",
              "status": "PUBLISHED",
              "city": "Marrakech",
              "district": "Guéliz",
              "address": "Avenue Mohammed V",
              "photo_urls": [],
              "seller_id": "user_seller_005",
              "seller_name": "Nadia Chraibi",
              "seller_phone": "0666789012",
              "seller_since": 1690000000000,
              "view_count": 61,
              "message_count": 7,
              "created_at": 1700400000000,
              "expires_at": 1715400000000
            }
          ],
          "total": 5,
          "page": 1,
          "page_size": 20
        }""".trimIndent()

        private val MY_LISTINGS = """
        [
          {
            "_id": "my_listing_001",
            "title": "Mon appartement à vendre",
            "description": "Appartement en excellent état, quartier calme.",
            "price": 750000,
            "is_negotiable": true,
            "surface": 65.0,
            "rooms": 2,
            "bathrooms": 1,
            "floor": 1,
            "year_built": 2008,
            "condition": "GOOD",
            "type": "APARTMENT",
            "transaction_type": "SALE",
            "status": "PUBLISHED",
            "city": "Casablanca",
            "district": "Hay Hassani",
            "address": "Rue Al Massira",
            "photo_urls": [],
            "seller_id": "user_demo_001",
            "seller_name": "Utilisateur Demo",
            "seller_phone": "0661234567",
            "seller_since": 1700000000000,
            "view_count": 12,
            "message_count": 1,
            "created_at": 1700500000000,
            "expires_at": 1715500000000
          }
        ]""".trimIndent()

        private val EMPTY_LISTINGS_LIST = "[]"

        private val CONVERSATIONS = """
        [
          {
            "_id": "conv_001",
            "listing_id": "listing_001",
            "listing_title": "Appartement moderne au Maarif",
            "listing_photo_url": "",
            "buyer_id": "user_demo_001",
            "buyer_name": "Utilisateur Demo",
            "seller_id": "user_seller_001",
            "seller_name": "Mohammed Alami",
            "last_message": "Bonjour, est-ce que le bien est encore disponible ?",
            "last_message_at": 1700600000000,
            "unread_count": 1
          }
        ]""".trimIndent()

        private val CONVERSATION_1 = """
        {
          "_id": "conv_001",
          "listing_id": "listing_001",
          "listing_title": "Appartement moderne au Maarif",
          "listing_photo_url": "",
          "buyer_id": "user_demo_001",
          "buyer_name": "Utilisateur Demo",
          "seller_id": "user_seller_001",
          "seller_name": "Mohammed Alami",
          "last_message": "",
          "last_message_at": 0,
          "unread_count": 0
        }""".trimIndent()

        private val MESSAGES = """
        [
          {
            "_id": "msg_001",
            "conversation_id": "conv_001",
            "sender_id": "user_demo_001",
            "sender_name": "Utilisateur Demo",
            "content": "Bonjour, est-ce que le bien est encore disponible ?",
            "created_at": 1700600000000,
            "is_read": true
          },
          {
            "_id": "msg_002",
            "conversation_id": "conv_001",
            "sender_id": "user_seller_001",
            "sender_name": "Mohammed Alami",
            "content": "Oui bonjour, le bien est toujours disponible. Souhaitez-vous une visite ?",
            "created_at": 1700610000000,
            "is_read": false
          }
        ]""".trimIndent()

        private val MESSAGE_SENT = """
        {
          "_id": "msg_new",
          "conversation_id": "conv_001",
          "sender_id": "user_demo_001",
          "sender_name": "Utilisateur Demo",
          "content": "Message envoyé",
          "created_at": 1700700000000,
          "is_read": false
        }""".trimIndent()

        private val FAVORITE_TOGGLE = """{"is_favorite": true}"""

        private val ADMIN_STATS = """
        {
          "total_users": 247,
          "total_listings": 1043,
          "total_messages": 3821,
          "active_listings": 876,
          "new_users_week": 18,
          "new_listings_week": 64
        }""".trimIndent()

        private val EXPORT_DATA = """{"data": "export_mock_data", "message": "ok"}"""

        private val OK = """{"message": "ok"}"""
    }
}
