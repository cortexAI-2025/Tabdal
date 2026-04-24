package com.tabdal.android.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary            = Primary,
    onPrimary          = Color.White,
    primaryContainer   = PrimaryDark,
    secondary          = Secondary,
    onSecondary        = Color.White,
    tertiary           = Tertiary,
    background         = BackgroundDark,
    onBackground       = OnSurfaceDark,
    surface            = SurfaceDark,
    onSurface          = OnSurfaceDark,
    surfaceVariant     = SurfaceVariantDark,
    onSurfaceVariant   = OnSurfaceVariantDark,
    error              = ErrorRed,
    outline            = DividerDark
)

private val LightColorScheme = lightColorScheme(
    primary            = Primary,
    onPrimary          = Color.White,
    primaryContainer   = PrimaryLight,
    secondary          = Secondary,
    onSecondary        = Color.White,
    tertiary           = Tertiary,
    background         = BackgroundLight,
    onBackground       = OnSurfaceLight,
    surface            = SurfaceLight,
    onSurface          = OnSurfaceLight,
    surfaceVariant     = Color(0xFFEEEEEE),
    onSurfaceVariant   = OnSurfaceVariantLight,
    error              = ErrorRed,
    outline            = Color(0xFFD1D1D6)
)

@Composable
fun TabdalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography  = TabdalTypography,
        content     = content
    )
}
