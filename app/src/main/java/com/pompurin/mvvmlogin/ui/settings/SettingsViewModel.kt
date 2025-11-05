package com.pompurin.mvvmlogin.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pompurin.mvvmlogin.data.ThemePreferences

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val themePreferences = ThemePreferences(application)

    private val _isDarkTheme = MutableLiveData<Boolean>()
    val isDarkTheme: LiveData<Boolean> = _isDarkTheme

    private val _useSystemTheme = MutableLiveData<Boolean>()
    val useSystemTheme: LiveData<Boolean> = _useSystemTheme

    init {
        // Cargar preferencias guardadas
        _isDarkTheme.value = themePreferences.isDarkTheme()
        _useSystemTheme.value = themePreferences.useSystemTheme()
    }

    fun toggleDarkTheme() {
        val newValue = !(_isDarkTheme.value ?: false)
        _isDarkTheme.value = newValue
        themePreferences.setDarkTheme(newValue)
    }

    fun setDarkTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
        themePreferences.setDarkTheme(isDark)
    }

    fun toggleSystemTheme() {
        val newValue = !(_useSystemTheme.value ?: true)
        _useSystemTheme.value = newValue
        themePreferences.setUseSystemTheme(newValue)
    }

    fun setUseSystemTheme(useSystem: Boolean) {
        _useSystemTheme.value = useSystem
        themePreferences.setUseSystemTheme(useSystem)
    }

    // Obtener el tema que se debe usar
    fun shouldUseDarkTheme(isSystemInDarkTheme: Boolean): Boolean {
        return if (_useSystemTheme.value == true) {
            isSystemInDarkTheme
        } else {
            _isDarkTheme.value ?: false
        }
    }
}