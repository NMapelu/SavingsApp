package com.example.semesterproject.data.model

// This is what we send TO the server when logging in
data class LoginRequest(
    val email: String,
    val password: String
)