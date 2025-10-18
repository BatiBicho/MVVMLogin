package com.pompurin.mvvmlogin.ui.weather

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pompurin.mvvmlogin.domain.Weather

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherListScreen(navController: NavController) {
    val viewModel: WeatherViewModel = viewModel()
    val cities by viewModel.cities.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clima App") },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Settings, contentDescription = "Configuración")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(cities) { city ->
                WeatherCard(
                    weather = city,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun WeatherCard(weather: Weather, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = weather.city,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${weather.temperature}°C",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = weather.description)
        }
    }
}