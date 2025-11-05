package com.pompurin.mvvmlogin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue // <-- ¡IMPORTANTE!
import androidx.compose.runtime.livedata.observeAsState // <-- ¡IMPORTANTE!
import com.pompurin.mvvmlogin.navigation.NavigationWrapper
import com.pompurin.mvvmlogin.ui.settings.SettingsViewModel
import com.pompurin.mvvmlogin.ui.theme.MVVMLoginTheme

class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 1. "Escuchamos" los cambios del ViewModel
            val isDarkTheme by settingsViewModel.isDarkTheme.observeAsState(false)
            val useSystemTheme by settingsViewModel.useSystemTheme.observeAsState(true)

            // 2. Obtenemos el estado del sistema
            val isSystemInDarkTheme = isSystemInDarkTheme()

            // 3. ¡LA LÓGICA DEBE ESTAR AQUÍ!
            // Esta variable se recalcula CADA VEZ que isDarkTheme o useSystemTheme cambian.
            val shouldUseDarkTheme = if (useSystemTheme) {
                isSystemInDarkTheme
            } else {
                isDarkTheme
            }

            // 4. Le pasamos el resultado a nuestro tema
            MVVMLoginTheme(darkTheme = shouldUseDarkTheme) {
                NavigationWrapper(settingsViewModel = settingsViewModel)
            }
        }
    }
}