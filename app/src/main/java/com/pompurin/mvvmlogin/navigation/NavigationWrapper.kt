package com.pompurin.mvvmlogin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pompurin.mvvmlogin.ui.Home.Home
import com.pompurin.mvvmlogin.ui.login.LoginScreen
import com.pompurin.mvvmlogin.ui.login.LoginViewModel

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            LoginScreen(modifier = Modifier, viewModel = LoginViewModel(), navigateToHome = { navController.navigate(Home) })
        }

        composable<Home> {
            Home(navigateToBack = { navController.popBackStack() })
        }
    }
}