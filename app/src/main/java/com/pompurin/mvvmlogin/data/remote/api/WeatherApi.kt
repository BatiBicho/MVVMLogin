package com.pompurin.mvvmlogin.data.remote.api

import com.pompurin.mvvmlogin.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    // Obtener clima actual por ciudad o ubicación
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String, // Puede ser: ciudad, código postal, coordenadas "lat,lon", IP, etc.
        @Query("aqi") aqi: String = "no", // Calidad del aire (yes/no)
        @Query("lang") language: String = "es" // Idioma de la respuesta
    ): Response<WeatherResponse>

    // Obtener pronóstico (forecast)
    @GET("forecast.json")
    suspend fun getForecast(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int = 3, // Días de pronóstico (1-10)
        @Query("aqi") aqi: String = "no",
        @Query("alerts") alerts: String = "no",
        @Query("lang") language: String = "es"
    ): Response<WeatherResponse>

    // Buscar ubicaciones
    @GET("search.json")
    suspend fun searchLocation(
        @Query("key") apiKey: String,
        @Query("q") query: String
    ): Response<List<com.pompurin.mvvmlogin.data.model.Location>>
}