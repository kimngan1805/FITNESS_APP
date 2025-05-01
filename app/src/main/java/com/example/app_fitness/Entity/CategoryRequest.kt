package com.example.app_fitness.Entity

data class CategoryRequest(
    val category_id: Int,
    val category_name: String,
    val image_url: String,
    val gender: String
)