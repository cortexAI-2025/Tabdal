package com.tabdal.android.presentation.ui.listing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tabdal.android.domain.models.Report
import com.tabdal.android.domain.models.ReportReason
import com.tabdal.android.domain.models.ReportTarget
import com.tabdal.android.presentation.ui.components.*
import com.tabdal.android.presentation.viewmodels.ListingViewModel
import com.tabdal.android.utils.Extensions.toFormattedDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingDetailScreen(
    listingId: String,
    onBack: () -> Unit,
    onContactSeller: (String) -> Unit,
    viewModel: ListingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showReportDialog by remember { mutableStateOf(false) }
    var selectedReason by remember { mutableStateOf(ReportReason.OTHER) }

    LaunchedEffect(listingId) { viewModel.loadListingDetail(listingId) }

    LaunchedEffect(uiState.success) {
        if (uiState.success) viewModel.clearSuccess()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabdalTopBar(
            title = "",
            onBack = onBack,
            actions = {
                IconButton(onClick = { viewModel.toggleFavorite(listingId) }) {
                    Icon(
                        imageVector = if (uiState.listing?.isFavorite == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favori",
                        tint = if (uiState.listing?.isFavorite == true) MaterialTheme.colorScheme.secondary
                               else MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = { showReportDialog = true }) {
                    Icon(Icons.Default.Flag, "Signaler", tint = MaterialTheme.colorScheme.onSurface)
                }
            }
        )

        when {
            uiState.isLoading -> LoadingView()
            uiState.error != null -> ErrorView(uiState.error!!) { viewModel.loadListingDetail(listingId) }
            uiState.listing != null -> {
                val listing = uiState.listing!!
                LazyColumn(modifier = Modifier.weight(1f)) {
                    // Photo carousel
                    item {
                        if (listing.photoUrls.isNotEmpty()) {
                            val pagerState = rememberPagerState { listing.photoUrls.size }
                            Box {
                                HorizontalPager(state = pagerState) { page ->
                                    AsyncImage(
                                        model = listing.photoUrls[page],
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxWidth().height(280.dp)
                                    )
                                }
                                if (listing.photoUrls.size > 1) {
                                    Surface(
                                        modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                                    ) {
                                        Text("${pagerState.currentPage + 1}/${listing.photoUrls.size}",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelMedium)
                                    }
                                }
                            }
                        } else {
                            Box(modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(0.dp)),
                                contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Home, null, modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    // Content
                    item {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            // Title + price
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(listing.type.labelFr, style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.secondary)
                                    Text(listing.status.labelFr, style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Text(listing.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                                PriceTag(listing.price, listing.isNegotiable)
                            }

                            // Location
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.secondary)
                                Text(
                                    text = listOf(listing.address, listing.district, listing.city).filter { it.isNotBlank() }.joinToString(", "),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            HorizontalDivider()

                            // Characteristics grid
                            Text("Caractéristiques", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                PropertyRow("Surface", "${listing.surface.toInt()} m²")
                                if (listing.rooms > 0) PropertyRow("Chambres", "${listing.rooms}")
                                if (listing.bathrooms > 0) PropertyRow("Salles de bain", "${listing.bathrooms}")
                                listing.floor?.let { PropertyRow("Étage", "$it") }
                                listing.yearBuilt?.let { PropertyRow("Année", "$it") }
                                PropertyRow("État", listing.condition.labelFr)
                                PropertyRow("Transaction", listing.transactionType.labelFr)
                            }

                            HorizontalDivider()

                            // Description
                            if (listing.description.isNotBlank()) {
                                Text("Description", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Text(listing.description, style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                                HorizontalDivider()
                            }

                            // Seller info
                            Text("Vendeur", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    AvatarInitials(listing.sellerName, size = 48)
                                    Column {
                                        Text(listing.sellerName, fontWeight = FontWeight.SemiBold)
                                        if (listing.sellerSince > 0) {
                                            Text("Membre depuis ${listing.sellerSince.toFormattedDate()}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                }
                            }

                            // Legal disclaimer
                            LegalDisclaimerBanner()
                        }
                    }
                }

                // Bottom action bar
                Surface(shadowElevation = 8.dp) {
                    Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = { onContactSeller(listing.sellerId) },
                            modifier = Modifier.weight(1f).height(52.dp)
                        ) {
                            Icon(Icons.Default.Message, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Contacter")
                        }
                    }
                }
            }
        }
    }

    // Report dialog
    if (showReportDialog) {
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = { Text("Signaler cette annonce") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    ReportReason.entries.forEach { reason ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = selectedReason == reason, onClick = { selectedReason = reason })
                            Spacer(Modifier.width(8.dp))
                            Text(reason.labelFr)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val userId = viewModel.currentUserId() ?: ""
                    viewModel.reportListing(Report(
                        targetId = listingId, targetType = ReportTarget.LISTING,
                        reporterId = userId, reason = selectedReason
                    ))
                    showReportDialog = false
                }) { Text("Signaler") }
            },
            dismissButton = { TextButton(onClick = { showReportDialog = false }) { Text("Annuler") } }
        )
    }
}

@Composable
private fun PropertyRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
        Text(value, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.bodyMedium)
    }
}

private fun Long.toFormattedDate(): String {
    val sdf = java.text.SimpleDateFormat("MMMM yyyy", java.util.Locale("fr"))
    return sdf.format(java.util.Date(this))
}
