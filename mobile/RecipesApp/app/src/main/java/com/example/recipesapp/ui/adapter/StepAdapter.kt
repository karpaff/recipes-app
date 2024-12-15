package com.example.recipesapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R

class StepAdapter(private val steps: List<String>) :
    RecyclerView.Adapter<StepAdapter.StepViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_step, parent, false)
        return StepViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.bind(position + 1, steps[position])
    }

    override fun getItemCount(): Int = steps.size

    class StepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val stepNumberTextView: TextView = itemView.findViewById(R.id.step_number)
        private val stepDescriptionTextView: TextView = itemView.findViewById(R.id.step_description)

        fun bind(stepNumber: Int, stepDescription: String) {
            stepNumberTextView.text = "$stepNumber."
            stepDescriptionTextView.text = stepDescription
        }
    }
}
