package com.pompurin.mvvmlogin.ui.Home

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// --- Data class para los datos del gráfico ---
data class BarChartData(
    val label: String,
    val value: Float // <-- Ahora esto representa KM reales
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEstadisticas(
    navController: NavController
) {
    // --- 1. ESTADO DE LA UI ---
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Esta semana", "Semana pasada")
    var showDebugControls by remember { mutableStateOf(false) }

    // --- 2. ESTADO DE LOS DATOS (para la muestra) ---
    var hidratacionState by remember { mutableStateOf("2.3L") }
    var promedioPasosState by remember { mutableStateOf("8750") }
    var kmRecorridosState by remember { mutableStateOf("75.5 Km") }
    var caloriasState by remember { mutableStateOf("4,500 Kcal") }

    // --- CAMBIO 1: Los valores del gráfico ahora son KM reales ---
    var chartDataState by remember {
        mutableStateOf(
            listOf(
                BarChartData("L", 7.0f),
                BarChartData("M", 9.0f), // Este será el 100% de altura
                BarChartData("M", 5.0f),
                BarChartData("J", 6.0f),
                BarChartData("V", 8.0f),
                BarChartData("S", 3.0f),
                BarChartData("D", 5.5f)
            )
        )
    }

    // --- 3. ESTRUCTURA DE LA PANTALLA ---
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadísticas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // <-- Arreglado
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {
            // --- Pestañas ---
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            // --- Contenido Principal (Gráfico y Cuadrícula) ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Spacer(Modifier.height(16.dp))

                // --- Gráfico de Barras ---
                Text(
                    text = "RENDIMIENTO SEMANAL (KM)", // <-- Título actualizado
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                // Le pasamos los datos en KM
                WeeklyBarChart(data = chartDataState)

                Spacer(Modifier.height(32.dp))

                // --- Cuadrícula de Estadísticas 2x2 ---
                StatsGrid(
                    hidratacion = hidratacionState,
                    promedioPasos = promedioPasosState,
                    kmRecorridos = kmRecorridosState,
                    calorias = caloriasState
                )

                Spacer(Modifier.height(32.dp))

                // --- Panel de Control para Muestras ---
                DebugControls(
                    show = showDebugControls,
                    onShowChange = { showDebugControls = it },
                    // (El resto de los pases de estado están bien)
                    hidratacion = hidratacionState,
                    onHidratacionChange = { hidratacionState = it },
                    promedioPasos = promedioPasosState,
                    onPromedioPasosChange = { promedioPasosState = it },
                    kmRecorridos = kmRecorridosState,
                    onKmRecorridosChange = { kmRecorridosState = it },
                    calorias = caloriasState,
                    onCaloriasChange = { caloriasState = it },
                    chartData = chartDataState,
                    onChartDataChange = { chartDataState = it }
                )
            }
        }
    }
}


// --- 4. COMPOSABLES AUXILIARES ---

/**
 * Muestra el gráfico de barras semanal
 */
