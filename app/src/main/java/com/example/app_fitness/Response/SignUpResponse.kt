package com.example.app_fitness.Response


data class SignUpResponse(
    val success: Boolean,
    val message: String,
    val user_id: Int? // Trả về user_id khi đăng ký thành công
)