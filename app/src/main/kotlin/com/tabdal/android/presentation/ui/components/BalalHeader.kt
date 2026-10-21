package com.tabdal.android.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tabdal.android.R

/** Logo BALAL centré en haut des écrans principaux. */
@Composable
fun BalalLogoBar(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth().padding(top = 12.dp, bottom = 4.dp), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(R.drawable.logo_balal),
            contentDescription = "BALAL",
            contentScale = ContentScale.Fit,
            modifier = Modifier.height(40.dp)
        )
    }
}

/** Titre d'écran principal (style "Favoris", "Mes messages"). */
@Composable
fun ScreenTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp)
    )
}

/** Illustration d'état vide : pastille crème + icône, titre, sous-titre. */
@Composable
fun EmptyState(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String? = null,
    action: @Composable (() -> Unit)? = null
) {
    Column(
        Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(120.dp)) {
            Box(contentAlignment = Alignment.Center) {
                androidx.compose.material3.Icon(icon, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(56.dp))
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        if (subtitle != null) {
            Spacer(Modifier.height(8.dp))
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
        if (action != null) { Spacer(Modifier.height(24.dp)); action() }
    }
}
