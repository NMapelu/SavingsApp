package com.example.semesterproject.data.network

import com.example.semesterproject.data.model.AuthResponse
import com.example.semesterproject.data.model.LoginRequest
import com.example.semesterproject.data.model.SignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/auth/signup") // <-- Matches your Node.js route (e.g., app.post('/auth/signup', ...))
    suspend fun signUp(
        @Body signUpRequest: SignUpRequest
    ): Response<AuthResponse> // We expect an AuthResponse back

    @POST("/auth/login") // <-- Matches your Node.js route (e.g., app.post('/auth/login', ...))
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<AuthResponse>

    // TODO: Add your "Forgot Password" endpoint here later
    // @POST("/auth/forgot-password")
    // suspend fun forgotPassword(@Body email: String): Response<AuthResponse>
}