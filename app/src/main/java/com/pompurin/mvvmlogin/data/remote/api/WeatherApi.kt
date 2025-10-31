package com.pompurin.mvvmlogin.data.remote.api


import com.pompurin.mvvmlogin.data.model.*
import retrofit2.Response
import retrofit2.http.*

// ========== API DE CLIMA (OpenWeatherMap) ==========
interface WeatherApi {

    // Obtener clima actual por ciudad
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric", // metric = Celsius
        @Query("lang") language: String = "es"
    ): Response<Weather>

    // Obtener clima actual por coordenadas
    @GET("weather")
    suspend fun getCurrentWeatherByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "es"
    ): Response<Weather>

    // Pronóstico de 5 días
    @GET("forecast")
    suspend fun getForecast(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "es"
    ): Response<Weather>
}