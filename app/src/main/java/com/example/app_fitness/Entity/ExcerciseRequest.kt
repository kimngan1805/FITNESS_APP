package com.example.app_fitness.Entity


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize

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
    val image_url: String,
    val unlock_order: Int,
    val detail_name: String

): Parcelable
