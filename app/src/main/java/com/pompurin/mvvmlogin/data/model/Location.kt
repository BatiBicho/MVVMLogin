package com.pompurin.mvvmlogin.data.model

import com.google.gson.annotations.SerializedName

// ========== MODELO DE UBICACIÓN ==========
data class Location(
    @SerializedName("lat")
    val latitude: Double,

    @SerializedName("lng")
    val longitude: Double,

    @SerializedName("name")
    val name: String,

    @SerializedName("address")
    val address: String?
)

// Response de Google Maps Directions API
data class DirectionsResponse(
    @SerializedName("routes")
    val routes: List<Route>,

    @SerializedName("status")
    val status: String
)

data class Route(
    @SerializedName("legs")
    val legs: List<Leg>,

    @SerializedName("overview_polyline")
    val overviewPolyline: Polyline
)

data class Leg(
    @SerializedName("distance")
    val distance: Distance,

    @SerializedName("duration")
    val duration: Duration,

    @SerializedName("start_address")
    val startAddress: String,

    @SerializedName("end_address")
    val endAddress: String
)

data class Distance(
    @SerializedName("text")
    val text: String,

    @SerializedName("value")
    val value: Int
)

data class Duration(
    @SerializedName("text")
    val text: String,

    @SerializedName("value")
    val value: Int
)

data class Polyline(
    @SerializedName("points")
    val points: String
)