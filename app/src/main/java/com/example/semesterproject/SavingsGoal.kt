package com.example.semesterproject

// SavingsGoal.kt
data class SavingsGoal(
    val id: Int,
    val title: String,
    val targetAmount: Int,
    val currentAmount: Int,
    val targetDate: String? = null // Optional
) {
    /**
     * Calculates the progress percentage towards the goal.
     */
    val progressPercent: Int
        get() = if (targetAmount > 0) {
            ((currentAmount / targetAmount) * 100).toInt()
        } else 0

    /**
     * Calculates the remaining amount needed.
     */
    val remainingAmount: Int
        get() = targetAmount - currentAmount
}