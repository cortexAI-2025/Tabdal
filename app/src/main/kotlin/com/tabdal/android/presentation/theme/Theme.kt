package com.tabdal.android.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary            = Primary,
    onPrimary          = Color.White,
    primaryContainer   = SurfaceVariantLight,
    onPrimaryContainer = PrimaryDark,
    secondary          = Secondary,
    onSecondary        = Color.White,
    secondaryContainer = Color(0xFFFFF0D0),
    onSecondaryContainer = Color(0xFF4A3600),
    tertiary           = Tertiary,
    onTertiary         = Color.White,
    background         = BackgroundLight,
    onBackground       = OnSurfaceLight,
    surface            = SurfaceLight,
    onSurface          = OnSurfaceLight,
    surfaceVariant     = SurfaceVariantLight,
    onSurfaceVariant   = OnSurfaceVariantLight,
    error              = ErrorRed,
    onError            = Color.White,
    outline            = OutlineLight,
    outlineVariant     = Color(0xFFE8E8E8)
)

private val DarkColorScheme = darkColorScheme(
    primary            = PrimaryLight,
    onPrimary          = Color.White,
    primaryContainer   = PrimaryDark,
    onPrimaryContainer = Color(0xFFD8E8A0),
    secondary          = SecondaryLight,
    onSecondary        = Color.White,
    secondaryContainer = Color(0xFF4A3600),
    onSecondaryContainer = Color(0xFFFFE08A),
    tertiary           = Tertiary,
    onTertiary         = Color(0xFF3A2800),
    background         = BackgroundDark,
    onBackground       = OnSurfaceDark,
    surface            = SurfaceDark,
    onSurface          = OnSurfaceDark,
    surfaceVariant     = SurfaceVariantDark,
    onSurfaceVariant   = OnSurfaceVariantDark,
    error              = ErrorRed,
    onError            = Color.White,
    outline            = OutlineDark,
    outlineVariant     = Color(0xFF3A3E20)
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
