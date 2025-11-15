package com.example.semesterproject.ui.theme



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.semesterproject.data.local.GoalsRepository

// This factory class is required to create a ViewModel that takes a constructor argument (the repository).
class GoalViewModelFactory(private val repository: GoalsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GoalViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}