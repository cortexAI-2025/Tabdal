package com.tabdal.android.presentation.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tabdal.android.presentation.ui.components.*
import com.tabdal.android.presentation.viewmodels.ProfileViewModel

@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle(initialValue = null)

    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            fullName = user.fullName
            phone = user.phone
            city = user.city
        }
    }

    LaunchedEffect(uiState.success) {
        if (uiState.success) { viewModel.clearSuccess(); onBack() }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabdalTopBar(title = "Modifier le profil", onBack = onBack)

        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))

            currentUser?.let { AvatarInitials(it.fullName, size = 72) }

            Spacer(Modifier.height(8.dp))

            TabdalTextField(value = fullName, onValueChange = { fullName = it }, label = "Nom complet")
            TabdalTextField(value = phone, onValueChange = { phone = it }, label = "Téléphone",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
            TabdalTextField(value = city, onValueChange = { city = it }, label = "Ville")

            uiState.error?.let { error ->
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Text(error, color = MaterialTheme.colorScheme.onErrorContainer, modifier = Modifier.padding(12.dp))
                }
            }
        }

        Surface(shadowElevation = 8.dp) {
            TabdalButton(
                text = "Enregistrer",
                onClick = {
                    currentUser?.let { user ->
                        viewModel.updateProfile(
                            user.copy(fullName = fullName, phone = phone, city = city),
                            photoUri = null
                        )
                    }
                },
                isLoading = uiState.isLoading,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
