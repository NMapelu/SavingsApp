
package com.example.semesterproject

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.semesterproject.ChallengeResponse
import com.example.semesterproject.Challenge
import com.example.semesterproject.RetrofitClient
import com.example.semesterproject.R

class MainActivity : AppCompatActivity() {

    // --- New Properties to hold all challenge cards ---
    private lateinit var challengeCards: List<CardView>
    private lateinit var challengeTitles: List<TextView>
    private lateinit var challengeProgressBars: List<LinearProgressIndicator>
    private var fetchedChallenges: List<Challenge> = emptyList()

    // --- POPUP VIEWS ---
    private lateinit var popupLayout: LinearLayout
    private lateinit var popupTitle: TextView
    private lateinit var popupDetails: TextView
    private lateinit var closePopup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // --- View Initialization ---
        val card1 = findViewById<CardView>(R.id.cardChallenge1)
        val card2 = findViewById<CardView>(R.id.cardChallenge2)
        val card3 = findViewById<CardView>(R.id.cardChallenge3)
        val card4 = findViewById<CardView>(R.id.cardChallenge4)
        challengeCards = listOf(card1, card2, card3, card4)

        // Assuming you add TextViews with IDs like 'cardTitle1', 'cardTitle2', etc. to your XML
        // And ProgressBars with IDs like 'progressBar1', 'progressBar2', etc.
        // For now, we'll use the existing TextView inside the CardView
        challengeTitles = listOf(
            card1.findViewById(R.id.cardTitle1), // ⚠️ Update XML to include this ID
            card2.findViewById(R.id.cardTitle2), // ⚠️ Update XML to include this ID
            card3.findViewById(R.id.cardTitle3), // ⚠️ Update XML to include this ID
            card4.findViewById(R.id.cardTitle4)  // ⚠️ Update XML to include this ID
        )

        challengeProgressBars = listOf(
            card1.findViewById(R.id.progressBar1), // ⚠️ Update XML to include this ID
            card2.findViewById(R.id.progressBar2), // ⚠️ Update XML to include this ID
            card3.findViewById(R.id.progressBar3), // ⚠️ Update XML to include this ID
            card4.findViewById(R.id.progressBar4)  // ⚠️ Update XML to include this ID
        )

        popupLayout = findViewById(R.id.popupLayout)
        popupTitle = findViewById(R.id.popupTitle)
        popupDetails = findViewById(R.id.popupDetails)
        closePopup = findViewById(R.id.closePopup)

        // --- Setup Logic ---
        setupToolbar()
        setupPopup()
        fetchChallenges() // Fetch data dynamically
        setupCardClickListeners() // Set up listeners AFTER fetching data

        // --- HANDLE SYSTEM INSETS (Edge-to-Edge layout) ---
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.user_profile -> { Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show(); true }
                R.id.menu_dashboard -> { Toast.makeText(this, "Dashboard clicked", Toast.LENGTH_SHORT).show(); true }
                R.id.menu_settings -> { Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show(); true }
                R.id.menu_logout -> { Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show(); true }
                else -> false
            }
        }
    }

    private fun setupPopup() {
        closePopup.setOnClickListener {
            popupLayout.visibility = View.GONE
        }
    }

    private fun setupCardClickListeners() {
        challengeCards.forEachIndexed { index, cardView ->
            cardView.setOnClickListener {
                // Ensure the index is within the bounds of the fetched data list
                if (index < fetchedChallenges.size) {
                    val challenge = fetchedChallenges[index]
                    showPopup(popupLayout, popupTitle, popupDetails, challenge.title, challenge.summary)
                } else {
                    Toast.makeText(this, "No data available for this card.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchChallenges() {
        // ⚠️ FIX 1: Define the user ID. Since you don't have a login, use a valid test ID (e.g., 1)
        val userId = 1

        // Run the network request on an IO (background) thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // ⚠️ FIX 2: Pass the userId to the getChallenges function
                val response = RetrofitClient.apiService.getChallenges(userId)

                // Switch back to the Main thread to update the UI
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        fetchedChallenges = response.body()!!.challengeList
                        updateDashboardUI(fetchedChallenges)
                    } else {
                        Toast.makeText(this@MainActivity, "Failed to load data: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                // Switch back to the Main thread to show the error
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Network Error: ${e.message}", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateDashboardUI(challenges: List<Challenge>) {
        challenges.forEachIndexed { index, challenge ->
            if (index < challengeCards.size) {
                // Update Card Title
                challengeTitles[index].text = challenge.title

                // Update Progress Bar
                // Progress is an integer from 0 to 100
                challengeProgressBars[index].progress = challenge.progressPercent.toInt().coerceIn(0, 100)
            }
        }

        // Hide unused cards if fewer than 4 challenges were fetched
        for (i in challenges.size until challengeCards.size) {
            challengeCards[i].visibility = View.GONE
        }
    }

    // --- POPUP HELPER FUNCTION (Unchanged) ---
    private fun showPopup(
        popupLayout: LinearLayout,
        popupTitle: TextView,
        popupDetails: TextView,
        title: String,
        details: String
    ) {
        popupTitle.text = title
        popupDetails.text = details
        popupLayout.visibility = View.VISIBLE
    }
}