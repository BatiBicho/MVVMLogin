package com.pompurin.mvvmlogin.data.remote.api

import com.pompurin.mvvmlogin.data.model.*
import retrofit2.Response
import retrofit2.http.*

// ========== API DE MAPAS (Google Maps) ==========
interface MapsApi {

    // Obtener ruta entre dos puntos
    @GET("directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String, // "lat,lng" o dirección
        @Query("destination") destination: String,
        @Query("key") apiKey: String,
        @Query("mode") mode: String = "driving", // driving, walking, bicycling, transit
        @Query("language") language: String = "es"
    ): Response<DirectionsResponse>

    // Geocoding: convertir dirección a coordenadas
    @GET("geocode/json")
    suspend fun geocodeAddress(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): Response<Location>

    // Reverse Geocoding: convertir coordenadas a dirección
    @GET("geocode/json")
    suspend fun reverseGeocode(
        @Query("latlng") latLng: String, // "lat,lng"
        @Query("key") apiKey: String
    ): Response<Location>

    // Buscar lugares cercanos
    @GET("place/nearbysearch/json")
    suspend fun searchNearbyPlaces(
        @Query("location") location: String, // "lat,lng"
        @Query("radius") radius: Int, // en metros
        @Query("type") type: String, // restaurant, hospital, etc.
        @Query("key") apiKey: String
    ): Response<Location>
}