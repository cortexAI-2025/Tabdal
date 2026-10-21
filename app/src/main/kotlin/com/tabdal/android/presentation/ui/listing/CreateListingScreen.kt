package com.tabdal.android.presentation.ui.listing

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

    var step by remember { mutableIntStateOf(0) }
    val stepCount = 5

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

    val photoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        val remaining = Constants.MAX_PHOTOS - selectedPhotos.size
        selectedPhotos = selectedPhotos + uris.take(remaining)
    }

    LaunchedEffect(uiState.success) {
        if (uiState.success) { viewModel.clearSuccess(); onCreated() }
    }

    val canContinue = when (step) {
        0 -> title.isNotBlank()
        2 -> city.isNotBlank()
        3 -> (price.toLongOrNull() ?: 0L) > 0 && (surface.toDoubleOrNull() ?: 0.0) > 0
        else -> true
    }
    val goBack = { if (step == 0) onBack() else step-- }

    Column(Modifier.fillMaxSize()) {
        // Barre : retour / fermer + progression segmentée
        Row(Modifier.fillMaxWidth().padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = goBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Retour") }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onBack) { Icon(Icons.Default.Close, "Fermer") }
        }
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(stepCount) { i ->
                Box(Modifier.weight(1f).height(4.dp).clip(RoundedCornerShape(2.dp)).background(
                    if (i <= step) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant))
            }
        }

        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            when (step) {
                0 -> {
                    StepTitle("Commençons par l'essentiel !")
                    Text("*Champs obligatoires", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    TabdalTextField(value = title, onValueChange = { if (it.length <= 200) title = it },
                        label = "Quel est le titre de votre annonce ? *")
                    Text("${title.length}/200", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.align(Alignment.End))
                }
                1 -> {
                    StepTitle("Ajoutez des photos")
                    Text("Mettez en valeur votre bien ! Plus il y a de détails, plus votre annonce sera de qualité.",
                        style = MaterialTheme.typography.bodyMedium)
                    Text("Vos photos (max ${Constants.MAX_PHOTOS})", style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold)
                    val slots = Constants.MAX_PHOTOS
                    val items: List<Uri?> = (0 until slots).map { selectedPhotos.getOrNull(it) }
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Surface(shape = RoundedCornerShape(16.dp), shadowElevation = 4.dp,
                            modifier = Modifier.fillMaxWidth().height(110.dp).clickable {
                                if (selectedPhotos.size < Constants.MAX_PHOTOS) photoLauncher.launch("image/*") }) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                Icon(Icons.Outlined.AddAPhoto, null, Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.height(6.dp))
                                Text("Ajouter des photos", fontWeight = FontWeight.SemiBold)
                            }
                        }
                        items.chunked(2).forEachIndexed { row, pair ->
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                pair.forEachIndexed { col, uri ->
                                    val idx = row * 2 + col
                                    Box(Modifier.weight(1f).height(120.dp).clip(RoundedCornerShape(16.dp))
                                        .border(if (idx == 0) 2.dp else 1.dp,
                                            if (idx == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                            RoundedCornerShape(16.dp))
                                        .clickable { if (uri == null) photoLauncher.launch("image/*") },
                                        contentAlignment = Alignment.Center) {
                                        if (uri != null) {
                                            AsyncImage(model = uri, contentDescription = null, contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize())
                                            IconButton(onClick = { selectedPhotos = selectedPhotos - uri },
                                                modifier = Modifier.align(Alignment.TopEnd).size(28.dp)
                                                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f), CircleShape)) {
                                                Icon(Icons.Default.Close, null, Modifier.size(14.dp))
                                            }
                                        } else {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Icon(Icons.Outlined.PhotoCamera, null, Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
                                                Text("Photo n°${idx + 1}", color = MaterialTheme.colorScheme.primary)
                                            }
                                        }
                                        if (idx == 0) Surface(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(50),
                                            modifier = Modifier.align(Alignment.TopStart).padding(6.dp)) {
                                            Text("Photo de couverture", color = Color.White, style = MaterialTheme.typography.labelSmall,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                                        }
                                    }
                                }
                                if (pair.size == 1) Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
                2 -> {
                    StepTitle("Où se situe votre bien ?")
                    TabdalTextField(value = city, onValueChange = { city = it }, label = "Ville *")
                    TabdalTextField(value = district, onValueChange = { district = it }, label = "Quartier")
                    TabdalTextField(value = address, onValueChange = { address = it }, label = "Numéro, nom de rue")
                }
                3 -> {
                    StepTitle("Informations du bien")
                    EnumDropdown("Type de bien *", selectedType, PropertyType.entries, { it.labelFr }) { selectedType = it }
                    EnumDropdown("Transaction *", selectedTransaction, TransactionType.entries, { it.labelFr }) { selectedTransaction = it }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        TabdalTextField(value = price, onValueChange = { price = it }, label = "Prix (MAD) *",
                            modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                        TabdalTextField(value = surface, onValueChange = { surface = it }, label = "Surface (m²) *",
                            modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isNegotiable, onCheckedChange = { isNegotiable = it })
                        Text("Prix négociable")
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        TabdalTextField(value = rooms, onValueChange = { rooms = it }, label = "Chambres",
                            modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                        TabdalTextField(value = bathrooms, onValueChange = { bathrooms = it }, label = "SDB",
                            modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        TabdalTextField(value = floor, onValueChange = { floor = it }, label = "Étage",
                            modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                        TabdalTextField(value = yearBuilt, onValueChange = { yearBuilt = it }, label = "Année",
                            modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    }
                    EnumDropdown("État du bien", selectedCondition, PropertyCondition.entries, { it.labelFr }) { selectedCondition = it }
                }
                else -> {
                    StepTitle("Décrivez votre bien")
                    TabdalTextField(value = description, onValueChange = { description = it }, label = "Description",
                        singleLine = false, minLines = 6, maxLines = 12)
                    uiState.error?.let { error ->
                        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                            Text(error, color = MaterialTheme.colorScheme.onErrorContainer, modifier = Modifier.padding(12.dp))
                        }
                    }
                }
            }
        }

        Surface(shadowElevation = 8.dp) {
            TabdalButton(
                text = if (step == stepCount - 1) "Publier l'annonce" else "Continuer",
                enabled = canContinue,
                onClick = {
                    if (step < stepCount - 1) step++ else {
                        val listing = Listing(
                            id = "", title = title, description = description,
                            price = price.toLongOrNull() ?: 0L, isNegotiable = isNegotiable,
                            surface = surface.toDoubleOrNull() ?: 0.0,
                            rooms = rooms.toIntOrNull() ?: 0, bathrooms = bathrooms.toIntOrNull() ?: 0,
                            floor = floor.toIntOrNull(), yearBuilt = yearBuilt.toIntOrNull(),
                            condition = selectedCondition, type = selectedType,
                            transactionType = selectedTransaction,
                            city = city, district = district, address = address, sellerId = userId
                        )
                        viewModel.createListing(listing, selectedPhotos)
                    }
                },
                isLoading = uiState.isLoading,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun StepTitle(text: String) {
    Text(text, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> EnumDropdown(label: String, value: T, options: List<T>, labelOf: (T) -> String, onSelect: (T) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = labelOf(value), onValueChange = {}, readOnly = true, label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
            shape = RoundedCornerShape(28.dp)
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { opt ->
                DropdownMenuItem(text = { Text(labelOf(opt)) }, onClick = { onSelect(opt); expanded = false })
            }
        }
    }
}
