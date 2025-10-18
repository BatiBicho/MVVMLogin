package com.pompurin.mvvmlogin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.pompurin.mvvmlogin.domain.WeatherInfo
import com.pompurin.mvvmlogin.ui.Home.Home
import com.pompurin.mvvmlogin.ui.login.LoginScreen
import com.pompurin.mvvmlogin.ui.login.LoginViewModel
import com.pompurin.mvvmlogin.ui.weather.WeatherListScreen

/*
Navigate to screen and delete the stack
navController.navigate(Login){
    popUpTo<Login>{inclusive = true}
}
* */

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            LoginScreen(
                modifier = Modifier,
                viewModel = LoginViewModel(),
                navigateToHome = { navController.navigate(Home) })
        }

        composable<Home> {
            Home(
                navigateToBack = { navController.popBackStack() },
                navigateToDetail = { navController.navigate(Detail(it))}
            )
        }

        composable<Detail> { navBackStackEntry ->
            val detail: Detail = navBackStackEntry.toRoute()
            WeatherListScreen(
                navigateToHome = {navController.popBackStack()},
                text = detail.id
            )
        }
    }
}