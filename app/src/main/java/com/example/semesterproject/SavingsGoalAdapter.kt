package com.example.semesterproject

import com.example.semesterproject.SavingsGoal
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class SavingsGoalAdapter(
    private val goalList: List<SavingsGoal>,
    private val onGoalClick: (SavingsGoal) -> Unit // Added click listener
) :
    RecyclerView.Adapter<SavingsGoalAdapter.GoalViewHolder>() {

    // Create KSH currency formatter
    private val currencyFormat = NumberFormat.getCurrencyInstance(java.util.Locale("en", "KE")).apply {
        currency = java.util.Currency.getInstance("KES")
    }

    class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.text_goal_title)
        val summary: TextView = itemView.findViewById(R.id.text_goal_progress_summary)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progress_goal_bar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_savings_goal, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = goalList[position]
        holder.title.text = goal.title

        holder.progressBar.max = 100
        holder.progressBar.progress = goal.progressPercent

        // Uses getString and R.string.goal_progress_summary (assumed fixed)
        holder.summary.text = holder.itemView.context.getString(
            R.string.goal_progress_summary,
            currencyFormat.format(goal.currentAmount),
            currencyFormat.format(goal.targetAmount),
            goal.progressPercent
        )

        // Sets the click listener
        holder.itemView.setOnClickListener {
            onGoalClick(goal)
        }
    }

    override fun getItemCount() = goalList.size
}