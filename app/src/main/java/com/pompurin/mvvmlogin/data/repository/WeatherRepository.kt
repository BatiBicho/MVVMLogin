package com.pompurin.mvvmlogin.data.repository

import com.pompurin.mvvmlogin.data.model.*
import com.pompurin.mvvmlogin.data.remote.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// ========== REPOSITORIO DE CLIMA ==========
class WeatherRepository {

    private val api = ApiClient.weatherApi

    // IMPORTANTE: Obtén tu API key gratis en https://www.weatherapi.com/
    private val API_KEY = "e836e4b1f0e6482d9fa65902250111"

    suspend fun getCurrentWeather(location: String): Resource<WeatherResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCurrentWeather(
                    apiKey = API_KEY,
                    location = location
                )

                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error("No se pudo obtener el clima: ${response.message()}")
                }
            } catch (e: Exception) {
                Resource.Error("Error de conexión: ${e.localizedMessage ?: e.message}")
            }
        }
    }

    suspend fun getCurrentWeatherByCoordinates(
        latitude: Double,
        longitude: Double
    ): Resource<WeatherResponse> {
        return withContext(Dispatchers.IO) {
            try {
                // Weather API acepta coordenadas en formato "lat,lon"
                val location = "$latitude,$longitude"
                val response = api.getCurrentWeather(
                    apiKey = API_KEY,
                    location = location
                )

                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error("No se pudo obtener el clima")
                }
            } catch (e: Exception) {
                Resource.Error("Error: ${e.localizedMessage ?: e.message}")
            }
        }
    }

    suspend fun getForecast(location: String, days: Int = 3): Resource<WeatherResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getForecast(
                    apiKey = API_KEY,
                    location = location,
                    days = days
                )

                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error("No se pudo obtener el pronóstico")
                }
            } catch (e: Exception) {
                Resource.Error("Error: ${e.localizedMessage ?: e.message}")
            }
        }
    }
}