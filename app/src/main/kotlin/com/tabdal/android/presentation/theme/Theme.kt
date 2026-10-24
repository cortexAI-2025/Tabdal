package com.tabdal.android.presentation.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary            = Primary,
    onPrimary          = Color.White,
    primaryContainer   = Color(0xFFDCEBF2),
    onPrimaryContainer = PrimaryDark,
    secondary          = Secondary,
    onSecondary        = Color.White,
    secondaryContainer = Color(0xFFFFE6D1),
    onSecondaryContainer = Color(0xFF5A2A05),
    tertiary           = Tertiary,
    onTertiary         = Color(0xFF0C2B38),
    background         = BackgroundLight,
    onBackground       = OnSurfaceLight,
    surface            = SurfaceLight,
    onSurface          = OnSurfaceLight,
    surfaceVariant     = SurfaceVariantLight,
    onSurfaceVariant   = OnSurfaceVariantLight,
    error              = ErrorRed,
    onError            = Color.White,
    outline            = OutlineLight,
    outlineVariant     = Color(0xFFE3E8EB)
)

/** Thème clair imposé : fond blanc et charte BALAL, quel que soit le réglage du téléphone. */
@Composable
fun TabdalTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography  = TabdalTypography,
        content     = content
    )
}
