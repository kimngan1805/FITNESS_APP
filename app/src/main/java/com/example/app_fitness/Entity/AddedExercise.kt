package com.example.app_fitness.Entity

data class AddedExercise(
    val exercise_id: Int,
    val user_id: Int,
    val is_completed: Int = 0, // Giá trị mặc định là 0 (chưa hoàn thành)
    val completed_at: String? = null // Giá trị mặc định là null
)