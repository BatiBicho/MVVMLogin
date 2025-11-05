package com.pompurin.mvvmlogin.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ========== ESQUEMAS DE COLOR ==========
private val LightColorScheme = lightColorScheme(
    primary = LightColors.Primary,              // Color principal (botones)
    onPrimary = LightColors.OnPrimary,          // Texto sobre primary
    secondary = LightColors.Secondary,          // Color secundario
    background = LightColors.Background,        // Fondo de la app
    surface = LightColors.Surface,              // Fondo de cards, dialogs
    onSurface = LightColors.OnSurface,          // Texto sobre surface
    error = LightColors.Error,
    outline = LightColors.Secondary,            // Bordes (inputs)
    surfaceVariant = LightColors.InputBackground // Variante de surface (inputs)
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkColors.Primary,               // Botones
    onPrimary = DarkColors.OnPrimary,           // Texto sobre botones
    secondary = DarkColors.Secondary,           // Color secundario
    background = DarkColors.Background,         // Fondo de la app
    surface = DarkColors.Surface,               // Fondo de cards, dialogs
    onSurface = DarkColors.OnSurface,           // Texto sobre surface
    error = DarkColors.Error,
    outline = DarkColors.Secondary,             // Bordes (inputs)
    surfaceVariant = DarkColors.InputBackground // Variante de surface (inputs),
)

// ========== TEMA PRINCIPAL DE LA APP ==========
@Composable
fun MVVMLoginTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}