package com.pompurin.mvvmlogin.ui.maps

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

@Composable
fun MapScreen(modifier: Modifier = Modifier, navigateToBack: () -> Unit) {

    // --- 1. Estado para manejar la carga y el error ---
    var isMapLoading by remember { mutableStateOf(true) }
    var mapError by remember { mutableStateOf<String?>(null) }


    // --- 2. Configuración del MapView y su ciclo de vida ---

    val context = LocalContext.current
    val mapView = remember {
        MapView(context)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    val savedInstanceState = rememberSaveable { Bundle() }

    // ✅ Control del ciclo de vida (Sin cambios, esto está bien)
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

    // --- 3. Definición de la UI (Layout) ---

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        // El mapa va primero (capa inferior)
        AndroidView(
            factory = {
                mapView.apply {

                    // ✅ AÑADIDO: Listener para capturar errores de carga
                    addOnDidFailLoadingMapListener { errorMessage ->
                        Log.e("MapScreen", "Error al cargar el mapa: $errorMessage")
                        mapError = "Error al cargar el estilo del mapa: $errorMessage"
                        isMapLoading = false
                    }

                    getMapAsync(OnMapReadyCallback { maplibreMap ->
                        try {
                            maplibreMap.setStyle("https://demotiles.maplibre.org/style.json") {
                                // Éxito: El estilo se cargó
                                isMapLoading = false
                            }
                        } catch (e: Exception) {
                            // Captura de excepción síncrona (por si acaso)
                            Log.e("MapScreen", "Excepción al setear estilo: ${e.message}")
                            mapError = "Excepción: ${e.message}"
                            isMapLoading = false
                        }
                    })
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // El botón va después (capa superior), siempre visible
        Button(
            onClick = { navigateToBack() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("Regresar")
        }

        // --- 4. Superposiciones de Estado (Error o Carga) ---

        // ✅ AÑADIDO: Muestra un mensaje de error si 'mapError' no es nulo
        if (mapError != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
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
                    text = mapError!!, // Sabemos que no es nulo aquí
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
        // ✅ AÑADIDO: Muestra un indicador de carga mientras 'isMapLoading' es true
        else if (isMapLoading) {
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