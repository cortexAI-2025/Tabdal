package com.tabdal.android.presentation.ui.admin

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AdminScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Simple Top Bar
        Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 2.dp) {
            Row(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, "Retour")
                }
                Text("Administration", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Tableau de bord", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

            // Stats row
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Utilisateurs", "—", Icons.Default.People, modifier = Modifier.weight(1f))
                StatCard("Annonces actives", "—", Icons.Default.Home, modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Signalements", "—", Icons.Default.Flag, modifier = Modifier.weight(1f))
                StatCard("Messages", "—", Icons.Default.Message, modifier = Modifier.weight(1f))
            }

            HorizontalDivider()

            Text("Actions de modération", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

            AdminActionCard(
                icon = Icons.Default.People,
                title = "Gestion des utilisateurs",
                description = "Bloquer ou supprimer des comptes"
            )
            AdminActionCard(
                icon = Icons.Default.Home,
                title = "Modération des annonces",
                description = "Désactiver des annonces signalées"
            )
            AdminActionCard(
                icon = Icons.Default.Flag,
                title = "Gestion des signalements",
                description = "Traiter les signalements en attente"
            )

            Text(
                "ℹ️ L'interface d'administration complète est accessible via le back-office web.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun AdminActionCard(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, description: String) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)) {
                Icon(icon, null, modifier = Modifier.padding(10.dp).size(22.dp), tint = MaterialTheme.colorScheme.primary)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Default.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
