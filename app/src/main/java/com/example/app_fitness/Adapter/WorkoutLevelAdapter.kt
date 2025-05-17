package com.example.app_fitness.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_fitness.Entity.WorkoutLevel
import com.example.app_fitness.R

class WorkoutLevelAdapter(
    private val workoutLevels: List<WorkoutLevel>,
    private val onItemClick: (WorkoutLevel) -> Unit
) : RecyclerView.Adapter<WorkoutLevelAdapter.WorkoutLevelViewHolder>() {

    class WorkoutLevelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val levelImage: ImageView = itemView.findViewById(R.id.levelImage)
        val levelName: TextView = itemView.findViewById(R.id.levelName)
        val levelDescription: TextView = itemView.findViewById(R.id.levelDescription)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutLevelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_workout_level, parent, false)
        return WorkoutLevelViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutLevelViewHolder, position: Int) {
        val workoutLevel = workoutLevels[position]

        holder.levelName.text = workoutLevel.level_name
        holder.levelDescription.text = workoutLevel.description

        Glide.with(holder.itemView.context)
            .load(workoutLevel.image_url)
            .into(holder.levelImage)

        holder.itemView.setOnClickListener {
            onItemClick(workoutLevel)
        }
    }

    override fun getItemCount(): Int = workoutLevels.size
}
