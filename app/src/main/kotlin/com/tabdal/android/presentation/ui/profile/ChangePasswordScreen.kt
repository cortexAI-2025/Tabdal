package com.tabdal.android.presentation.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tabdal.android.presentation.ui.components.*
import com.tabdal.android.presentation.viewmodels.ProfileViewModel

@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showCurrent by remember { mutableStateOf(false) }
    var showNew by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.success) {
        if (uiState.success) { viewModel.clearSuccess(); onBack() }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabdalTopBar(title = "Modifier le mot de passe", onBack = onBack)
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TabdalTextField(
                value = currentPassword, onValueChange = { currentPassword = it },
                label = "Mot de passe actuel",
                visualTransformation = if (showCurrent) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showCurrent = !showCurrent }) {
                        Icon(if (showCurrent) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                    }
                }
            )
            TabdalTextField(
                value = newPassword, onValueChange = { newPassword = it },
                label = "Nouveau mot de passe",
                visualTransformation = if (showNew) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showNew = !showNew }) {
                        Icon(if (showNew) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                    }
                }
            )
            TabdalTextField(
                value = confirmPassword, onValueChange = { confirmPassword = it },
                label = "Confirmer le nouveau mot de passe",
                visualTransformation = PasswordVisualTransformation()
            )
            uiState.error?.let { error ->
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Text(error, color = MaterialTheme.colorScheme.onErrorContainer, modifier = Modifier.padding(12.dp))
                }
            }
        }
        Surface(shadowElevation = 8.dp) {
            TabdalButton(
                text = "Modifier le mot de passe",
                onClick = {
                    if (newPassword == confirmPassword)
                        viewModel.changePassword(currentPassword, newPassword)
                },
                isLoading = uiState.isLoading,
                enabled = currentPassword.isNotBlank() && newPassword.length >= 8 && newPassword == confirmPassword,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
