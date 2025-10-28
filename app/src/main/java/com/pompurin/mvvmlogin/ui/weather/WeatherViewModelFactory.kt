package com.pompurin.mvvmlogin.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pompurin.mvvmlogin.data.WeatherRepository

// Esta clase hereda de ViewModelProvider.Factory
class WeatherViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {

    // ¡Aquí sí es correcto usar 'override'!
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}