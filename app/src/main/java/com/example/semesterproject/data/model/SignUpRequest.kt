package com.example.semesterproject.data.model

// This is what we send TO the server when signing up
data class SignUpRequest(
    val fullName: String,
    val email: String,
    val password: String
)