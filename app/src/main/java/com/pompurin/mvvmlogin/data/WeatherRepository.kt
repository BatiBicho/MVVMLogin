package com.pompurin.mvvmlogin.data

import com.pompurin.mvvmlogin.domain.Weather
import com.pompurin.mvvmlogin.domain.WeatherResponse

class WeatherRepository(private val weatherApiService: WeatherApiService) {

    suspend fun getWeatherByCity(city: String, apiKey: String): Weather {
        val response = weatherApiService.getWeatherByCity(city, apiKey)
        return mapToWeather(response)
    }

    private fun mapToWeather(response: WeatherResponse): Weather {
        return Weather(
            id = response.weather.first().id,
            city = response.name,
            temperature = response.main.temp,
            description = response.weather.first().description,
            humidity = response.main.humidity,
            windSpeed = response.wind.speed,
            icon = response.weather.first().icon
        )
    }
}