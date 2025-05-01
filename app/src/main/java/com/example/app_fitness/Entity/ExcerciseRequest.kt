package com.example.app_fitness.Entity


import com.google.gson.annotations.SerializedName

data class ExerciseRequest(
    val id: Int,
    @SerializedName("exercise_name")
    val exercise_name: String,
    @SerializedName("exercise_level_id")
    val exercise_level_id: Int,
    @SerializedName("exercise_category_id")
    val category_id: Int,
    val gender: String,
    val description: String,
    val note: String,
    val day: Int,
    @SerializedName("link")
    val video_url: String,
    @SerializedName("image_url")
    val image_url: String
)
