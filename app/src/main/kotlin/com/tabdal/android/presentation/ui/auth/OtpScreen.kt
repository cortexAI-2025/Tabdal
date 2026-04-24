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
import com.tabdal.android.presentation.ui.components.TabdalTopBar
import com.tabdal.android.presentation.viewmodels.AuthViewModel

@Composable
fun OtpScreen(
    phone: String,
    verificationId: String,
    onVerified: () -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var otpCode by remember { mutableStateOf("") }

    LaunchedEffect(uiState.success) {
        if (uiState.success) { viewModel.clearSuccess(); onVerified() }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabdalTopBar(title = "Vérification", onBack = onBack)
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("📱", fontSize = 48.sp)
            Spacer(Modifier.height(16.dp))
            Text("Vérification du numéro", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Un code à 6 chiffres a été envoyé au\n$phone",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(32.dp))
            OutlinedTextField(
                value = otpCode,
                onValueChange = { if (it.length <= 6 && it.all { c -> c.isDigit() }) otpCode = it },
                label = { Text("Code à 6 chiffres") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center, letterSpacing = 8.sp, fontSize = 24.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )
            uiState.error?.let { error ->
                Spacer(Modifier.height(8.dp))
                Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(24.dp))
            TabdalButton(text = "Vérifier",
                onClick = { viewModel.verifyOtp(verificationId, otpCode) },
                enabled = otpCode.length == 6, isLoading = uiState.isLoading)
            Spacer(Modifier.height(16.dp))
            TextButton(onClick = { viewModel.sendPhoneVerification(phone) }) {
                Text("Renvoyer le code", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
