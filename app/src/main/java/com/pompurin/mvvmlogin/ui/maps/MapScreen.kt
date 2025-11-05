package com.pompurin.mvvmlogin.ui.maps

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.OnMapReadyCallback

// --- CAMBIO 1: Definir una clase para tus datos ---
// Esta clase "empaqueta" todos los datos que tu UI necesita.
data class ActivityMetrics(
    val heartRate: String = "--- lpm",
    val distance: String = "--- km",
    val activeTime: String = "--:--",
    val calories: String = "--- kcal"
)


@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    // --- CAMBIO 2: Recibir los datos como parámetro ---
    metrics: ActivityMetrics, // En lugar de datos estáticos
    navigateToBack: () -> Unit
) {

    var isMapLoading by remember { mutableStateOf(true) }
    var mapError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val savedInstanceState = rememberSaveable { Bundle() }

    // --- (Toda la lógica del mapa y el ciclo de vida se mantiene igual) ---
    DisposableEffect(lifecycleOwner, mapView) {
        val observer = LifecycleEventObserver { _: LifecycleOwner, event: Lifecycle.Event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(savedInstanceState)
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> {
                    mapView.onPause()
                    mapView.onSaveInstanceState(savedInstanceState)
                }
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDestroy()
        }
    }
    // --- (Fin de la lógica del mapa) ---


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // MAPA: ocupa 75% del alto
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
        ) {
            // ... (Todo el código de AndroidView, Botón Regresar y Superposiciones
            //      se mantiene exactamente igual que en tu original) ...

            AndroidView(
                factory = {
                    mapView.apply {
                        addOnDidFailLoadingMapListener { errorMessage ->
                            Log.e("MapScreen", "Error al cargar el mapa: $errorMessage")
                            mapError = "Error al cargar el estilo: $errorMessage"
                            isMapLoading = false
                        }

                        getMapAsync(OnMapReadyCallback { maplibreMap ->
                            try {
                                maplibreMap.setStyle("https://demotiles.maplibre.org/style.json") {
                                    isMapLoading = false
                                }
                            } catch (e: Exception) {
                                Log.e("MapScreen", "Excepción: ${e.message}")
                                mapError = "Excepción: ${e.message}"
                                isMapLoading = false
                            }
                        })
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Botón de regreso arriba del mapa
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Button(
                    onClick = navigateToBack,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black.copy(alpha = 0.5f),
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("← Regresar")
                }
            }

            // Superposiciones del mapa (error o loading)
            when {
                mapError != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.85f))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No se pudo cargar el mapa 🗺️",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = mapError!!,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                isMapLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }


        // --- CAMBIO 3: PANEL DE DATOS AHORA USA EL OBJETO "metrics" ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF7F7F7))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Los valores ahora vienen del parámetro
                StatCellHalf(value = metrics.heartRate, label = "Ritmo Cardíaco")
                StatCellHalf(value = metrics.distance, label = "Km Recorridos")
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                // Los valores ahora vienen del parámetro
                StatCellHalf(value = metrics.activeTime, label = "Tiempo Activo")
                StatCellHalf(value = metrics.calories, label = "Calorías")
            }
        }
    }
}

// Esta función no necesita cambios, ya era reutilizable. ¡Bien hecho!
@Composable
fun StatCellHalf(value: String, label: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}