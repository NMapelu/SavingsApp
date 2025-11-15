// SavingsGoalActivity.kt

package com.example.semesterproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.semesterproject.data.local.GoalsRepository
import com.example.semesterproject.data.local.LocalGoalsDataSource
import com.example.semesterproject.ui.theme.GoalViewModel
import com.example.semesterproject.ui.theme.GoalViewModelFactory

class SavingsGoalActivity : ComponentActivity() {

    private val viewModel: GoalViewModel by lazy {
        val localDataSource = LocalGoalsDataSource
        val repository = GoalsRepository(localDataSource)
        val factory = GoalViewModelFactory(repository)
        ViewModelProvider(this, factory)[GoalViewModel::class.java]
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- Compose Entry Point ---
        setContent {
            MaterialTheme {
                SavingsGoalScreen(
                    viewModel = viewModel,
                    onAddNewGoal = {
                        startActivity(Intent(this, NewGoalActivity::class.java))
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshGoals()
    }
}