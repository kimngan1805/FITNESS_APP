package com.example.app_fitness.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_fitness.Activity.ExerciseDetailActivity
import com.example.app_fitness.Entity.ExerciseRequest
import com.example.app_fitness.R

class ExerciseAdapter(private val exercises: List<ExerciseRequest>) :
    RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseImage: ImageView = itemView.findViewById(R.id.exerciseImage)
        val exerciseName: TextView = itemView.findViewById(R.id.exerciseName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_excercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.exerciseName.text = exercise.exercise_name

        Glide.with(holder.itemView.context)
            .load(exercise.image_url)
            .into(holder.exerciseImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ExerciseDetailActivity::class.java)
            intent.putExtra("exercise_name", exercise.exercise_name)
            intent.putExtra("exercise_level_id", exercise.exercise_level_id)
            intent.putExtra("exercise_image_url", exercise.image_url)
            intent.putExtra("exercise_video_url", exercise.video_url)
            intent.putExtra("gender", exercise.gender)
            // Truyền thêm các dữ liệu khác nếu cần
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = exercises.size
}
