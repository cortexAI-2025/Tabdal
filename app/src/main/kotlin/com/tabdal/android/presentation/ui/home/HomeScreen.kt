package com.tabdal.android.presentation.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.tabdal.android.domain.models.PropertyType
import com.tabdal.android.domain.models.ListingFilter
import com.tabdal.android.presentation.ui.components.BalalLogoBar
import com.tabdal.android.presentation.ui.components.ErrorView
import com.tabdal.android.presentation.ui.components.ListingCard
import com.tabdal.android.presentation.ui.components.LoadingView
import com.tabdal.android.presentation.viewmodels.ListingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onListingClick: (String) -> Unit,
    onFilterClick: () -> Unit,
    viewModel: ListingViewModel = hiltViewModel()
) {
    val listings = viewModel.listings.collectAsLazyPagingItems()
    val filter by viewModel.filter.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }
    val selectedType = remember { mutableStateOf<PropertyType?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        BalalLogoBar()

        // Recherche (pastille grise) + filtres
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.applyFilter(filter.copy(city = it))
                },
                placeholder = { Text("Rechercher sur BALAL (ville, quartier…)") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                singleLine = true,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            IconButton(onClick = onFilterClick) {
                Icon(Icons.Default.FilterList, "Filtres", tint = MaterialTheme.colorScheme.primary)
            }
        }

        // Catégories : pastilles rondes
        val categories = listOf<Triple<String, ImageVector, PropertyType?>>(
            Triple("Tous", Icons.Outlined.Home, null),
            Triple("Appartement", Icons.Outlined.Apartment, PropertyType.APARTMENT),
            Triple("Villa", Icons.Outlined.Villa, PropertyType.VILLA),
            Triple("Terrain", Icons.Outlined.Terrain, PropertyType.LAND),
            Triple("Commerce", Icons.Outlined.Storefront, PropertyType.COMMERCIAL),
            Triple("Bureau", Icons.Outlined.Business, PropertyType.OFFICE)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories.size) { i ->
                val (label, icon, type) = categories[i]
                val selected = selectedType.value == type
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        selectedType.value = type
                        viewModel.applyFilter(filter.copy(propertyType = type))
                    }
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(icon, label, modifier = Modifier.size(26.dp),
                                tint = if (selected) Color.White else MaterialTheme.colorScheme.primary)
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(label, style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium)
                }
            }
        }

        // Listings
        when (listings.loadState.refresh) {
            is LoadState.Loading -> LoadingView()
            is LoadState.Error   -> ErrorView("Impossible de charger les annonces") { listings.retry() }
            else -> {
                if (listings.itemCount == 0) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Aucune annonce trouvée", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(listings.itemCount) { index ->
                            listings[index]?.let { listing ->
                                ListingCard(
                                    listing = listing,
                                    onClick = { onListingClick(listing.id) },
                                    onFavoriteToggle = { viewModel.toggleFavorite(listing.id) }
                                )
                            }
                        }
                        if (listings.loadState.append is LoadState.Loading) {
                            item {
                                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
