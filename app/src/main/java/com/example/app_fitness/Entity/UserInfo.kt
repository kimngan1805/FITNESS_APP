package com.example.app_fitness.Entity

import com.google.gson.annotations.SerializedName


data class UserInfo(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("fullname")
    val fullname: String,
    val email: String,
    val gender: String,
    @SerializedName("workout_level")
    val workoutLevel: String,
    @SerializedName("medical_condition")
    val medicalCondition: String,
    @SerializedName("improvement_goal")
    val improvementGoal: String,
    val weight: Float,
    val height: Float
)
