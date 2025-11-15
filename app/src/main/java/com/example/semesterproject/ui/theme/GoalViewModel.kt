package com.example.semesterproject.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.semesterproject.SavingsGoal
import com.example.semesterproject.data.local.GoalsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GoalViewModel(private val repository: GoalsRepository) : ViewModel() {

    private val _goals = MutableStateFlow<List<SavingsGoal>>(emptyList())
    val goals: StateFlow<List<SavingsGoal>> = _goals.asStateFlow()


    init {
        // Load local data immediately for instant UI display
        _goals.value = repository.getLocalGoals()
        // Then refresh from network in the background
        refreshGoals()
    }

    fun refreshGoals() {
        viewModelScope.launch {
            try {
                // Fetch from network (with fast timeout), updates UI when complete
                val goalList = repository.getGoals()
                _goals.value = goalList
            } catch (e: Exception) {
                // Handle error (e.g., log it or set an error state)
                println("Error fetching goals: ${e.message}")
                // Keep existing data instead of clearing on error
                if (_goals.value.isEmpty()) {
                    _goals.value = repository.getLocalGoals()
                }
            }
        }
    }

    class GoalViewModelFactory(private val repository: GoalsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GoalViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GoalViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    fun addNewGoal(title: String, targetAmount: Int) {
        // Basic placeholder for creating a new goal
        val newGoal = SavingsGoal(
            id = 0, // ID will be assigned by the local data source
            title = title,
            targetAmount = targetAmount,
            currentAmount = 0,
            targetDate = "" // Handle date input properly in your NewGoalActivity
        )
        viewModelScope.launch {
            repository.createGoal(newGoal)
            refreshGoals()
        }
    }

    // Add method to save a complete SavingsGoal object
    fun createGoal(goal: SavingsGoal) {
        viewModelScope.launch {
            // Create a temporary goal with a temporary ID for immediate UI update
            val tempId = (_goals.value.maxOfOrNull { it.id } ?: 0) + 1
            val tempGoal = goal.copy(id = tempId)
            
            // Optimistically add to UI immediately BEFORE network call
            _goals.value = _goals.value + tempGoal
            
            // Then sync with network in background
            try {
                val createdGoal = repository.createGoal(goal)
                // Replace temp goal with the real one from server (with correct ID)
                _goals.value = _goals.value.map { if (it.id == tempId) createdGoal else it }
            } catch (e: Exception) {
                println("Error creating goal: ${e.message}")
                // If creation fails, remove the temp goal and refresh
                _goals.value = _goals.value.filter { it.id != tempId }
                refreshGoals()
            }
        }
    }

    // Add other functions for delete and update here
    fun deleteGoal(goalId: Int) {
        viewModelScope.launch {
            // Optimistically remove from UI immediately
            _goals.value = _goals.value.filter { it.id != goalId }
            
            // Then sync with network in background
            try {
                repository.deleteGoal(goalId)
            } catch (e: Exception) {
                println("Error deleting goal: ${e.message}")
                // If network fails, refresh to restore the goal
                refreshGoals()
            }
        }
    }

    // Update goal - used for adding amounts or updating any goal property
    fun updateGoal(goal: SavingsGoal) {
        viewModelScope.launch {
            // Optimistically update UI immediately
            _goals.value = _goals.value.map { if (it.id == goal.id) goal else it }
            
            // Then sync with network in background
            try {
                repository.updateGoal(goal.id, goal)
            } catch (e: Exception) {
                println("Error updating goal: ${e.message}")
                // If network fails, refresh to ensure consistency
                refreshGoals()
            }
        }
    }

    // Add amount to a goal
    fun addAmountToGoal(goalId: Int, amountToAdd: Int) {
        viewModelScope.launch {
            try {
                val currentGoal = _goals.value.find { it.id == goalId }
                if (currentGoal != null) {
                    // Optimistically update UI immediately
                    val updatedGoal = currentGoal.copy(
                        currentAmount = (currentGoal.currentAmount + amountToAdd).coerceAtMost(currentGoal.targetAmount)
                    )
                    // Update local state immediately for instant feedback
                    _goals.value = _goals.value.map { if (it.id == goalId) updatedGoal else it }
                    
                    // Then sync with network in background
                    try {
                        repository.updateGoal(goalId, updatedGoal)
                    } catch (e: Exception) {
                        println("Error syncing update to network: ${e.message}")
                        // If network fails, refresh from local to ensure consistency
                        refreshGoals()
                    }
                }
            } catch (e: Exception) {
                println("Error adding amount to goal: ${e.message}")
            }
        }
    }
}