package com.example.semesterproject

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // IMPORTANT: Use your computer's IP address (e.g., 192.168.1.5)
    // or the special Android Emulator IP '10.0.2.2' to access 'localhost'
    private const val BASE_URL = "http://10.0.2.2:3000"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

