package com.pompurin.mvvmlogin.data.repository

import com.pompurin.mvvmlogin.data.model.*
import com.pompurin.mvvmlogin.data.remote.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// ========== REPOSITORIO DE CLIMA ==========
class WeatherRepository {

    private val api = ApiClient.weatherApi
    private val API_KEY = "TU_API_KEY_DE_OPENWEATHER" // Obtén tu key en openweathermap.org

    suspend fun getCurrentWeather(cityName: String): Resource<Weather> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCurrentWeather(
                    cityName = cityName,
                    apiKey = API_KEY
                )

                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error("No se pudo obtener el clima: ${response.message()}")
                }
            } catch (e: Exception) {
                Resource.Error("Error de conexión: ${e.message}")
            }
        }
    }

    suspend fun getCurrentWeatherByCoordinates(
        latitude: Double,
        longitude: Double
    ): Resource<Weather> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCurrentWeatherByCoordinates(
                    latitude = latitude,
                    longitude = longitude,
                    apiKey = API_KEY
                )

                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error("No se pudo obtener el clima")
                }
            } catch (e: Exception) {
                Resource.Error("Error: ${e.message}")
            }
        }
    }
}