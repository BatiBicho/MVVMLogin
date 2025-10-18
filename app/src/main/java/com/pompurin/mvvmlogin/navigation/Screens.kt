package com.pompurin.mvvmlogin.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Home

// Route with params
@Serializable
data class Detail(val id:String)