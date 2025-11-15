package com.example.semesterproject

import com.example.semesterproject.SavingsGoal
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.example.semesterproject.api.RetrofitClient
import com.example.semesterproject.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale

class EditGoalActivity : AppCompatActivity() {

    private lateinit var titleInput: TextInputEditText
    private lateinit var targetAmountInput: TextInputEditText
    private lateinit var currentAmountInput: TextInputEditText
    private lateinit var saveChangesButton: Button
    private lateinit var deleteButton: Button

    private var originalGoal: SavingsGoal? = null
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_goal)

        // Initialize Views (R.id. names must match activity_edit_goal.xml)
        titleInput = findViewById(R.id.edit_text_goal_title)
        targetAmountInput = findViewById(R.id.edit_text_target_amount)
        currentAmountInput = findViewById(R.id.edit_text_current_amount)
        saveChangesButton = findViewById(R.id.button_save_changes)
        deleteButton = findViewById(R.id.button_delete_goal)

        loadGoalData()

        saveChangesButton.setOnClickListener { updateGoal() }
        deleteButton.setOnClickListener { deleteGoalConfirmation() }
    }

    private fun loadGoalData() {
        // Retrieve data passed from SavingsGoalActivity
        val id = intent.getIntExtra("GOAL_ID", -1)
        if (id == -1) {
            Toast.makeText(this, "Error: Goal ID not found.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        originalGoal = SavingsGoal(
            id = id,
            title = intent.getStringExtra("GOAL_TITLE") ?: "",
            targetAmount = intent.getIntExtra("GOAL_TARGET_AMOUNT", 0),
            currentAmount = intent.getIntExtra("GOAL_CURRENT_AMOUNT", 0),
            targetDate = intent.getStringExtra("GOAL_TARGET_DATE")
        )

        // Populate the UI
        titleInput.setText(originalGoal?.title)

        // Use proper string formatting to display amounts (no decimals for KSH)
        val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
        numberFormat.minimumFractionDigits = 0
        numberFormat.maximumFractionDigits = 0

        targetAmountInput.setText(numberFormat.format(originalGoal?.targetAmount))
        currentAmountInput.setText(numberFormat.format(originalGoal?.currentAmount))
    }

    // --- UPDATE (PUT Request) ---
    private fun updateGoal() {
        val currentGoal = originalGoal ?: return

        // Input Validation and Data Extraction
        val newTitle = titleInput.text.toString().trim()
        val newTargetAmount = targetAmountInput.text.toString().toIntOrNull()
        val newCurrentAmount = currentAmountInput.text.toString().toIntOrNull() ?: 0

        if (newTitle.isEmpty() || newTargetAmount == null || newTargetAmount <= 0) {
            Toast.makeText(this, "Please enter valid Title and Target Amount.", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedGoal = currentGoal.copy(
            title = newTitle,
            targetAmount = newTargetAmount,
            currentAmount = newCurrentAmount
        )

        mainScope.launch {
            try {
                val apiService = RetrofitClient.instance

                val result: SavingsGoal = withContext(Dispatchers.IO) {
                    apiService.updateGoal(currentGoal.id, updatedGoal)
                }

                Toast.makeText(this@EditGoalActivity, "Goal '${result.title}' updated!", Toast.LENGTH_SHORT).show()
                finish()

            } catch (e: Exception) {
                Toast.makeText(
                    this@EditGoalActivity,
                    "ERROR updating goal: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // --- DELETE (DELETE Request) ---
    private fun deleteGoalConfirmation() {
        val goalToDelete = originalGoal ?: return

        AlertDialog.Builder(this)
            .setTitle("Delete Goal")
            .setMessage("Are you sure you want to delete '${goalToDelete.title}'? This cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                executeDelete(goalToDelete.id)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun executeDelete(goalId: Int) {
        mainScope.launch {
            try {
                val apiService = RetrofitClient.instance

                withContext(Dispatchers.IO) {
                    apiService.deleteGoal(goalId)
                }

                Toast.makeText(this@EditGoalActivity, "Goal deleted successfully!", Toast.LENGTH_LONG).show()
                finish()

            } catch (e: Exception) {
                Toast.makeText(
                    this@EditGoalActivity,
                    "ERROR deleting goal: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}