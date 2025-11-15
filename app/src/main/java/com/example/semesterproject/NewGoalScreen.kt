package com.example.semesterproject

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.util.Calendar



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewGoalScreen(onGoalSaved: () -> Unit) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // State variables to hold form input
    var title by remember { mutableStateOf("") }
    var targetAmount by remember { mutableStateOf("") }
    var currentAmount by remember { mutableStateOf("0.00") }
    var selectedDate by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Create a New Savings Goal") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Goal Title Input (Replaces TextInputEditText 1)
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Goal Name (e.g., New Laptop)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 2. Target Amount Input (Replaces TextInputEditText 2)
            OutlinedTextField(
                value = targetAmount,
                onValueChange = { targetAmount = it },
                label = { Text("Target Amount ($)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 3. Current Amount Input (Replaces TextInputEditText 3)
            OutlinedTextField(
                value = currentAmount,
                onValueChange = { currentAmount = it },
                label = { Text("Currently Saved ($)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 4. Select Target Date Button (Replaces Button 4)
            Button(
                onClick = {
                    // TODO: Replace with a proper DatePicker dialog in Compose
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.MONTH, 3)
                    selectedDate = calendar.timeInMillis
                    // Show a toast placeholder
                    android.widget.Toast.makeText(context, "Date picker would open here!", android.widget.Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Target Date (Optional)")
            }

            // 5. Selected Date Text (Replaces TextView 5)
            Text(
                text = if (selectedDate != null) {
                    "Target: ${android.text.format.DateFormat.format("MM/dd/yyyy", selectedDate!!)}"
                } else {
                    "No target date set"
                },
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 6. Save Goal Button (Replaces Button 6)
            Button(
                onClick = {
                    saveGoalCompose(title, targetAmount, currentAmount, selectedDate, context, onGoalSaved)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Save Goal", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

// Separate function for goal saving logic
fun saveGoalCompose(
    title: String,
    targetAmountStr: String,
    currentAmountStr: String,
    targetDate: Long?,
    context: android.content.Context,
    onGoalSaved: () -> Unit
) {
    // --- Input Validation (Similar to original Kotlin logic) ---
    val targetAmount = targetAmountStr.toIntOrNull()
    val currentAmount = currentAmountStr.toIntOrNull() ?: 0

    if (title.isBlank() || targetAmount == null || targetAmount <= 0) {
        android.widget.Toast.makeText(context, "Please enter a valid Goal Name and Target Amount.", android.widget.Toast.LENGTH_LONG).show()
        return
    }

    if (currentAmount > targetAmount) {
        android.widget.Toast.makeText(context, "Current amount cannot exceed the target.", android.widget.Toast.LENGTH_LONG).show()
        return
    }

    // Create the goal object (requires your SavingsGoal data class)
    val newGoal = SavingsGoal(
        id = -1,
        title = title,
        targetAmount = targetAmount,
        currentAmount = currentAmount,
        targetDate = targetDate?.let { it.toString() }
    )

    // TODO: Network call logic goes here...

    android.widget.Toast.makeText(context, "Goal '${newGoal.title}' created!", android.widget.Toast.LENGTH_LONG).show()
    onGoalSaved()
}