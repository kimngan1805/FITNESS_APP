package com.example.app_fitness.Response

data class CommentCountResponse(
    val success: Boolean,
    val message: String,
    val count: Int
)