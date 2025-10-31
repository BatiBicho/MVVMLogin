package com.pompurin.mvvmlogin.data.remote

import com.pompurin.mvvmlogin.data.remote.api.MapsApi
import com.pompurin.mvvmlogin.data.remote.api.UserApi
import com.pompurin.mvvmlogin.data.remote.api.WeatherApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // URL base de tu API principal
    private const val BASE_URL = "https://tu-api.com/api/"

    // URL de API de clima (ejemplo: OpenWeatherMap)
    private const val WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/"

    // URL de API de mapas (si usas una diferente a Google Maps)
    private const val MAPS_BASE_URL = "https://maps.googleapis.com/maps/api/"

    // Configuración del cliente HTTP con logging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Retrofit para tu API principal
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Retrofit para API de clima
    private val weatherRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Retrofit para API de mapas
    private val mapsRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(MAPS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Función genérica para crear servicios de API
    fun <T> createService(serviceClass: Class<T>, useWeatherApi: Boolean = false, useMapsApi: Boolean = false): T {
        return when {
            useWeatherApi -> weatherRetrofit.create(serviceClass)
            useMapsApi -> mapsRetrofit.create(serviceClass)
            else -> retrofit.create(serviceClass)
        }
    }
}

// Objeto para acceder fácilmente a tus APIs
object ApiClient {
    val userApi: UserApi by lazy {
        RetrofitClient.createService(UserApi::class.java)
    }

    val weatherApi: WeatherApi by lazy {
        RetrofitClient.createService(WeatherApi::class.java, useWeatherApi = true)
    }

    val mapsApi: MapsApi by lazy {
        RetrofitClient.createService(MapsApi::class.java, useMapsApi = true)
    }
}