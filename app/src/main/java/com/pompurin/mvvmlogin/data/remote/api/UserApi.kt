package com.pompurin.mvvmlogin.data.remote.api


import com.pompurin.mvvmlogin.data.model.*
import retrofit2.Response
import retrofit2.http.*

// ========== API DE USUARIOS ==========
interface UserApi {

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<ApiResponse<User>>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse<User>>

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") userId: Int,
        @Header("Authorization") token: String
    ): Response<ApiResponse<User>>

    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") userId: Int,
        @Header("Authorization") token: String,
        @Body user: User
    ): Response<ApiResponse<User>>

    @DELETE("users/{id}")
    suspend fun deleteUser(
        @Path("id") userId: Int,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Unit>>

    @GET("users")
    suspend fun getAllUsers(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): Response<ApiResponse<List<User>>>
}