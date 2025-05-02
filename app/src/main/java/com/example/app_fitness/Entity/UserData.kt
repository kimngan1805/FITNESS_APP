package com.example.app_fitness.Entity

data class UserData(
    val userId: Int,
    val date: String,
    val weight: Float,
    val height: Int,
    val result: String?,
    val bodyFat: Int?,
    val neck: Float?,
    val shoulder: Float?,
    val chest: Float?,
    val waist: Float?
)
