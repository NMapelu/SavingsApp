package com.example.semesterproject.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // !!! IMPORTANT !!!
    // If you are running your Node.js server on your computer and testing
    // with an Android Emulator, use "10.0.2.2" instead of "localhost".
    // If testing on a physical device, use your computer's network IP address.
    private const val BASE_URL = "http://10.0.2.2:3000" // Change 3000 if your server port is different

    // This logger will show network calls in your Logcat (for debugging)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create()) // Use GSON to convert JSON
            .build()
    }

    // This is what your ViewModels will call to get the API service
    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}