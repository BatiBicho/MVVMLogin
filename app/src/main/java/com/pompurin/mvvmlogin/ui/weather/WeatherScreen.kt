package com.pompurin.mvvmlogin.ui.weather

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pompurin.mvvmlogin.R
import com.pompurin.mvvmlogin.data.model.Weather
import com.pompurin.mvvmlogin.data.repository.Resource
import com.pompurin.mvvmlogin.data.repository.WeatherRepository
import kotlinx.coroutines.launch


// ========== COMPOSABLE DE PANTALLA DE CLIMA ==========
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    navigateToBack: () -> Unit
) {
    val weather by viewModel.weather.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()

    var cityName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clima") },
                navigationIcon = {
                    IconButton(onClick = navigateToBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.primary),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = "Consulta el Clima",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.primary),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Campo de búsqueda
            OutlinedTextField(
                value = cityName,
                onValueChange = { cityName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = {
                    Text(
                        text = "Nombre de la ciudad",
                        color = colorResource(R.color.primary).copy(alpha = 0.7f)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colorResource(R.color.primary),
                    unfocusedTextColor = colorResource(R.color.primary),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = colorResource(R.color.primary),
                    focusedBorderColor = colorResource(R.color.primary),
                    unfocusedBorderColor = colorResource(R.color.primary).copy(alpha = 0.7f),
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de búsqueda
            Button(
                onClick = {
                    if (cityName.isNotEmpty()) {
                        viewModel.getWeatherByCity(cityName)
                    }
                },
                enabled = cityName.isNotEmpty() && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.primary),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(
                        text = "Buscar Clima",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Mostrar error
            errorMessage?.let { error ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Red.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = error,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 14.sp
                    )
                }
            }

            // Mostrar datos del clima
            weather?.let { w ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(R.color.primary).copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Ciudad y país
                        Text(
                            text = "${w.cityName}, ${w.sys.country}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.primary)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Temperatura principal
                        Text(
                            text = "${w.main.temp.toInt()}°C",
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.primary)
                        )

                        // Descripción
                        Text(
                            text = w.weather.firstOrNull()?.description?.capitalize() ?: "",
                            fontSize = 20.sp,
                            color = colorResource(R.color.primary).copy(alpha = 0.8f)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Detalles adicionales
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            WeatherDetailItem(
                                label = "Mín",
                                value = "${w.main.tempMin.toInt()}°C"
                            )
                            WeatherDetailItem(
                                label = "Máx",
                                value = "${w.main.tempMax.toInt()}°C"
                            )
                            WeatherDetailItem(
                                label = "Humedad",
                                value = "${w.main.humidity}%"
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Sensación térmica y viento
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            WeatherDetailItem(
                                label = "Sensación",
                                value = "${w.main.feelsLike.toInt()}°C"
                            )
                            WeatherDetailItem(
                                label = "Viento",
                                value = "${w.wind.speed} m/s"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherDetailItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = colorResource(R.color.primary).copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.primary)
        )
    }
}