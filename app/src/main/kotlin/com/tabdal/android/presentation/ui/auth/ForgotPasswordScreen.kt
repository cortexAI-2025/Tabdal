package com.tabdal.android.presentation.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tabdal.android.presentation.ui.components.TabdalButton
import com.tabdal.android.presentation.ui.components.TabdalTextField
import com.tabdal.android.presentation.ui.components.TabdalTopBar
import com.tabdal.android.presentation.viewmodels.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var email by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        TabdalTopBar(title = "Mot de passe oublié", onBack = onBack)
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (uiState.success) {
                Text("✅", fontSize = 48.sp)
                Spacer(Modifier.height(16.dp))
                Text("Email envoyé !", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("Vérifiez votre boîte mail pour réinitialiser votre mot de passe.",
                    textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(32.dp))
                Button(onClick = onBack) { Text("Retour à la connexion") }
            } else {
                Text("🔐", fontSize = 48.sp)
                Spacer(Modifier.height(16.dp))
                Text("Réinitialiser le mot de passe", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("Entrez votre email pour recevoir un lien de réinitialisation.",
                    textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(32.dp))
                TabdalTextField(value = email, onValueChange = { email = it }, label = "Email",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
                uiState.error?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
                Spacer(Modifier.height(24.dp))
                TabdalButton(text = "Envoyer le lien",
                    onClick = { viewModel.sendPasswordReset(email) },
                    isLoading = uiState.isLoading, enabled = email.isNotBlank())
            }
        }
    }
}
