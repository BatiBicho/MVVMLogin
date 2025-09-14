package com.pompurin.mvvmlogin.domain

data class Weather(
    val id: Int,
    val city : String,
    val temperature : Double,
    val description  : String,
    val humidity : Int,
    val windSpeed : Double,
    val icon : String
)

data class WeatherResponse(
    val weather : List<WeatherInfo>,
    val main : MainInfo,
    val wind : WindInfo,
    val name : String
)

data class WeatherInfo(
    val id : Int,
    val main : String,
    val description: String,
    val icon: String
)

data class MainInfo(
    val temp: Double,
    val humidity: Int
)

data class WindInfo(
    val speed : Double
)