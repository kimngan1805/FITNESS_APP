package com.example.app_fitness.Entity

import com.google.gson.annotations.SerializedName

data class ExerciseHistoryItem(
    @SerializedName("exercise_name")
    val name: String,

    @SerializedName("image_url")
    val imageUrl: String
)
