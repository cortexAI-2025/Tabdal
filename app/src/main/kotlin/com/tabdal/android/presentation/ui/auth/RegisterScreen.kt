package com.tabdal.android.presentation.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tabdal.android.presentation.ui.components.TabdalButton
import com.tabdal.android.presentation.ui.components.TabdalTextField
import com.tabdal.android.presentation.viewmodels.AuthViewModel

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToOtp: (phone: String, verificationId: String) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.verificationId) {
        uiState.verificationId?.let { vid -> onNavigateToOtp(phone, vid) }
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))
        Text("TABDAL", style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Text("Créer un compte", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(24.dp))
        TabdalTextField(value = fullName, onValueChange = { fullName = it }, label = "Nom complet")
        Spacer(Modifier.height(12.dp))
        TabdalTextField(value = email, onValueChange = { email = it }, label = "Email",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
        Spacer(Modifier.height(12.dp))
        TabdalTextField(value = phone, onValueChange = { phone = it }, label = "Téléphone (+212...)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
        Spacer(Modifier.height(12.dp))
        TabdalTextField(value = city, onValueChange = { city = it }, label = "Ville")
        Spacer(Modifier.height(12.dp))
        TabdalTextField(
            value = password, onValueChange = { password = it }, label = "Mot de passe",
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                }
            }
        )
        Spacer(Modifier.height(12.dp))
        TabdalTextField(value = confirmPassword, onValueChange = { confirmPassword = it },
            label = "Confirmer le mot de passe",
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
        Spacer(Modifier.height(16.dp))
        uiState.error?.let { error ->
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()) {
                Text(error, color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(8.dp))
        }
        TabdalButton(text = "S'inscrire",
            onClick = { viewModel.register(email, password, confirmPassword, fullName, phone, city) },
            isLoading = uiState.isLoading)
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text("Déjà un compte ? ", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Se connecter", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(onClick = onNavigateToLogin))
        }
        Spacer(Modifier.height(48.dp))
    }
}
