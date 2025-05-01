package com.example.app_fitness.Entity


data class SignUpRequest(
    val fullname: String,
    val username: String,
    val email: String,
    val password: String
)