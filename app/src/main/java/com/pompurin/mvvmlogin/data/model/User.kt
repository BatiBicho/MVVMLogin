package com.pompurin.mvvmlogin.data.model

import com.google.gson.annotations.SerializedName

// ========== MODELO DE USUARIO ==========
data class User(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("birth_date")
    val birthDate: String?,

    @SerializedName("address")
    val address: String?
)

// Request para registro
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val birthDate: String,
    val address: String
)

// Request para login
data class LoginRequest(
    val email: String,
    val password: String
)

// Response genérica de la API
data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: T?
)