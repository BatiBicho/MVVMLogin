package com.pompurin.mvvmlogin.ui.weather

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import androidx.core.graphics.toColorInt

// Colores de tu paleta
private val ColorBackground = Color("#FAF7F2".toColorInt())
private val ColorContourDark = Color("#5C4033".toColorInt())
private val ColorContourLight = Color("#8B6F50".toColorInt())
private val ColorGreenPrimary = Color("#D1E395".toColorInt())
private val ColorGreenSecondary = Color("#66803C".toColorInt())

@Composable
fun HikingWeatherScreen(
    viewModel: HikingWeatherViewModel,
    navigateToBack: () -> Unit
) {
    val context = LocalContext.current
    val weather by viewModel.weather.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()
    val hikingRecommendation by viewModel.hikingRecommendation.observeAsState()

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Launcher para pedir permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (hasLocationPermission) {
            getCurrentLocation(context) { lat, lon ->
                viewModel.getWeatherByLocation(lat, lon)
            }
        }
    }

    // Obtener ubicación automáticamente al cargar la pantalla
    LaunchedEffect(Unit) {
        if (hasLocationPermission) {
            getCurrentLocation(context) { lat, lon ->
                viewModel.getWeatherByLocation(lat, lon)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        // Título principal
        Text(
            text = "¿Senderismo Hoy?",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = ColorContourDark
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Basado en el clima de tu ubicación",
            fontSize = 14.sp,
            color = ColorContourLight
        )

        Spacer(Modifier.height(24.dp))

        // Botón para obtener ubicación
        if (!hasLocationPermission) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFE5E5)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "⚠️ Se necesita acceso a la ubicación",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorContourDark,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Para obtener el clima de tu ciudad",
                        fontSize = 14.sp,
                        color = ColorContourLight,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorGreenPrimary,
                            contentColor = ColorContourDark
                        )
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Permitir Ubicación")
                    }
                }
            }
        } else {
            Button(
                onClick = {
                    getCurrentLocation(context) { lat, lon ->
                        viewModel.getWeatherByLocation(lat, lon)
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorGreenPrimary,
                    contentColor = ColorContourDark
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = ColorContourDark
                    )
                } else {
                    Icon(Icons.Default.LocationOn, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Actualizar Ubicación", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Mostrar error
        errorMessage?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFE5E5)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = error,
                    color = Color(0xFF8B0000),
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp
                )
            }
        }

        // Mostrar información del clima
        weather?.let { w ->
            // Card de ubicación
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.7f)
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "📍 ${w.location.name}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorContourDark
                    )
                    Text(
                        text = "${w.location.region}, ${w.location.country}",
                        fontSize = 14.sp,
                        color = ColorContourLight
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Card de recomendación principal
            hikingRecommendation?.let { rec ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (rec.isRecommended)
                            ColorGreenPrimary.copy(alpha = 0.3f)
                        else
                            Color(0xFFFFE5E5)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Ícono de recomendación
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(
                                    color = if (rec.isRecommended) ColorGreenSecondary else Color(0xFF8B0000),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (rec.isRecommended) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.size(50.dp),
                                tint = Color.White
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        // Score
                        Text(
                            text = "Puntuación: ${rec.score}/100",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorContourDark
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = rec.reason,
                            fontSize = 18.sp,
                            color = ColorContourLight,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Tips y recomendaciones
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.7f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "📋 Recomendaciones",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorContourDark
                        )
                        Spacer(Modifier.height(12.dp))
                        rec.tips.forEach { tip ->
                            Text(
                                text = "• $tip",
                                fontSize = 14.sp,
                                color = ColorContourDark,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Card de detalles del clima
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.7f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "🌤️ Detalles del Clima",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorContourDark
                    )
                    Spacer(Modifier.height(16.dp))

                    // Temperatura y condición
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${w.current.tempC.toInt()}°C",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorGreenSecondary
                        )
                        Text(
                            text = w.current.condition.text,
                            fontSize = 18.sp,
                            color = ColorContourLight,
                            textAlign = TextAlign.End
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                    Divider(color = ColorContourLight.copy(alpha = 0.3f))
                    Spacer(Modifier.height(16.dp))

                    // Grid de detalles
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        WeatherDetailColumn("Sensación", "${w.current.feelslikeC.toInt()}°C")
                        WeatherDetailColumn("Humedad", "${w.current.humidity}%")
                        WeatherDetailColumn("Viento", "${w.current.windKph.toInt()} km/h")
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        WeatherDetailColumn("Presión", "${w.current.pressureMb.toInt()} mb")
                        WeatherDetailColumn("Visibilidad", "${w.current.visKm} km")
                        WeatherDetailColumn("UV", "${w.current.uv}")
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Botón Regresar
        Button(
            onClick = navigateToBack,
            modifier = Modifier.fillMaxWidth(0.6f),
            colors = ButtonDefaults.buttonColors(
                containerColor = ColorGreenPrimary,
                contentColor = ColorContourDark
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Regresar", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun WeatherDetailColumn(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = ColorContourLight
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = ColorContourDark
        )
    }
}

// Función para obtener la ubicación actual
private fun getCurrentLocation(
    context: android.content.Context,
    onLocationReceived: (Double, Double) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    try {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                onLocationReceived(location.latitude, location.longitude)
            }
        }
    } catch (e: SecurityException) {
        // Permiso no concedido
    }
}