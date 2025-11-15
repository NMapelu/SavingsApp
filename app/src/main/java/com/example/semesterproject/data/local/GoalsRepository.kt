package com.example.semesterproject.data.local

import com.example.semesterproject.SavingsGoal
import com.example.semesterproject.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Handles data operations, prioritizing network persistence over local mock data.
 * This repository is the bridge between the ViewModel and the external API.
 */
class GoalsRepository(private val localDataSource: LocalGoalsDataSource) {
    // Note: The localDataSource is now unused for primary operations but kept for compatibility.

    // 1. READ (FIXES UNRESOLVED REFERENCE ERROR)
    /**
     * Fetches the current list of goals from the network API, falls back to local storage if network fails.
     * Returns local data immediately for faster UI response.
     */
    suspend fun getGoals(): List<SavingsGoal> = withContext(Dispatchers.IO) {
        // Return local data immediately for instant UI display
        val localGoals = localDataSource.goals.value
        
        // Try to fetch from network in the background and update if successful
        try {
            val networkGoals = RetrofitClient.instance.getGoals()
            // If network call succeeds, return network data
            networkGoals
        } catch (e: Exception) {
            println("Repository Error fetching goals, using local storage: ${e.message}")
            // Fallback to local storage if network fails
            localGoals
        }
    }
    
    /**
     * Gets local goals immediately without network call - for instant UI loading
     */
    fun getLocalGoals(): List<SavingsGoal> {
        return localDataSource.goals.value
    }

    // 2. CREATE (Uses Network, falls back to local storage if network fails)
    suspend fun createGoal(newGoal: SavingsGoal): SavingsGoal = withContext(Dispatchers.IO) {
        // Save to local storage first for immediate availability
        val localGoal = localDataSource.createGoal(newGoal)
        
        // Then try to sync with network in background
        try {
            val networkGoal = RetrofitClient.instance.createGoal(newGoal)
            // If network succeeds, update local storage with network response (which has correct ID)
            localDataSource.updateGoal(networkGoal)
            networkGoal
        } catch (e: Exception) {
            println("Network error creating goal, using local storage: ${e.message}")
            // Return the locally created goal
            localGoal
        }
    }

    // 3. UPDATE (Uses Network, falls back to local storage if network fails)
    suspend fun updateGoal(id: Int, updatedGoal: SavingsGoal): SavingsGoal = withContext(Dispatchers.IO) {
        try {
            RetrofitClient.instance.updateGoal(id, updatedGoal)
        } catch (e: Exception) {
            println("Network error updating goal, using local storage: ${e.message}")
            // Fallback to local storage if network fails
            localDataSource.updateGoal(updatedGoal)
        }
    }

    // 4. DELETE (Uses Network, falls back to local storage if network fails)
    suspend fun deleteGoal(goalId: Int) = withContext(Dispatchers.IO) {
        try {
            RetrofitClient.instance.deleteGoal(goalId)
        } catch (e: Exception) {
            println("Network error deleting goal, using local storage: ${e.message}")
            // Fallback to local storage if network fails
            localDataSource.deleteGoal(goalId)
        }
    }
}