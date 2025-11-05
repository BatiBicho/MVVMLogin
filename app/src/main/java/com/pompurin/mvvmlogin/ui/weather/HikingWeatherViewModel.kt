package com.pompurin.mvvmlogin.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pompurin.mvvmlogin.data.model.WeatherResponse
import com.pompurin.mvvmlogin.data.repository.Resource
import com.pompurin.mvvmlogin.data.repository.WeatherRepository
import kotlinx.coroutines.launch

// Data class para recomendación de senderismo
data class HikingRecommendation(
    val isRecommended: Boolean,
    val score: Int, // 0-100
    val reason: String,
    val tips: List<String>
)

class HikingWeatherViewModel : ViewModel() {

    private val weatherRepository = WeatherRepository()

    private val _weather = MutableLiveData<WeatherResponse?>()
    val weather: LiveData<WeatherResponse?> = _weather

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _hikingRecommendation = MutableLiveData<HikingRecommendation?>()
    val hikingRecommendation: LiveData<HikingRecommendation?> = _hikingRecommendation

    // Obtener clima por coordenadas (automático del GPS)
    fun getWeatherByLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = weatherRepository.getCurrentWeatherByCoordinates(latitude, longitude)) {
                is Resource.Success -> {
                    _weather.value = result.data
                    // Calcular recomendación de senderismo
                    _hikingRecommendation.value = calculateHikingRecommendation(result.data)
                }
                is Resource.Error -> {
                    _errorMessage.value = result.message
                }
                is Resource.Loading -> {
                    // Ya manejado
                }
            }

            _isLoading.value = false
        }
    }

    // Obtener clima por nombre de ciudad
    fun getWeatherByCity(cityName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = weatherRepository.getCurrentWeather(cityName)) {
                is Resource.Success -> {
                    _weather.value = result.data
                    _hikingRecommendation.value = calculateHikingRecommendation(result.data)
                }
                is Resource.Error -> {
                    _errorMessage.value = result.message
                }
                is Resource.Loading -> {
                    // Ya manejado
                }
            }

            _isLoading.value = false
        }
    }

    // Algoritmo para calcular si es recomendable hacer senderismo
    private fun calculateHikingRecommendation(weather: WeatherResponse): HikingRecommendation {
        val current = weather.current
        var score = 100
        val tips = mutableListOf<String>()
        var reason = ""

        // Evaluar temperatura (ideal: 10-25°C)
        when {
            current.tempC < 5 -> {
                score -= 30
                tips.add("🧥 Hace mucho frío, lleva ropa abrigada")
                reason = "Temperatura muy baja"
            }
            current.tempC < 10 -> {
                score -= 15
                tips.add("🧥 Hace frío, viste en capas")
            }
            current.tempC > 30 -> {
                score -= 25
                tips.add("💧 Hace mucho calor, lleva mucha agua")
                reason = "Temperatura muy alta"
            }
            current.tempC > 25 -> {
                score -= 10
                tips.add("🌞 Usa protector solar y gorra")
            }
            else -> {
                tips.add("👌 Temperatura ideal para senderismo")
            }
        }

        // Evaluar precipitación
        if (current.precipMm > 0) {
            score -= 40
            tips.add("☔ Está lloviendo, lleva impermeable")
            if (reason.isEmpty()) reason = "Está lloviendo"
        }

        // Evaluar condición del clima
        val condition = current.condition.text.lowercase()
        when {
            condition.contains("rain") || condition.contains("lluvia") -> {
                score -= 30
                if (reason.isEmpty()) reason = "Lluvia"
            }
            condition.contains("storm") || condition.contains("tormenta") -> {
                score -= 50
                tips.add("⚠️ Tormenta, NO es seguro hacer senderismo")
                reason = "Tormenta eléctrica"
            }
            condition.contains("snow") || condition.contains("nieve") -> {
                score -= 40
                tips.add("❄️ Nieve, requiere equipo especializado")
                reason = "Nieve"
            }
            condition.contains("fog") || condition.contains("niebla") -> {
                score -= 20
                tips.add("🌫️ Niebla, ten cuidado con la visibilidad")
            }
            condition.contains("sunny") || condition.contains("clear") -> {
                tips.add("☀️ Día despejado, perfecto para caminata")
            }
        }

        // Evaluar viento (ideal: < 20 km/h)
        when {
            current.windKph > 40 -> {
                score -= 30
                tips.add("💨 Viento fuerte, ten precaución")
                if (reason.isEmpty()) reason = "Viento muy fuerte"
            }
            current.windKph > 20 -> {
                score -= 10
                tips.add("🌬️ Viento moderado, lleva cortavientos")
            }
        }

        // Evaluar visibilidad (ideal: > 5 km)
        if (current.visKm < 5) {
            score -= 20
            tips.add("👁️ Visibilidad reducida, ten cuidado")
        }

        // Evaluar índice UV (> 6 es alto)
        if (current.uv > 6) {
            score -= 5
            tips.add("🧴 UV alto, usa protector solar SPF 50+")
        }

        // Evaluar humedad (muy alta puede ser incómoda)
        if (current.humidity > 80) {
            score -= 10
            tips.add("💦 Humedad alta, te sentirás más acalorado")
        }

        // Asegurar que el score esté entre 0-100
        score = score.coerceIn(0, 100)

        // Determinar si es recomendable
        val isRecommended = score >= 60

        // Generar razón principal si no hay ninguna
        if (reason.isEmpty()) {
            reason = when {
                score >= 80 -> "Condiciones excelentes"
                score >= 60 -> "Condiciones buenas"
                score >= 40 -> "Condiciones aceptables"
                else -> "Condiciones desfavorables"
            }
        }

        // Agregar tips generales si las condiciones son buenas
        if (score >= 60) {
            tips.add(0, "✅ ¡Buen momento para hacer senderismo!")
            tips.add("🥾 Usa calzado apropiado")
            tips.add("📱 Lleva tu teléfono cargado")
            tips.add("🗺️ Informa a alguien de tu ruta")
        } else if (score < 40) {
            tips.add(0, "❌ No es recomendable hacer senderismo ahora")
        } else {
            tips.add(0, "⚠️ Condiciones moderadas, evalúa tu experiencia")
        }

        return HikingRecommendation(
            isRecommended = isRecommended,
            score = score,
            reason = reason,
            tips = tips
        )
    }

    fun clearError() {
        _errorMessage.value = null
    }
}