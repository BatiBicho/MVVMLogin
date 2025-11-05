package com.pompurin.mvvmlogin.ui.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pompurin.mvvmlogin.R

// --- Funciones de ayuda para obtener los colores ---
fun colorFromHex(hex: String): Color {
    return Color(android.graphics.Color.parseColor("#$hex"))
}

@Composable
fun Home(
    navigateToBack: () -> Unit,
    navigateToDetail: (String) -> Unit,
    navigateToBlank: () -> Unit,
    navigateToWeather: () -> Unit,
    navigateToSettings: () -> Unit
) {
    val circleButtons = listOf(
        Triple("Clima", Icons.Default.Warning, "weather"),
        Triple("Rutas", Icons.Default.List, "lista"),
        Triple("Ajustes", Icons.Default.Settings, "ajustes"),
        Triple("Acerca de", Icons.Default.Info, "acerca")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))
        Text(
            text = "WALK-PIP",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.weight(1f))

        // --- Recuadro de botones circulares ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(16.dp)
                )
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Primera fila: Clima y Lista
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                circleButtons.take(2).forEach { (label, icon, route) ->
                    CircularButton(
                        icon = icon,
                        label = label,
                        onClick = {
                            if (route == "weather") {
                                navigateToWeather()
                            } else {
                                navigateToDetail(route)
                            }
                        },
                        buttonColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.onBackground,
                        iconColor = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Segunda fila: Ajustes y Acerca de
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                CircularButton(
                    icon = Icons.Default.Settings,
                    label = "Settings",
                    onClick = { navigateToSettings() },
                    buttonColor = MaterialTheme.colorScheme.primary,
                    iconColor = MaterialTheme.colorScheme.onPrimary,
                    textColor = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(Modifier.height(20.dp))

            // --- Botón "Pantalla en blanco" con mismo estilo ---
            CircularButton(
                icon = Icons.Default.Info, // puedes cambiar el ícono luego
                label = "Mapa",
                onClick = { navigateToDetail("map") },
                buttonColor = MaterialTheme.colorScheme.primary,
                iconColor = MaterialTheme.colorScheme.onPrimary,
                textColor = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(Modifier.weight(1f))

        // --- Botón Regresar ---
        Button(
            onClick = { navigateToBack() },
            modifier = Modifier.fillMaxWidth(0.6f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Regresar")
        }
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
fun CircularButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    buttonColor: Color,
    iconColor: Color,
    textColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(buttonColor)
                .padding(12.dp),
            tint = iconColor
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

@Composable
fun CardWeather(){

}
