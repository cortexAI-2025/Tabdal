package com.tabdal.android.presentation.ui.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tabdal.android.presentation.ui.components.EmptyState
import com.tabdal.android.presentation.ui.components.ListingCard
import com.tabdal.android.presentation.ui.components.ScreenTitle
import com.tabdal.android.presentation.viewmodels.ListingViewModel

@Composable
fun FavoritesScreen(
    onListingClick: (String) -> Unit,
    viewModel: ListingViewModel = hiltViewModel()
) {
    val favorites by viewModel.favorites.collectAsStateWithLifecycle(initialValue = emptyList())
    var tab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Annonces", "Recherches", "Vendeurs")

    Column(Modifier.fillMaxSize()) {
        ScreenTitle("Favoris")
        TabRow(selectedTabIndex = tab, containerColor = MaterialTheme.colorScheme.surface) {
            tabs.forEachIndexed { i, label ->
                Tab(selected = tab == i, onClick = { tab = i }, text = { Text(label) })
            }
        }
        when (tab) {
            0 -> if (favorites.isEmpty()) {
                EmptyState(Icons.Outlined.FavoriteBorder, "Aucune annonce en favori",
                    "Touchez le cœur d'une annonce pour la retrouver ici.")
            } else LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(favorites, key = { it.id }) { listing ->
                    ListingCard(listing = listing, onClick = { onListingClick(listing.id) },
                        onFavoriteToggle = { viewModel.toggleFavorite(listing.id) })
                }
            }
            1 -> EmptyState(Icons.Outlined.Search, "Vous n'avez pas encore de recherches sauvegardées")
            else -> EmptyState(Icons.Outlined.Person, "Aucun vendeur suivi",
                "Suivez un vendeur pour voir ses nouvelles annonces.")
        }
    }
}
