package com.pompurin.mvvmlogin

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pompurin.mvvmlogin.ui.weather.SettingsScreen
import com.pompurin.mvvmlogin.ui.weather.WeatherDetailScreen
import com.pompurin.mvvmlogin.ui.weather.WeatherListScreen

sealed class Screen(val route: String) {
    object WeatherList : Screen("Weather_list")
    object WeatherDetail : Screen("weather_detail")
    object Settings : Screen("settings")
}

@Composable
fun WeatherApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.WeatherList.route) {
        composable(Screen.WeatherList.route) {
            WeatherListScreen(navController = navController)
        }
        composable(Screen.WeatherDetail.route + "/{cityId}") { backStackEntry ->
            val cityId = backStackEntry.arguments?.getString("cityId") ?: ""
            WeatherDetailScreen(cityId = cityId, navController = navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
}