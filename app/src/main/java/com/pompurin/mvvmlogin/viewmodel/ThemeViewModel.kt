package com.pompurin.mvvmlogin.viewmodel // <- ¡Asegúrate que el paquete sea correcto!

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThemeViewModel : ViewModel() {

    // Guarda el estado del tema (oscuro/claro)
    private val _isDarkTheme = MutableStateFlow(false) // Inicia en claro
    val isDarkTheme = _isDarkTheme.asStateFlow()

    // Función para cambiar el tema
    fun setTheme(isDark: Boolean) {
        viewModelScope.launch {
            _isDarkTheme.emit(isDark)
        }
    }
}