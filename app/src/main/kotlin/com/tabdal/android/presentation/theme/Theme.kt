package com.tabdal.android.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
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

private val DarkColorScheme = darkColorScheme(
    primary            = PrimaryLight,
    onPrimary          = Color.White,
    primaryContainer   = PrimaryDark,
    onPrimaryContainer = Color(0xFFCDE6F1),
    secondary          = SecondaryLight,
    onSecondary        = Color.White,
    secondaryContainer = Color(0xFF5A2A05),
    onSecondaryContainer = Color(0xFFFFDCC2),
    tertiary           = Tertiary,
    onTertiary         = Color(0xFF0C2B38),
    background         = BackgroundDark,
    onBackground       = OnSurfaceDark,
    surface            = SurfaceDark,
    onSurface          = OnSurfaceDark,
    surfaceVariant     = SurfaceVariantDark,
    onSurfaceVariant   = OnSurfaceVariantDark,
    error              = ErrorRed,
    onError            = Color.White,
    outline            = OutlineDark,
    outlineVariant     = Color(0xFF2A4553)
)

@Composable
fun TabdalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography  = TabdalTypography,
        content     = content
    )
}
