package com.tabdal.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tabdal.android.presentation.navigation.TabdalNavGraph
import com.tabdal.android.presentation.theme.TabdalTheme
import com.tabdal.android.presentation.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TabdalTheme(darkTheme = isSystemInDarkTheme()) {
                val authViewModel: AuthViewModel = hiltViewModel()
                val isLoggedIn = authViewModel.isLoggedIn

                splashScreen.setKeepOnScreenCondition { false }

                TabdalNavGraph(isLoggedIn = isLoggedIn)
            }
        }
    }
}
