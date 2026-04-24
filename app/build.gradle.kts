import java.util.Properties

// ── Lecture de local.properties (clés API, secrets CI) ──────────────────────
val localProperties = Properties().also { props ->
    rootProject.file("local.properties").takeIf { it.exists() }
        ?.inputStream()?.use { props.load(it) }
}

fun localProp(key: String, fallback: String = ""): String =
    localProperties.getProperty(key, fallback)

// ────────────────────────────────────────────────────────────────────────────

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.tabdal.android"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tabdal.android"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // ── API keys lues depuis local.properties (injectées par CI ou .env local) ──
        val mapsApiKey = localProp("MAPS_API_KEY", "YOUR_MAPS_API_KEY")
        val baseUrl    = localProp("BASE_URL", "https://api.tabdal.ma/v1/")
        val useMock    = localProp("USE_MOCK", "false").toBoolean()

        // Manifest placeholder pour Google Maps SDK
        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey

        // BuildConfig pour accès côté Kotlin
        buildConfigField("String",  "BASE_URL",    "\"$baseUrl\"")
        buildConfigField("String",  "MAPS_API_KEY", "\"$mapsApiKey\"")
        buildConfigField("Boolean", "USE_MOCK",     "$useMock")
    }

    signingConfigs {
        create("release") {
            // Renseigné via -Pandroid.injected.signing.* dans le workflow CI,
            // ou via les propriétés ci-dessous pour une signature locale.
            val keystorePath = localProp("KEYSTORE_PATH")
            val keystorePass = localProp("KEYSTORE_PASSWORD")
            val keyAlias     = localProp("KEY_ALIAS")
            val keyPass      = localProp("KEY_PASSWORD")

            if (keystorePath.isNotBlank()) {
                storeFile     = file(keystorePath)
                storePassword = keystorePass
                this.keyAlias     = keyAlias
                keyPassword   = keyPass
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled   = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // N'utilise la config release que si un keystore est défini,
            // sinon Gradle utilisera le signing injecté par -Pandroid.injected.*
            val keystorePath = localProp("KEYSTORE_PATH")
            if (keystorePath.isNotBlank()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
        debug {
            isDebuggable       = true
            versionNameSuffix  = "-debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose     = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core AndroidX
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.activity.compose)
    implementation(libs.splashscreen)

    // Compose BOM
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons)
    implementation(libs.compose.foundation)

    // Navigation
    implementation(libs.nav.compose)

    // Hilt DI
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.room.paging)

    // Network
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.moshi)
    ksp(libs.moshi.codegen)

    // Images
    implementation(libs.coil.compose)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.analytics)

    // Maps
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    // Paging
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    // DataStore
    implementation(libs.datastore.prefs)

    // Coroutines
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.play.services)

    // Accompanist
    implementation(libs.accompanist.permissions)

    // Lottie
    implementation(libs.lottie.compose)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
}
