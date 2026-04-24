package com.tabdal.android.presentation.ui.listing

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tabdal.android.domain.models.*
import com.tabdal.android.presentation.ui.components.*
import com.tabdal.android.presentation.viewmodels.ListingViewModel
import com.tabdal.android.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListingScreen(
    onBack: () -> Unit,
    onCreated: () -> Unit,
    viewModel: ListingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userId = viewModel.currentUserId() ?: ""

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var isNegotiable by remember { mutableStateOf(false) }
    var surface by remember { mutableStateOf("") }
    var rooms by remember { mutableStateOf("") }
    var bathrooms by remember { mutableStateOf("") }
    var floor by remember { mutableStateOf("") }
    var yearBuilt by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(PropertyType.APARTMENT) }
    var selectedTransaction by remember { mutableStateOf(TransactionType.SALE) }
    var selectedCondition by remember { mutableStateOf(PropertyCondition.GOOD) }
    var selectedPhotos by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val photoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        val remaining = Constants.MAX_PHOTOS - selectedPhotos.size
        selectedPhotos = selectedPhotos + uris.take(remaining)
    }

    LaunchedEffect(uiState.success) {
        if (uiState.success) { viewModel.clearSuccess(); onCreated() }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabdalTopBar(title = "Créer une annonce", onBack = onBack)

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {

            // Section: Category
            SectionHeader("Catégorie")
            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = {}
            ) {
                var typeExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = typeExpanded, onExpandedChange = { typeExpanded = it }) {
                    OutlinedTextField(
                        value = selectedType.labelFr, onValueChange = {},
                        readOnly = true, label = { Text("Type de bien *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable), shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }) {
                        PropertyType.entries.forEach { type ->
                            DropdownMenuItem(text = { Text(type.labelFr) }, onClick = { selectedType = type; typeExpanded = false })
                        }
                    }
                }
            }

            var txExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = txExpanded, onExpandedChange = { txExpanded = it }) {
                OutlinedTextField(
                    value = selectedTransaction.labelFr, onValueChange = {},
                    readOnly = true, label = { Text("Transaction *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = txExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable), shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(expanded = txExpanded, onDismissRequest = { txExpanded = false }) {
                    TransactionType.entries.forEach { tx ->
                        DropdownMenuItem(text = { Text(tx.labelFr) }, onClick = { selectedTransaction = tx; txExpanded = false })
                    }
                }
            }

            HorizontalDivider()

            // Section: Photos
            SectionHeader("Photos de l'annonce (max ${Constants.MAX_PHOTOS})")
            if (selectedPhotos.isEmpty()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth().height(120.dp).clickable {
                        if (selectedPhotos.size < Constants.MAX_PHOTOS) photoLauncher.launch("image/*")
                    }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(4.dp))
                        Text("Ajouter jusqu'à ${Constants.MAX_PHOTOS} photos", style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(selectedPhotos) { uri ->
                        Box(modifier = Modifier.size(100.dp)) {
                            AsyncImage(model = uri, contentDescription = null, contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)))
                            IconButton(
                                onClick = { selectedPhotos = selectedPhotos - uri },
                                modifier = Modifier.align(Alignment.TopEnd).size(24.dp)
                                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                            ) { Icon(Icons.Default.Close, null, modifier = Modifier.size(14.dp)) }
                        }
                    }
                    if (selectedPhotos.size < Constants.MAX_PHOTOS) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.size(100.dp).clickable { photoLauncher.launch("image/*") }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Add, null, tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            }

            HorizontalDivider()

            // Section: Address
            SectionHeader("Localisation")
            TabdalTextField(value = city, onValueChange = { city = it }, label = "Ville *")
            TabdalTextField(value = district, onValueChange = { district = it }, label = "Quartier")
            TabdalTextField(value = address, onValueChange = { address = it }, label = "Adresse")

            HorizontalDivider()

            // Section: Details
            SectionHeader("Informations du bien")
            TabdalTextField(value = title, onValueChange = { title = it }, label = "Titre de l'annonce *")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TabdalTextField(value = price, onValueChange = { price = it }, label = "Prix (MAD) *",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                TabdalTextField(value = surface, onValueChange = { surface = it }, label = "Surface (m²) *",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isNegotiable, onCheckedChange = { isNegotiable = it })
                Spacer(Modifier.width(8.dp))
                Text("Prix négociable")
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TabdalTextField(value = rooms, onValueChange = { rooms = it }, label = "Chambres",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                TabdalTextField(value = bathrooms, onValueChange = { bathrooms = it }, label = "SDB",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TabdalTextField(value = floor, onValueChange = { floor = it }, label = "Étage",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                TabdalTextField(value = yearBuilt, onValueChange = { yearBuilt = it }, label = "Année",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }

            // Condition
            var conditionExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = conditionExpanded, onExpandedChange = { conditionExpanded = it }) {
                OutlinedTextField(
                    value = selectedCondition.labelFr, onValueChange = {},
                    readOnly = true, label = { Text("État du bien") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = conditionExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable), shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(expanded = conditionExpanded, onDismissRequest = { conditionExpanded = false }) {
                    PropertyCondition.entries.forEach { cond ->
                        DropdownMenuItem(text = { Text(cond.labelFr) }, onClick = { selectedCondition = cond; conditionExpanded = false })
                    }
                }
            }

            TabdalTextField(value = description, onValueChange = { description = it }, label = "Description",
                singleLine = false, minLines = 4, maxLines = 8)

            uiState.error?.let { error ->
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Text(error, color = MaterialTheme.colorScheme.onErrorContainer, modifier = Modifier.padding(12.dp))
                }
            }
        }

        // Submit button
        Surface(shadowElevation = 8.dp) {
            TabdalButton(
                text = "Publier l'annonce",
                onClick = {
                    val listing = Listing(
                        id = "", title = title, description = description,
                        price = price.toLongOrNull() ?: 0L,
                        isNegotiable = isNegotiable,
                        surface = surface.toDoubleOrNull() ?: 0.0,
                        rooms = rooms.toIntOrNull() ?: 0,
                        bathrooms = bathrooms.toIntOrNull() ?: 0,
                        floor = floor.toIntOrNull(),
                        yearBuilt = yearBuilt.toIntOrNull(),
                        condition = selectedCondition, type = selectedType,
                        transactionType = selectedTransaction,
                        city = city, district = district, address = address,
                        sellerId = userId
                    )
                    viewModel.createListing(listing, selectedPhotos)
                },
                isLoading = uiState.isLoading,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary)
}
