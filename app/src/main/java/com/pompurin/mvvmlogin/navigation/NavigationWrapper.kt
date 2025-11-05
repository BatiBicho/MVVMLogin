package com.pompurin.mvvmlogin.navigation

import com.pompurin.mvvmlogin.ui.maps.ActivityMetrics
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pompurin.mvvmlogin.ui.Home.BlankScreen
import com.pompurin.mvvmlogin.ui.Home.Home
import com.pompurin.mvvmlogin.ui.Home.PantallaEstadisticas // <-- ¡IMPORTE NUEVO!
import com.pompurin.mvvmlogin.ui.login.LoginScreen
import com.pompurin.mvvmlogin.ui.login.LoginViewModel
import com.pompurin.mvvmlogin.ui.login.RegisterScreen
import com.pompurin.mvvmlogin.ui.login.RegisterViewModel
import com.pompurin.mvvmlogin.ui.maps.MapScreen
import com.pompurin.mvvmlogin.ui.settings.SettingsViewModel
import com.pompurin.mvvmlogin.ui.weather.HikingWeatherScreen
import com.pompurin.mvvmlogin.ui.weather.HikingWeatherViewModel
import com.pompurin.mvvmlogin.ui.weather.WeatherScreen
import com.pompurin.mvvmlogin.ui.weather.WeatherViewModel
import com.pompurin.mvvmlogin.ui.settings.SettingsScreen

private const val TAG = "NavigationWrapper"

@Composable
fun NavigationWrapper(settingsViewModel: SettingsViewModel) {
    Log.d(TAG, "Creating NavController")
    val navController = rememberNavController()

    // ViewModels
    Log.d(TAG, "Creating ViewModels")
    val loginViewModel: LoginViewModel = viewModel()
    val registerViewModel: RegisterViewModel = viewModel()
    val weatherViewModel: WeatherViewModel = viewModel()
    val hikingWeatherViewModel: HikingWeatherViewModel = viewModel()

    Log.d(TAG, "Setting up NavHost")
    NavHost(navController = navController, startDestination = "login") {

        // ... (login y register sin cambios) ...
        composable("login") {
            Log.d(TAG, "Navigating to LoginScreen")
            LoginScreen(
                modifier = Modifier,
                viewModel = loginViewModel,
                navigateToHome = { navController.navigate("home") },
                navigateToRegister = { navController.navigate("register") }
            )
        }

        composable("register") {
            Log.d(TAG, "Navigating to RegisterScreen")
            RegisterScreen(
                modifier = Modifier,
                viewModel = registerViewModel,
                navigateToHome = { navController.navigate("home") },
                navigateToLogin = { navController.navigate("login") }
            )
        }


        // --- CAMBIO 1: Pasamos la nueva función a 'Home' ---
        composable("home") {
            Log.d(TAG, "Navigating to Home")
            Home(
                navigateToBack = { navController.popBackStack() },
                navigateToDetail = { id -> navController.navigate("detail/$id") },
                navigateToBlank = { navController.navigate("blank") },
                navigateToWeather = { navController.navigate("hikingWeather") },
                navigateToSettings = { navController.navigate("settings") },
                navigateToStats = { navController.navigate("stats") } // <-- ¡AÑADIDO!
            )
        }

        // ... (detail/{id}, weather, hikingWeather sin cambios) ...
        composable("detail/{id}") { backStackEntry ->
            val detailId = backStackEntry.arguments?.getString("id") ?: ""

            when (detailId) {
                "map" -> MapScreen(
                    metrics = ActivityMetrics(),
                    navigateToBack = { navController.popBackStack() }
                )
            }
        }

        composable("weather") {
            Log.d(TAG, "Navigating to WeatherScreen")
            WeatherScreen(
                viewModel = weatherViewModel,
                navigateToBack = { navController.popBackStack() }
            )
        }

        composable("hikingWeather") {
            Log.d(TAG, "Navigating to HikingWeatherScreen")
            HikingWeatherScreen (
                viewModel = hikingWeatherViewModel,
                navigateToBack = { navController.popBackStack() }
            )
        }

        // ... (settings sin cambios) ...
        composable("settings") {
            Log.d(TAG, "Navigating to SettingsScreen")
            SettingsScreen (
                viewModel = settingsViewModel,
                navigateToBack = { navController.popBackStack() }
            )
        }

        // ... (blank sin cambios) ...
        composable("blank") {
            Log.d(TAG, "Navigating to BlankScreen")
            BlankScreen(
                navigateToBack = { navController.popBackStack() }
            )
        }

        // --- CAMBIO 2: Añadimos la nueva ruta para estadísticas ---
        composable("stats") {
            Log.d(TAG, "Navigating to PantallaEstadisticas")
            PantallaEstadisticas(
                navController = navController
            )
        }

    }
    Log.d(TAG, "NavHost setup complete")
}