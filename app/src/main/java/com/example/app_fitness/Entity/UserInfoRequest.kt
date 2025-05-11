package com.example.app_fitness.Entity

import com.google.gson.annotations.SerializedName

data class UserInfoRequest(
    val id: Int,
    @SerializedName("userId")  // Ánh xạ "userId" từ JSON sang user_id
    val user_id: Int,
    val weight: Float,
    val height: Float,
    val age: Int,
    val medical_condition: String?,
    @SerializedName("workoutLevel")
    val workout_level: String?,
    @SerializedName("improvementGoal")
    val improvement_goal: String?,
    val gender: String
)