package com.pompurin.mvvmlogin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.pompurin.mvvmlogin.navigation.NavigationWrapper
import com.pompurin.mvvmlogin.ui.login.LoginScreen
import com.pompurin.mvvmlogin.ui.login.LoginViewModel
import com.pompurin.mvvmlogin.ui.theme.MVVMLoginTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MVVMLoginTheme {
                Surface (modifier = Modifier.fillMaxSize(), color = colorResource(R.color.background)) {
                    NavigationWrapper()
                }
            }
        }
    }
}