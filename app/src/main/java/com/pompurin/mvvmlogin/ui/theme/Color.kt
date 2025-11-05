package com.pompurin.mvvmlogin.ui.theme

import androidx.compose.ui.graphics.Color

// ========== TEMA CLARO ==========
object LightColors {
    val Background = Color(0xFFFFFFFF)          // Blanco
    val Primary = Color(0xFF5C4033)             // Botones
    val Secondary = Color(0xFF66803C)           // Contorno de inputs
    val InputBackground = Color(0xFFFAF7F2)     // Fondo de inputs
    val InputText = Color(0xFF8B6F50)           // Texto dentro de inputs
    val Title = Color(0xFF000000)               // Negro
    val OnPrimary = Color(0xFFFFFFFF)           // Texto sobre botones
    val Surface = Color(0xFFF5F5F5)             // Superficies (cards)
    val OnSurface = Color(0xFF000000)           // Texto sobre superficies
    val Error = Color(0xFFB00020)               // Errores
}

// ========== TEMA OSCURO ==========
object DarkColors {
    val Background = Color(0xFF212121)          // Fondo oscuro
    val Primary = Color(0xFFD1E395)             // Botones
    val Secondary = Color(0xFFD1E395)           // Contorno de inputs
    val InputBackground = Color(0xFF2C2C2C)     // Fondo de inputs (un poco más claro que fondo)
    val InputText = Color(0xFFD1E395)           // Texto dentro de inputs
    val Title = Color(0xFFF6E5C3)               // Título
    val OnPrimary = Color(0xFF000000)           // Texto sobre botones (oscuro sobre verde claro)
    val Surface = Color(0xFF2C2C2C)             // Superficies (cards)
    val OnSurface = Color(0xFFFFFFFF)           // Texto sobre superficies
    val Error = Color(0xFFCF6679)               // Errores
}

// ========== COLORES ADICIONALES PERSONALIZADOS ==========
// Estos no cambian con el tema
object AppColors {
    val GreenPrimary = Color(0xFFD1E395)
    val GreenSecondary = Color(0xFF66803C)
    val ContourDark = Color(0xFF5C4033)
    val ContourLight = Color(0xFF8B6F50)
    val Beige = Color(0xFFF6E5C3)
}