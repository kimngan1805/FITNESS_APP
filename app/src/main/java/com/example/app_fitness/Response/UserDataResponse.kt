package com.example.app_fitness.Response

data class UserDataResponse(
    val success: Boolean,
    val message: String,
    val weight: Float?,
    val height: Int?
)
