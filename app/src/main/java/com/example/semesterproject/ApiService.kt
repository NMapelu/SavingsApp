package com.example.semesterproject

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response

interface ApiService {

    @GET("/api/users/{userId}/challenges")
    suspend fun getChallenges(
        @Path("userId") userId: Int
    ): Response<ChallengeResponse>

}
