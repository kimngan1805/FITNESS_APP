package com.example.app_fitness.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_fitness.Entity.ExerciseRequest
import com.example.app_fitness.R

class ReviewAdapter(private val exercises: List<ExerciseRequest>) :
    RecyclerView.Adapter<ReviewAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.exerciseTitle)
        val time: TextView = itemView.findViewById(R.id.timeText)
        val calories: TextView = itemView.findViewById(R.id.caloriesText)
        val image: ImageView = itemView.findViewById(R.id.exerciseImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.title.text = exercise.exercise_name
        holder.time.text = "${exercise.time} Min"
        holder.calories.text = "${exercise.muscle_group} Kcal"
        Glide.with(holder.itemView.context)
            .load(exercise.image_url)
            .into(holder.image)
    }

    override fun getItemCount() = exercises.size
}
