// NewGoalActivity.kt

package com.example.semesterproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.ViewModelProvider
import com.example.semesterproject.data.local.GoalsRepository
import com.example.semesterproject.data.local.LocalGoalsDataSource
import com.example.semesterproject.ui.theme.GoalViewModel
import com.example.semesterproject.ui.theme.GoalViewModelFactory

class NewGoalActivity : ComponentActivity() {
    private val viewModel: GoalViewModel by lazy {
        val localDataSource = LocalGoalsDataSource
        val repository = GoalsRepository(localDataSource)
        val factory = GoalViewModelFactory(repository)
        ViewModelProvider(this, factory)[GoalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                // Pass the ViewModel to the form screen so it can save goals
                NewGoalFormScreen(
                    viewModel = viewModel,
                    onGoalSaved = { finish() }
                )
            }
        }
    }
}