package com.tabdal.android.presentation.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tabdal.android.presentation.ui.components.AvatarInitials
import com.tabdal.android.presentation.ui.components.TabdalTopBar
import com.tabdal.android.presentation.viewmodels.AuthViewModel
import com.tabdal.android.presentation.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(
    onNavigateToMyListings: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToChangePassword: () -> Unit,
    onLogout: () -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val currentUser by profileViewModel.currentUser.collectAsStateWithLifecycle(initialValue = null)
    var showDeleteDialog by remember { mutableStateOf(false) }
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(authState.success) {
        if (authState.success) { authViewModel.clearSuccess(); onLogout() }
    }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        TabdalTopBar(title = "Compte")

        // User card
        currentUser?.let { user ->
            Card(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AvatarInitials(user.fullName, size = 64)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(user.fullName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(user.email, style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (user.phone.isNotBlank()) {
                            Text(user.phone, style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        if (user.city.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.secondary)
                                Text(user.city, style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary)
                            }
                        }
                    }
                    IconButton(onClick = onNavigateToEditProfile) {
                        Icon(Icons.Default.Settings, "Modifier le profil")
                    }
                }
            }
        }

        // Menu items
        Card(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
            Column {
                ProfileMenuItem(icon = Icons.Default.Store, label = "Mes annonces", onClick = onNavigateToMyListings)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                ProfileMenuItem(icon = Icons.Default.Lock, label = "Modifier le mot de passe", onClick = onNavigateToChangePassword)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                ProfileMenuItem(icon = Icons.Default.Download, label = "Exporter mes données",
                    onClick = { profileViewModel.exportData() })
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                ProfileMenuItem(icon = Icons.Default.Support, label = "Contactez-nous", onClick = {})
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                ProfileMenuItem(icon = Icons.Default.Logout, label = "Se déconnecter",
                    onClick = { authViewModel.logout(); onLogout() },
                    tint = MaterialTheme.colorScheme.error)
            }
        }

        Spacer(Modifier.height(8.dp))

        // Danger zone
        Card(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
            ProfileMenuItem(
                icon = Icons.Default.DeleteForever,
                label = "Supprimer mon compte",
                onClick = { showDeleteDialog = true },
                tint = MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(32.dp))
    }

    // Delete account dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Supprimer le compte") },
            text = { Text("Cette action supprimera votre compte immédiatement. Vos données seront anonymisées sous 30 jours. Cette action est irréversible.") },
            confirmButton = {
                Button(
                    onClick = { showDeleteDialog = false; authViewModel.deleteAccount() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Supprimer définitivement") }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Annuler") } }
        )
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    tint: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Surface(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(shape = RoundedCornerShape(10.dp), color = tint.copy(alpha = 0.1f)) {
                Icon(icon, null, modifier = Modifier.padding(8.dp).size(20.dp), tint = tint)
            }
            Text(label, modifier = Modifier.weight(1f), color = tint)
            Icon(Icons.Default.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
