package com.example.app_fitness.Entity

import com.google.gson.annotations.SerializedName


data class UserInfo(
    val id: Int,
    @SerializedName("userId")  // Ánh xạ "userId" từ JSON sang user_id
    val user_id: Int,
    val weight: Float,
    val height: Float,
    val age: Int,
    val medical_condition: String,
    val workout_level: String,
    val improvement_goal: String,
    val gender: String,
    val email: String
)