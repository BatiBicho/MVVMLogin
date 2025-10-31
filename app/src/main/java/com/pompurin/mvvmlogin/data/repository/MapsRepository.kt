package com.pompurin.mvvmlogin.data.repository

import com.pompurin.mvvmlogin.data.model.*
import com.pompurin.mvvmlogin.data.remote.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// ========== REPOSITORIO DE MAPAS ==========
class MapsRepository {

    private val api = ApiClient.mapsApi
    private val API_KEY = "TU_API_KEY_DE_GOOGLE_MAPS" // Obtén tu key en Google Cloud Console

    suspend fun getDirections(
        origin: String,
        destination: String,
        mode: String = "driving"
    ): Resource<DirectionsResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getDirections(
                    origin = origin,
                    destination = destination,
                    apiKey = API_KEY,
                    mode = mode
                )

                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error("No se pudo obtener la ruta")
                }
            } catch (e: Exception) {
                Resource.Error("Error: ${e.message}")
            }
        }
    }

    suspend fun geocodeAddress(address: String): Resource<Location> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.geocodeAddress(address, API_KEY)

                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error("No se pudo geocodificar la dirección")
                }
            } catch (e: Exception) {
                Resource.Error("Error: ${e.message}")
            }
        }
    }
}