package com.example.semesterproject.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    /**
     * !!! IMPORTANT !!!
     * This is a placeholder. You must replace this string with the actual
     * IP address and port where your Node.js backend is running.
     * Example for AVD: "http://10.0.2.2:3000/"
     * Example for physical device on local network: "http://192.168.1.100:3000/"
     */
    private const val BASE_URL = "http://192.168.100.17:3000/"
    // Note: The app will crash if this placeholder URL is used for real API calls.

    // Configure OkHttpClient with reasonable timeouts for faster response
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(3, TimeUnit.SECONDS) // 3 seconds to establish connection
        .readTimeout(5, TimeUnit.SECONDS) // 5 seconds to read response
        .writeTimeout(5, TimeUnit.SECONDS) // 5 seconds to write request
        .build()

    // The 'lazy' delegate ensures the Retrofit instance is created only once when first accessed.
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Use the configured OkHttpClient
            // Add the converter factory to handle Kotlin/JSON conversion
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            // Create the service interface implementation
            .create(ApiService::class.java)
    }
}