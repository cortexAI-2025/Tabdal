package com.tabdal.android.presentation.ui.listing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tabdal.android.domain.models.Listing
import com.tabdal.android.domain.models.ListingStatus
import com.tabdal.android.presentation.theme.Success
import com.tabdal.android.presentation.theme.Warning
import com.tabdal.android.presentation.theme.ErrorRed
import com.tabdal.android.presentation.ui.components.*
import com.tabdal.android.presentation.viewmodels.ListingViewModel

@Composable
fun MyListingsScreen(
    onCreateListing: () -> Unit,
    onEditListing: (String) -> Unit,
    onListingClick: (String) -> Unit,
    viewModel: ListingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.loadMyListings() }

    Column(modifier = Modifier.fillMaxSize()) {
        TabdalTopBar(
            title = "Mes annonces",
            actions = {
                IconButton(onClick = onCreateListing) {
                    Icon(Icons.Default.Add, "Créer")
                }
            }
        )

        when {
            uiState.isLoading -> LoadingView()
            uiState.myListings.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Aucune annonce", style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = onCreateListing) {
                            Icon(Icons.Default.Add, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Créer une annonce")
                        }
                    }
                }
            }
            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.myListings) { listing ->
                        MyListingCard(
                            listing = listing,
                            onClick = { onListingClick(listing.id) },
                            onEdit = { onEditListing(listing.id) },
                            onDelete = { viewModel.deleteListing(listing.id) },
                            onMarkSold = { viewModel.markAsSold(listing.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MyListingCard(
    listing: Listing,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onMarkSold: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()) {
                    Text(listing.title, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.weight(1f))
                    val (statusColor, statusLabel) = when (listing.status) {
                        ListingStatus.PUBLISHED -> Pair(Success, "Publié")
                        ListingStatus.DRAFT     -> Pair(Warning, "Brouillon")
                        ListingStatus.SOLD      -> Pair(MaterialTheme.colorScheme.primary, "Vendu")
                        ListingStatus.DISABLED  -> Pair(ErrorRed, "Désactivé")
                    }
                    StatusBadge(statusLabel, statusColor)
                }
                PriceTag(listing.price)
                Text("${listing.surface.toInt()} m² • ${listing.city}", style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("${listing.viewCount} vues", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${listing.messageCount} messages", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Box {
                IconButton(onClick = { showMenu = true }) { Icon(Icons.Default.MoreVert, null) }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(
                        text = { Text("Modifier") },
                        leadingIcon = { Icon(Icons.Default.Edit, null) },
                        onClick = { showMenu = false; onEdit() }
                    )
                    if (listing.status == ListingStatus.PUBLISHED) {
                        DropdownMenuItem(
                            text = { Text("Marquer vendu") },
                            onClick = { showMenu = false; onMarkSold() }
                        )
                    }
                    DropdownMenuItem(
                        text = { Text("Supprimer", color = MaterialTheme.colorScheme.error) },
                        leadingIcon = { Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error) },
                        onClick = { showMenu = false; showDeleteConfirm = true }
                    )
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Supprimer l'annonce") },
            text = { Text("Cette action est irréversible.") },
            confirmButton = {
                Button(onClick = { onDelete(); showDeleteConfirm = false },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                    Text("Supprimer")
                }
            },
            dismissButton = { TextButton(onClick = { showDeleteConfirm = false }) { Text("Annuler") } }
        )
    }
}
