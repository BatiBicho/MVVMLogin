package com.pompurin.mvvmlogin.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pompurin.mvvmlogin.data.RetrofitClient
import com.pompurin.mvvmlogin.data.WeatherRepository
import com.pompurin.mvvmlogin.ui.Home.BlankScreen
import com.pompurin.mvvmlogin.ui.Home.Home
import com.pompurin.mvvmlogin.ui.login.LoginScreen
import com.pompurin.mvvmlogin.ui.login.LoginViewModel
import com.pompurin.mvvmlogin.ui.weather.WeatherListScreen
import com.pompurin.mvvmlogin.ui.weather.WeatherViewModel
import com.pompurin.mvvmlogin.ui.weather.WeatherViewModelFactory

private const val TAG = "NavigationWrapper"

@Composable
fun NavigationWrapper() {
    Log.d(TAG, "Creating NavController")
    val navController = rememberNavController()

    // ViewModels
    Log.d(TAG, "Creating LoginViewModel")
    val loginViewModel: LoginViewModel = viewModel()

    Log.d(TAG, "Creating WeatherRepository")
    val weatherRepository = WeatherRepository(RetrofitClient.instance)

    Log.d(TAG, "Creating WeatherViewModel")
    val weatherViewModel: WeatherViewModel = viewModel(factory = WeatherViewModelFactory(weatherRepository))

    Log.d(TAG, "Setting up NavHost")
    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            Log.d(TAG, "Navigating to LoginScreen")
            LoginScreen(
                modifier = Modifier,
                viewModel = loginViewModel,
                navigateToHome = { navController.navigate("home") }
            )
        }

        composable("home") {
            Log.d(TAG, "Navigating to Home")
            Home(
                navigateToBack = { navController.popBackStack() },
                navigateToDetail = { id -> navController.navigate("detail/$id") },
                navigateToBlank = { navController.navigate("blank") }
            )
        }

        composable("detail/{id}") { backStackEntry ->
            val detailId = backStackEntry.arguments?.getString("id") ?: ""
            Log.d(TAG, "Navigating to WeatherListScreen with id: $detailId")
            WeatherListScreen(
                viewModel = weatherViewModel,
                navigateToHome = { navController.popBackStack() },
                text = detailId
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
