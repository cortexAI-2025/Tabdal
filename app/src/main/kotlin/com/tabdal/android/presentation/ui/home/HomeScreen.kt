package com.tabdal.android.presentation.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.tabdal.android.domain.models.PropertyType
import com.tabdal.android.domain.models.ListingFilter
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
        // Header
        Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 2.dp) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("TABDAL", style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                        Text("immobilier Maroc", style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary)
                    }
                    IconButton(onClick = onFilterClick) {
                        BadgedBox(badge = {}) {
                            Icon(Icons.Default.FilterList, "Filtres", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.applyFilter(filter.copy(city = it))
                    },
                    placeholder = { Text("Rechercher par ville, quartier…") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }
        }

        // Type filter chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedType.value == null,
                    onClick = { selectedType.value = null; viewModel.applyFilter(filter.copy(propertyType = null)) },
                    label = { Text("Tous") }
                )
            }
            items(PropertyType.entries.size) { index ->
                val type = PropertyType.entries[index]
                FilterChip(
                    selected = selectedType.value == type,
                    onClick = {
                        selectedType.value = if (selectedType.value == type) null else type
                        viewModel.applyFilter(filter.copy(propertyType = selectedType.value))
                    },
                    label = { Text(type.labelFr) }
                )
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
