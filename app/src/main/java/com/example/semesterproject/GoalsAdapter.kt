package com.example.semesterproject

// GoalsAdapter.kt



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.semesterproject.SavingsGoal // Assuming your data class is named com.example.semesterproject.SavingsGoal

// Placeholder layout for a single goal item (you'll need to create res/layout/item_goal.xml)
class GoalsAdapter : ListAdapter<SavingsGoal, GoalsAdapter.GoalViewHolder>(GoalDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_savings_goal, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.text_goal_title)
        private val amountTextView: TextView = itemView.findViewById(R.id.edit_text_target_amount)

        fun bind(goal: SavingsGoal) {
            titleTextView.text = goal.title
            val progress = goal.currentAmount / goal.targetAmount
            amountTextView.text = "$${goal.currentAmount} / $${goal.targetAmount} (${"%.0f".format(progress * 100)}%)"
        }
    }
}

// DiffUtil is crucial for efficient RecyclerView updates
class GoalDiffCallback : DiffUtil.ItemCallback<SavingsGoal>() {
    override fun areItemsTheSame(oldItem: SavingsGoal, newItem: SavingsGoal): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SavingsGoal, newItem: SavingsGoal): Boolean {
        return oldItem == newItem
    }
}