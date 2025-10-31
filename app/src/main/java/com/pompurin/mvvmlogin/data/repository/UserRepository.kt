package com.pompurin.mvvmlogin.data.repository

import com.pompurin.mvvmlogin.data.model.*
import com.pompurin.mvvmlogin.data.remote.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// ========== SEALED CLASS PARA MANEJAR ESTADOS ==========
sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message: String) : Resource<T>()
    class Loading<T> : Resource<T>()
}

// ========== REPOSITORIO DE USUARIOS ==========
class UserRepository {

    private val api = ApiClient.userApi

    suspend fun register(
        name: String,
        email: String,
        password: String,
        birthDate: String,
        address: String
    ): Resource<User> {
        return withContext(Dispatchers.IO) {
            try {
                val request = RegisterRequest(name, email, password, birthDate, address)
                val response = api.register(request)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.success && body.data != null) {
                        Resource.Success(body.data)
                    } else {
                        Resource.Error(body.message)
                    }
                } else {
                    Resource.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Resource.Error("Error de conexión: ${e.message}")
            }
        }
    }

    suspend fun login(email: String, password: String): Resource<User> {
        return withContext(Dispatchers.IO) {
            try {
                val request = LoginRequest(email, password)
                val response = api.login(request)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.success && body.data != null) {
                        Resource.Success(body.data)
                    } else {
                        Resource.Error(body.message)
                    }
                } else {
                    Resource.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Resource.Error("Error de conexión: ${e.message}")
            }
        }
    }

    suspend fun getUserById(userId: Int, token: String): Resource<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getUserById(userId, "Bearer $token")

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.success && body.data != null) {
                        Resource.Success(body.data)
                    } else {
                        Resource.Error(body.message)
                    }
                } else {
                    Resource.Error("Error al obtener usuario")
                }
            } catch (e: Exception) {
                Resource.Error("Error: ${e.message}")
            }
        }
    }

    suspend fun updateUser(userId: Int, token: String, user: User): Resource<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.updateUser(userId, "Bearer $token", user)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.success && body.data != null) {
                        Resource.Success(body.data)
                    } else {
                        Resource.Error(body.message)
                    }
                } else {
                    Resource.Error("Error al actualizar usuario")
                }
            } catch (e: Exception) {
                Resource.Error("Error: ${e.message}")
            }
        }
    }
}
