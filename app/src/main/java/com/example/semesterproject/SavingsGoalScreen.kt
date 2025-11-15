// SavingsGoalScreen.kt (New File)

package com.example.semesterproject

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.semesterproject.ui.theme.GoalViewModel
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsGoalScreen(
    viewModel: GoalViewModel,
    onAddNewGoal: () -> Unit
) {
    // Observes the Flow from the ViewModel. UI recomposes automatically when goals change.
    val goals by viewModel.goals.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("My Goals") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNewGoal) {
                Icon(Icons.Filled.Add, contentDescription = "Add New Goal")
            }
        },
        content = { padding ->
            if (goals.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No goals found. Start creating one!", style = MaterialTheme.typography.titleMedium)
                }
            } else {
                LazyColumn( // Compose's efficient alternative to RecyclerView
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(goals, key = { it.id }) { goal ->
                        GoalItem(
                            goal = goal,
                            onAddAmount = { viewModel.addAmountToGoal(goal.id, it) },
                            onDelete = { viewModel.deleteGoal(goal.id) }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun GoalItem(
    goal: SavingsGoal,
    onAddAmount: (Int) -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAddAmountDialog by remember { mutableStateOf(false) }
    var amountToAdd by remember { mutableStateOf("") }

    // Create KSH currency formatter
    val currencyFormat = NumberFormat.getCurrencyInstance(java.util.Locale("en", "KE")).apply {
        currency = java.util.Currency.getInstance("KES")
    }
    val progress = (goal.currentAmount.toFloat() / goal.targetAmount.toFloat()).coerceIn(0f, 1f)
    val progressPercent = (progress * 100).toInt()

    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Goal") },
            text = { Text("Are you sure you want to delete \"${goal.title}\"? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Add amount dialog
    if (showAddAmountDialog) {
        AlertDialog(
            onDismissRequest = { showAddAmountDialog = false },
            title = { Text("Add Amount to ${goal.title}") },
            text = {
                Column {
                    Text("Current: ${currencyFormat.format(goal.currentAmount)}")
                    Text("Target: ${currencyFormat.format(goal.targetAmount)}")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = amountToAdd,
                        onValueChange = { newValue ->
                            // Only allow numeric input
                            if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                amountToAdd = newValue
                            }
                        },
                        label = { Text("Amount to Add") },
                        placeholder = { Text("Enter amount") },
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val amount = amountToAdd.toIntOrNull() ?: 0
                        if (amount > 0) {
                            onAddAmount(amount)
                            amountToAdd = ""
                            showAddAmountDialog = false
                        }
                    },
                    enabled = amountToAdd.toIntOrNull()?.let { it > 0 } == true
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showAddAmountDialog = false
                    amountToAdd = ""
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        showDeleteDialog = true
                    }
                )
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = goal.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                // Long press indicator or delete button could go here
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${currencyFormat.format(goal.currentAmount)} / ${currencyFormat.format(goal.targetAmount)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "$progressPercent%",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { showAddAmountDialog = true }) {
                    Text("Add Amount")
                }
                TextButton(
                    onClick = { showDeleteDialog = true },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            }
        }
    }
}