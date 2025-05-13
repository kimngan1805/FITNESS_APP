package com.example.app_fitness.Entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseDetailRequest(
    @SerializedName("id") val detailId: Int,
    @SerializedName("exercise_id") val exerciseId: Int,
    @SerializedName("unlock_order") val unlockOrder: Int,
    @SerializedName("detail_name") val detailName: String,
    @SerializedName("detail_video") val videoUrl: String,
) : Parcelable
