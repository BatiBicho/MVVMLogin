package com.pompurin.mvvmlogin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.pompurin.mvvmlogin.navigation.NavigationWrapper
import com.pompurin.mvvmlogin.ui.settings.SettingsViewModel
import com.pompurin.mvvmlogin.ui.theme.MVVMLoginTheme

class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Observar el tema
            val isDarkTheme by settingsViewModel.isDarkTheme.observeAsState(false)
            val useSystemTheme by settingsViewModel.useSystemTheme.observeAsState(true)
            val isSystemInDarkTheme = isSystemInDarkTheme()

            // Determinar qué tema usar
            val shouldUseDarkTheme = if (useSystemTheme) {
                isSystemInDarkTheme
            } else {
                isDarkTheme
            }

            MVVMLoginTheme(darkTheme = shouldUseDarkTheme) {
                NavigationWrapper(settingsViewModel = settingsViewModel)
            }
        }
    }
}