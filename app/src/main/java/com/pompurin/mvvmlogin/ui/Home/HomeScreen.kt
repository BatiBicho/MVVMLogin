package com.pompurin.mvvmlogin.ui.Home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun Home(navigateToBack: () -> Unit, navigateToDetail: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
        Spacer(Modifier.weight(1f))
        Text(text = "Home Screen", fontSize = 30.sp)
        Spacer(Modifier.weight(1f))
        Row {
            TextField(
                value = text, onValueChange = { text = it }, modifier = Modifier.weight(1f)
            )
            Button(onClick = { navigateToDetail(text)}) {
                Text("Detail")
            }
        }
        Button(onClick = { navigateToBack() }) {
            Text("Regresar")
        }
        Spacer(Modifier.weight(1f))
    }
}