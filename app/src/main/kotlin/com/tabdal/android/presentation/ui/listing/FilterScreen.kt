package com.tabdal.android.presentation.ui.listing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tabdal.android.domain.models.*
import com.tabdal.android.presentation.ui.components.TabdalButton
import com.tabdal.android.presentation.ui.components.TabdalTextField
import com.tabdal.android.presentation.ui.components.TabdalTopBar
import com.tabdal.android.presentation.viewmodels.ListingViewModel

@Composable
fun FilterScreen(
    onBack: () -> Unit,
    onApply: () -> Unit,
    viewModel: ListingViewModel = hiltViewModel()
) {
    val currentFilter by viewModel.filter.collectAsStateWithLifecycle()

    var city by remember { mutableStateOf(currentFilter.city) }
    var district by remember { mutableStateOf(currentFilter.district) }
    var priceMin by remember { mutableStateOf(currentFilter.priceMin?.toString() ?: "") }
    var priceMax by remember { mutableStateOf(currentFilter.priceMax?.toString() ?: "") }
    var surfaceMin by remember { mutableStateOf(currentFilter.surfaceMin?.toString() ?: "") }
    var selectedType by remember { mutableStateOf(currentFilter.propertyType) }
    var selectedSort by remember { mutableStateOf(currentFilter.sortBy) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabdalTopBar(title = "Filtres", onBack = onBack)
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Location
            Text("Localisation", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            TabdalTextField(value = city, onValueChange = { city = it }, label = "Ville")
            TabdalTextField(value = district, onValueChange = { district = it }, label = "Quartier")

            HorizontalDivider()

            // Price
            Text("Prix (MAD)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TabdalTextField(value = priceMin, onValueChange = { priceMin = it }, label = "Min",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                TabdalTextField(value = priceMax, onValueChange = { priceMax = it }, label = "Max",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }

            HorizontalDivider()

            // Surface
            Text("Surface (m²)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            TabdalTextField(value = surfaceMin, onValueChange = { surfaceMin = it }, label = "Surface minimum",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

            HorizontalDivider()

            // Property type
            Text("Type de bien", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf(null to "Tous les types") + PropertyType.entries.map { it to it.labelFr }
            }.let { types ->
                types.forEach { (type, label) ->
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        RadioButton(selected = selectedType == type, onClick = { selectedType = type })
                        Spacer(Modifier.width(8.dp))
                        Text(label)
                    }
                }
            }

            HorizontalDivider()

            // Sort
            Text("Trier par", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            SortOption.entries.forEach { sort ->
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    RadioButton(selected = selectedSort == sort, onClick = { selectedSort = sort })
                    Spacer(Modifier.width(8.dp))
                    Text(sort.labelFr)
                }
            }
        }

        // Action buttons
        Surface(shadowElevation = 8.dp) {
            Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = {
                        city = ""; district = ""; priceMin = ""; priceMax = ""; surfaceMin = ""
                        selectedType = null; selectedSort = SortOption.RECENT
                    },
                    modifier = Modifier.weight(1f).height(52.dp)
                ) { Text("Réinitialiser") }
                Button(
                    onClick = {
                        viewModel.applyFilter(ListingFilter(
                            city = city, district = district,
                            propertyType = selectedType,
                            priceMin = priceMin.toLongOrNull(),
                            priceMax = priceMax.toLongOrNull(),
                            surfaceMin = surfaceMin.toDoubleOrNull(),
                            sortBy = selectedSort
                        ))
                        onApply()
                    },
                    modifier = Modifier.weight(1f).height(52.dp)
                ) { Text("Appliquer") }
            }
        }
    }
}