// --- CAMBIO 2: Lógica de WeeklyBarChart actualizada ---
@Composable
fun WeeklyBarChart(data: List<BarChartData>) {
    val maxChartHeight = 150.dp // Altura máxima en Dp

    // 1. Encontrar el valor máximo de KM en la lista.
    //    Usamos 1f como mínimo para evitar una división por cero si la lista está vacía.
    val maxKm = data.maxOfOrNull { it.value } ?: 1f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(maxChartHeight), // El contenedor tiene una altura fija
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom // Las barras y etiquetas crecen "hacia arriba"
    ) {
        data.forEach { barData ->
            // 2. Calcular el porcentaje de altura para esta barra (ej. 7.0km / 9.0km = 0.77f)
            val barHeightPercentage = if (maxKm > 0f) (barData.value / maxKm) else 0f

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom // <-- ¡CORREGIDO!
            ) {
                // 3. La Barra
                Box(
                    modifier = Modifier
                        .width(20.dp)
                        // La altura de la barra es un porcentaje de la altura MÁXIMA
                        .height(maxChartHeight * barHeightPercentage)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(MaterialTheme.colorScheme.primary)
                )

                // 4. La etiqueta (L, M, M, etc.)
                Text(
                    text = barData.label,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

/**
 * Muestra la cuadrícula de 4 estadísticas
 */
@Composable
fun StatsGrid(
    hidratacion: String,
    promedioPasos: String,
    kmRecorridos: String,
    calorias: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCell(
                label = "Hidratación:",
                value = hidratacion,
                icon = Icons.Default.WaterDrop,
                modifier = Modifier.weight(1f)
            )
            StatCell(
                label = "Promedio pasos:",
                value = promedioPasos,
                icon = Icons.Default.DirectionsRun,
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCell(
                label = "Km recorridos:",
                value = kmRecorridos,
                icon = Icons.Default.BarChart, // (Ícono de muestra)
                modifier = Modifier.weight(1f)
            )
            StatCell(
                label = "Calorías quemadas:",
                value = calorias,
                icon = Icons.Default.LocalFireDepartment,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Celda individual para la cuadrícula de estadísticas
 */
@Composable
fun StatCell(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Panel de control para "falsear" los datos de la muestra
 */
@Composable
fun DebugControls(
    // ... (parámetros sin cambios)
    show: Boolean,
    onShowChange: (Boolean) -> Unit,
    hidratacion: String,
    onHidratacionChange: (String) -> Unit,
    promedioPasos: String,
    onPromedioPasosChange: (String) -> Unit,
    kmRecorridos: String,
    onKmRecorridosChange: (String) -> Unit,
    calorias: String,
    onCaloriasChange: (String) -> Unit,
    chartData: List<BarChartData>,
    onChartDataChange: (List<BarChartData>) -> Unit
) {
    // Estado local solo para el textfield del gráfico
    var chartInput by remember {
        // Ahora mostrará "7.0,9.0,5.0,..."
        mutableStateOf(chartData.map { it.value.toString() }.joinToString(","))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // ... (Switch sin cambios) ...
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Controles de Muestra (Debug)",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Switch(checked = show, onCheckedChange = onShowChange)
            }


            if (show) {
                Spacer(Modifier.height(16.dp))
                // ... (Otros TextFields sin cambios) ...
                DemoTextField(
                    label = "Hidratación (ej. 2.5L)",
                    value = hidratacion,
                    onValueChange = onHidratacionChange
                )
                DemoTextField(
                    label = "Promedio Pasos (ej. 9000)",
                    value = promedioPasos,
                    onValueChange = onPromedioPasosChange
                )
                DemoTextField(
                    label = "Km Recorridos (ej. 80.0 Km)",
                    value = kmRecorridos,
                    onValueChange = onKmRecorridosChange
                )
                DemoTextField(
                    label = "Calorías (ej. 5,000 Kcal)",
                    value = calorias,
                    onValueChange = onCaloriasChange // <-- ¡CORREGIDO!
                )

                // --- CAMBIO 3: Etiqueta del TextField actualizada ---
                DemoTextField(
                    label = "Datos Gráfico (Km ej. 7.0,9.0,5.0,6.0,8.0,3.0,5.5)",
                    value = chartInput,
                    onValueChange = { newText ->
                        chartInput = newText
                        // La lógica para parsear los floats sigue siendo válida
                        try {
                            val newValues = newText.split(",")
                                .map { it.trim().toFloat() }
                            if (newValues.size == 7) {
                                onChartDataChange(
                                    listOf(
                                        BarChartData("L", newValues[0]),
                                        BarChartData("M", newValues[1]),
                                        BarChartData("M", newValues[2]),
                                        BarChartData("J", newValues[3]),
                                        BarChartData("V", newValues[4]),
                                        BarChartData("S", newValues[5]),
                                        BarChartData("D", newValues[6])
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            // Ignorar si el formato es incorrecto
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DemoTextField(
    // ... (Esta función no necesita cambios)
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    // ... (Sin cambios)
}