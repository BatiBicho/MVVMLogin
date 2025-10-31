package com.pompurin.mvvmlogin.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pompurin.mvvmlogin.data.model.Weather
import com.pompurin.mvvmlogin.data.repository.Resource
import com.pompurin.mvvmlogin.data.repository.WeatherRepository
import kotlinx.coroutines.launch

// ========== VIEW MODEL ==========
class WeatherViewModel : ViewModel() {

    private val weatherRepository = WeatherRepository()

    private val _weather = MutableLiveData<Weather?>()
    val weather: LiveData<Weather?> = _weather

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // Obtener clima por nombre de ciudad
    fun getWeatherByCity(cityName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = weatherRepository.getCurrentWeather(cityName)) {
                is Resource.Success -> {
                    _weather.value = result.data
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

    // Obtener clima por coordenadas (útil con GPS)
    fun getWeatherByCoordinates(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = weatherRepository.getCurrentWeatherByCoordinates(latitude, longitude)) {
                is Resource.Success -> {
                    _weather.value = result.data
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

    fun clearError() {
        _errorMessage.value = null
    }
}