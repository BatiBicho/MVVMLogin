@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.pompurin.mvvmlogin.navigation

import kotlinx.serialization.Serializable

@Serializable
data object LoginScreen

@Serializable
data object HomeScreen

@Serializable
data class DetailScreen(val id: String)

@Serializable
data object Blank
