package com.tabdal.android.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.compose.*
import com.tabdal.android.presentation.ui.admin.AdminScreen
import com.tabdal.android.presentation.ui.auth.*
import com.tabdal.android.presentation.ui.favorites.FavoritesScreen
import com.tabdal.android.presentation.ui.home.HomeScreen
import com.tabdal.android.presentation.ui.listing.*
import com.tabdal.android.presentation.ui.messaging.*
import com.tabdal.android.presentation.ui.profile.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabdalNavGraph(
    isLoggedIn: Boolean,
    navController: NavHostController = rememberNavController()
) {
    val startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route

    val bottomNavItems = listOf(
        BottomNavItem(Screen.Home.route, "Annonces", Icons.Default.Home),
        BottomNavItem(Screen.Favorites.route, "Favoris", Icons.Default.Favorite),
        BottomNavItem(Screen.CreateListing.route, "Vendre", Icons.Default.AddCircle),
        BottomNavItem(Screen.Conversations.route, "Chat", Icons.Default.ChatBubble),
        BottomNavItem(Screen.Profile.route, "Compte", Icons.Default.Person)
    )

    val mainRoutes = bottomNavItems.map { it.route }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in mainRoutes && isLoggedIn

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                if (item.route == Screen.CreateListing.route) {
                                    Surface(
                                        shape = androidx.compose.foundation.shape.CircleShape,
                                        color = MaterialTheme.colorScheme.primary
                                    ) {
                                        Icon(item.icon, item.label, modifier = Modifier.padding(8.dp),
                                            tint = MaterialTheme.colorScheme.onPrimary)
                                    }
                                } else {
                                    Icon(item.icon, item.label)
                                }
                            },
                            label = {
                                Text(item.label, style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal)
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Auth
            composable(Screen.Login.route) {
                LoginScreen(
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                    onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    onNavigateToLogin = { navController.popBackStack() },
                    onNavigateToOtp = { phone, vid ->
                        navController.navigate(Screen.Otp.createRoute(phone, vid))
                    }
                )
            }
            composable(
                Screen.Otp.route,
                arguments = listOf(
                    navArgument("phone") { type = NavType.StringType },
                    navArgument("verificationId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                OtpScreen(
                    phone = backStackEntry.arguments?.getString("phone") ?: "",
                    verificationId = backStackEntry.arguments?.getString("verificationId") ?: "",
                    onVerified = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.ForgotPassword.route) {
                ForgotPasswordScreen(onBack = { navController.popBackStack() })
            }

            // Main tabs
            composable(Screen.Home.route) {
                HomeScreen(
                    onListingClick = { id -> navController.navigate(Screen.ListingDetail.createRoute(id)) },
                    onFilterClick = { navController.navigate(Screen.Filter.route) }
                )
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    onListingClick = { id -> navController.navigate(Screen.ListingDetail.createRoute(id)) }
                )
            }
            composable(Screen.CreateListing.route) {
                CreateListingScreen(
                    onBack = { navController.popBackStack() },
                    onCreated = {
                        navController.navigate(Screen.MyListings.route) {
                            popUpTo(Screen.Home.route)
                        }
                    }
                )
            }
            composable(Screen.Conversations.route) {
                ConversationListScreen(
                    onConversationClick = { conversationId ->
                        navController.navigate(Screen.Messages.createRoute(conversationId))
                    }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToMyListings = { navController.navigate(Screen.MyListings.route) },
                    onNavigateToEditProfile = { navController.navigate(Screen.EditProfile.route) },
                    onNavigateToChangePassword = { navController.navigate(Screen.ChangePassword.route) },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // Listing
            composable(Screen.ListingDetail.route, arguments = listOf(navArgument("id") { type = NavType.StringType })) { entry ->
                ListingDetailScreen(
                    listingId = entry.arguments?.getString("id") ?: "",
                    onBack = { navController.popBackStack() },
                    onContactSeller = { sellerId ->
                        // Navigate to conversation creation flow
                        navController.navigate(Screen.Conversations.route)
                    }
                )
            }
            composable(Screen.Filter.route) {
                FilterScreen(
                    onBack = { navController.popBackStack() },
                    onApply = { navController.popBackStack() }
                )
            }
            composable(Screen.MyListings.route) {
                MyListingsScreen(
                    onCreateListing = { navController.navigate(Screen.CreateListing.route) },
                    onEditListing = { id -> navController.navigate(Screen.EditListing.createRoute(id)) },
                    onListingClick = { id -> navController.navigate(Screen.ListingDetail.createRoute(id)) }
                )
            }
            composable(Screen.EditListing.route, arguments = listOf(navArgument("id") { type = NavType.StringType })) { entry ->
                CreateListingScreen(
                    onBack = { navController.popBackStack() },
                    onCreated = { navController.popBackStack() }
                )
            }

            // Messages
            composable(Screen.Messages.route, arguments = listOf(navArgument("conversationId") { type = NavType.StringType })) { entry ->
                MessageScreen(
                    conversationId = entry.arguments?.getString("conversationId") ?: "",
                    listingId = "",
                    sellerName = "Conversation",
                    onBack = { navController.popBackStack() }
                )
            }

            // Profile sub-screens
            composable(Screen.EditProfile.route) {
                EditProfileScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.ChangePassword.route) {
                ChangePasswordScreen(onBack = { navController.popBackStack() })
            }

            // Admin
            composable(Screen.Admin.route) {
                AdminScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}

private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
