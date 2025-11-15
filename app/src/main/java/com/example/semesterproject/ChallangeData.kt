package com.example.semesterproject

import com.google.gson.annotations.SerializedName

// Matches the challenge object returned by the API
data class Challenge(
    val id: Int,
    val title: String, // Maps to 'challenge_type' from DB
    val daily_amount: Double,
    @SerializedName("target_amount")
    val targetAmount: Double,
    val contributed: Double, // Maps to 'current_balance' from DB
    @SerializedName("days_active")
    val daysActive: Int,
    @SerializedName("progress_percent")
    val progressPercent: Double
) {
    // 💡 Add a computed property to generate the 'summary' required by showPopup()
    val summary: String
        get() = """
            Goal: $title
            Target: KES ${String.format("%.2f", targetAmount)}
            Contributed: KES ${String.format("%.2f", contributed)}
            Daily Save: KES ${String.format("%.2f", daily_amount)}
            Days Active: $daysActive
        """.trimIndent()
}

// Matches the full JSON response wrapper
data class ChallengeResponse(
    val success: Boolean,
    @SerializedName("challenges")
    val challengeList: List<Challenge>
)