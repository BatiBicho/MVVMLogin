package com.pompurin.mvvmlogin.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pompurin.mvvmlogin.domain.Weather
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random
import com.pompurin.mvvmlogin.data.WeatherRepository

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    private val _cities = MutableStateFlow<List<Weather>>(emptyList())
    val cities: StateFlow<List<Weather>> = _cities.asStateFlow()

    init {
        loadCities()
    }

    fun loadCities() {
        viewModelScope.launch {
            _cities.value = listOf(
                Weather(1, "Madrid", 25.0, "Soleado", 45, 5.0, "01d"),
                Weather(2, "Barcelona", 23.0, "Parcialmente nublado", 60, 7.0, "02d"),
                Weather(3, "Valencia", 27.0, "Soleado", 40, 3.0, "01d")
            )
        }
    }

    fun getWeather(city: String) {
        viewModelScope.launch {
            _weatherState.value = WeatherState.Loading
            try {
                // En una app real, usarías la API real
                // val response = RetrofitClient.instance.getWeatherByCity(city, "TU_API_KEY")
                // _weatherState.value = WeatherState.Success(mapToWeather(response))

                // Simulación
                delay(1000) // Simular llamada a API
                val fakeWeather = Weather(
                    id = Random.nextInt(),
                    city = city,
                    temperature = (15..30).random().toDouble(),
                    description = listOf("Soleado", "Nublado", "Lluvioso").random(),
                    humidity = (30..80).random(),
                    windSpeed = (1..15).random().toDouble(),
                    icon = "01d"
                )
                _weatherState.value = WeatherState.Success(fakeWeather)
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error("Error al obtener el clima: ${e.message}")
            }
        }
    }
}

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val weather: Weather) : WeatherState()
    data class Error(val message: String) : WeatherState()
}