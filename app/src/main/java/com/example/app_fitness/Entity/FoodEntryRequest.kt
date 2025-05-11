package com.example.app_fitness.Entity

data class FoodEntryRequest(
    val user_id: Int,
    val food_name: String,
    val quantity: Int,
    val log_date: Long // Thêm trường timestamp
)