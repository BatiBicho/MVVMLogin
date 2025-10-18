package com.pompurin.mvvmlogin.ui.Home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Home(navigateToBack: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "Home Screen")
        Button(onClick = { navigateToBack() }) {
            Text("Regresar")
        }
    }
}