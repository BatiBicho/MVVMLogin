package com.pompurin.mvvmlogin.data

import android.content.Context
import android.content.SharedPreferences

class ThemePreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "theme_preferences"
        private const val KEY_DARK_THEME = "dark_theme"
        private const val KEY_USE_SYSTEM_THEME = "use_system_theme"
    }

    // Guardar si está en modo oscuro
    fun setDarkTheme(isDark: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_DARK_THEME, isDark).apply()
    }

    // Obtener si está en modo oscuro
    fun isDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(KEY_DARK_THEME, false)
    }

    // Guardar si usa el tema del sistema
    fun setUseSystemTheme(useSystem: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_USE_SYSTEM_THEME, useSystem).apply()
    }

    // Obtener si usa el tema del sistema
    fun useSystemTheme(): Boolean {
        return sharedPreferences.getBoolean(KEY_USE_SYSTEM_THEME, true)
    }

    // Limpiar preferencias
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}