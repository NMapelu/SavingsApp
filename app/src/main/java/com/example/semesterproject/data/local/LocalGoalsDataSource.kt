package com.example.semesterproject.data.local

import com.example.semesterproject.SavingsGoal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Singleton object to simulate local, in-memory storage for goals.
 * Data persists only while the application process is alive.
 */
object LocalGoalsDataSource {
    private var nextId = 5 // Starting ID after mock goals

    // Mutable list to hold the in-memory data
    private val goalsList = mutableListOf(
        SavingsGoal(
            id = 1,
            title = "Vacation Fund",
            targetAmount = 2500,
            currentAmount = 1500,
            targetDate = "2025-12-31"
        ),
        SavingsGoal(
            id = 2,
            title = "New Laptop",
            targetAmount = 1200,
            currentAmount = 200,
            targetDate = "2024-10-01"
        ),
        SavingsGoal(
            id = 3,
            title = "Emergency Savings",
            targetAmount = 5000,
            currentAmount = 5000,
            targetDate = null
        ),
        SavingsGoal(
            id = 4,
            title = "Retirement Booster",
            targetAmount = 10000,
            currentAmount = 1000,
            targetDate = "2030-01-01"
        )
    )

    // StateFlow to expose the current list to the Repository/ViewModel
    private val _goals = MutableStateFlow(goalsList.toList())
    val goals: StateFlow<List<SavingsGoal>> = _goals.asStateFlow()

    // --- CRUD Operations ---

    // CREATE: Adds a new goal and updates the flow immediately
    fun createGoal(newGoal: SavingsGoal): SavingsGoal {
        val goalWithId = newGoal.copy(id = nextId++)
        goalsList.add(goalWithId)
        // CRITICAL: Update the StateFlow to instantly notify the UI
        _goals.value = goalsList.toList()
        return goalWithId
    }

    // UPDATE: Finds and replaces a goal, then updates the flow
    fun updateGoal(updatedGoal: SavingsGoal): SavingsGoal {
        val index = goalsList.indexOfFirst { it.id == updatedGoal.id }
        if (index != -1) {
            goalsList[index] = updatedGoal
            _goals.value = goalsList.toList()
            return updatedGoal
        }
        throw NoSuchElementException("Goal with ID ${updatedGoal.id} not found for update.")
    }

    // DELETE: Removes a goal and updates the flow
    fun deleteGoal(goalId: Int) {
        val removed = goalsList.removeIf { it.id == goalId }
        if (removed) {
            _goals.value = goalsList.toList()
        }
    }
}