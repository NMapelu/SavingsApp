package com.example.semesterproject

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.widget.Toast
import com.example.semesterproject.ui.theme.GoalViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewGoalFormScreen(
    viewModel: GoalViewModel,
    onGoalSaved: () -> Unit
) {
    val context = LocalContext.current

    // State variables manage the text input fields
    var title by remember { mutableStateOf("") }
    var targetAmount by remember { mutableStateOf("") }
    var currentAmount by remember { mutableStateOf("0") }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) } // State for date picker visibility

    // Simple state for validation UI feedback
    var titleError by remember { mutableStateOf(false) }
    var targetAmountError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Create New Goal") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Create a New Savings Goal",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            // Goal Title Input
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    titleError = it.isBlank()
                },
                label = { Text("Goal Name (e.g., New Laptop)") },
                modifier = Modifier.fillMaxWidth(),
                isError = titleError,
                supportingText = if (titleError) { { Text("Goal name is required") } } else null
            )

            // Target Amount Input
            OutlinedTextField(
                value = targetAmount,
                onValueChange = { targetAmount = it.filter { c -> c.isDigit() } },
                label = { Text("Target Amount (Ksh)") },
                keyboardOptions = KeyboardOptions(
                    // Use the correct built-in type
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth(),
                isError = targetAmountError,
                supportingText = if (targetAmountError) { { Text("Enter a valid target amount (> ksh0)") } } else null
            )

            // Currently Saved Input
            OutlinedTextField(
                value = currentAmount,
                onValueChange = { currentAmount = it.filter { c -> c.isDigit() || c == '.' } },
                label = { Text("Currently Saved (Ksh)") },
                keyboardOptions = KeyboardOptions(
                    // Use the correct built-in type
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Select Target Date Button
            Button(
                onClick = { showDatePicker = true }, // Set state to show the dialog
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Select Target Date (Optional)")
            }

            // Selected Date Text
            Text(
                text = if (selectedDate != null) {
                    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                    "Target: ${dateFormat.format(selectedDate!!)}"
                } else {
                    "No target date set"
                },
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium
            )

            // Save Goal Button
            Button(
                onClick = {
                    val saved = saveGoalCompose(title, targetAmount, currentAmount, selectedDate, context, viewModel)
                    if (saved) onGoalSaved()
                    else {
                        // Update error states for validation feedback
                        titleError = title.isBlank()
                        targetAmountError = targetAmount.toIntOrNull() == null || targetAmount.toIntOrNull()!! <= 0
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp).padding(top = 16.dp)
            ) {
                Text("Save Goal", style = MaterialTheme.typography.titleMedium)
            }
        }
    }

    // DatePickerDialog Implementation
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate ?: Calendar.getInstance().timeInMillis
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDate = datePickerState.selectedDateMillis
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

// Data class for the Goal (Assuming this is defined elsewhere or should be here)
//data class SavingsGoal(
//    val id: Int,
//    val title: String,
//    val targetAmount: Double,
//    val currentAmount: Double,
//    val targetDate: String? // Store as String for simplicity in this example
//)

// Function containing saving logic (mirrors the saveGoal() in the original activity)
fun saveGoalCompose(
    title: String,
    targetAmountStr: String,
    currentAmountStr: String,
    targetDate: Long?,
    context: android.content.Context,
    viewModel: GoalViewModel
): Boolean {
    // FIX 1: Change from toDoubleOrNull() to toIntOrNull() for whole KSh
    val targetAmount = targetAmountStr.toIntOrNull()
    val currentAmount = currentAmountStr.toIntOrNull() ?: 0 // Use Int default (0)

    // Check for null or invalid target amount
    if (title.isBlank() || targetAmount == null || targetAmount <= 0) return false

    // FIX 2: Check that both values are valid Ints before comparing.
    // The previous error was that the compiler couldn't guarantee targetAmount was an Int here.
    // The check above 'targetAmount == null' ensures it is safe to use targetAmount as an Int.
    if (currentAmount > targetAmount) {
        Toast.makeText(context, "Current amount cannot exceed the target.", Toast.LENGTH_LONG).show()
        return false
    }

    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    // FIX 3: Pass the safely unwrapped Int values to the SavingsGoal constructor
    // (Assuming you've also fixed SavingsGoal.kt to use Ints)
    val newGoal = SavingsGoal(
        id = -1, // Will be assigned by the repository
        title = title,
        targetAmount = targetAmount,
        currentAmount = currentAmount,
        targetDate = targetDate?.let { dateFormat.format(it) }
    )

    // FIX: Actually save the goal using the ViewModel
    viewModel.createGoal(newGoal)

    Toast.makeText(context, "Goal '${newGoal.title}' created!", Toast.LENGTH_LONG).show()
    return true
}