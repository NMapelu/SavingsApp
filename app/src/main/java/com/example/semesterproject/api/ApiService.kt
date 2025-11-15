// api/ApiService.kt

package com.example.semesterproject.api

import com.example.semesterproject.SavingsGoal

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.PUT
import retrofit2.http.Path // Used to insert the ID into the URL path

interface ApiService {

    // 1. Endpoint to CREATE a new goal (POST request)
    @POST("/api/goals")
    suspend fun createGoal(@Body goal: SavingsGoal): SavingsGoal

    // 2. Endpoint to RETRIEVE all goals (GET request)
    @GET("/api/goals")
    suspend fun getGoals(): List<SavingsGoal>

    // 3. Endpoint to UPDATE an existing goal (PUT request)
    // The server needs the ID, which is included in the URL using @Path.
    @PUT("/api/goals/{id}")
    suspend fun updateGoal(
        @Path("id") goalId: Int, // The ID from the URL path
        @Body goal: SavingsGoal // The updated goal object
    ): SavingsGoal

    // 4. Endpoint to DELETE a goal (DELETE request)
    // The server only needs the ID from the URL path.
    @DELETE("/api/goals/{id}")
    suspend fun deleteGoal(@Path("id") goalId: Int): Response<Unit>
    // Response<Unit> means we expect a successful response (like 204 No Content) but no data body.
}