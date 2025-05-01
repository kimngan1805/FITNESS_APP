package com.example.app_fitness.Response

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val fullname: String?, // Thêm fullname
    val age: Int?          // Thêm age

)
