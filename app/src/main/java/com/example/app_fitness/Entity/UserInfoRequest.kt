package com.example.app_fitness.Entity

data class UserInfoRequest (
    val user_id: Int,
    val weight: Float,
    val height: Int,
    val age: Int,
    val medical_condition: String,
    val workout_level: String,
    val improvement_goal: String
)