package com.tabdal.android.presentation.ui.auth

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tabdal.android.R
import com.tabdal.android.presentation.ui.components.TabdalButton
import com.tabdal.android.presentation.ui.components.TabdalTextField
import com.tabdal.android.presentation.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.success) {
        if (uiState.success) { viewModel.clearSuccess(); onLoginSuccess() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.height(56.dp))

        // Logo Tabdal
        Image(
            painter = painterResource(R.drawable.logo_tabdal),
            contentDescription = "Logo Tabdal",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth(0.78f)
                .heightIn(max = 160.dp)
        )

        Spacer(Modifier.height(48.dp))

        Text(
            "Connexion",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        TabdalTextField(
            value = email, onValueChange = { email = it }, label = "Email",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(Modifier.height(16.dp))
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
        Spacer(Modifier.height(8.dp))
        Text(
            "Mot de passe oublié ?",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.End).clickable(onClick = onNavigateToForgotPassword)
        )
        Spacer(Modifier.height(24.dp))

        uiState.error?.let { error ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(error, color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(8.dp))
        }

        TabdalButton(
            text = "Se connecter",
            onClick = { viewModel.login(email, password) },
            isLoading = uiState.isLoading
        )
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text("Pas encore de compte ? ", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                "S'inscrire",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(onClick = onNavigateToRegister)
            )
        }
        Spacer(Modifier.height(48.dp))
    }
}
