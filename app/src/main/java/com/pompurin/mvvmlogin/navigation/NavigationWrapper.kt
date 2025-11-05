package com.pompurin.mvvmlogin.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pompurin.mvvmlogin.ui.Home.BlankScreen
import com.pompurin.mvvmlogin.ui.Home.Home
import com.pompurin.mvvmlogin.ui.login.LoginScreen
import com.pompurin.mvvmlogin.ui.login.LoginViewModel
import com.pompurin.mvvmlogin.ui.login.RegisterScreen
import com.pompurin.mvvmlogin.ui.login.RegisterViewModel
import com.pompurin.mvvmlogin.ui.maps.MapScreen
import com.pompurin.mvvmlogin.ui.weather.HikingWeatherScreen
import com.pompurin.mvvmlogin.ui.weather.HikingWeatherViewModel
import com.pompurin.mvvmlogin.ui.weather.WeatherScreen
import com.pompurin.mvvmlogin.ui.weather.WeatherViewModel

private const val TAG = "NavigationWrapper"

@Composable
fun NavigationWrapper() {
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

        composable("home") {
            Log.d(TAG, "Navigating to Home")
            Home(
                navigateToBack = { navController.popBackStack() },
                navigateToDetail = { id -> navController.navigate("detail/$id") },
                navigateToBlank = { navController.navigate("blank") },
                navigateToWeather = { navController.navigate("hikingWeather") }
            )
        }

        composable("detail/{id}") { backStackEntry ->
            val detailId = backStackEntry.arguments?.getString("id") ?: ""

            when (detailId) {
                "map" -> MapScreen(navigateToBack = { navController.popBackStack() })
                else -> BlankScreen(navigateToBack = { navController.popBackStack() })
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

        composable("blank") {
            Log.d(TAG, "Navigating to BlankScreen")
            BlankScreen(
                navigateToBack = { navController.popBackStack() }
            )
        }
    }
    Log.d(TAG, "NavHost setup complete")
}