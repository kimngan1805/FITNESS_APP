package com.example.app_fitness.Adapter
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.app_fitness.Activity.ExerciseVideoDetailActivity
import com.example.app_fitness.Entity.ExerciseRequest
import com.example.app_fitness.R

class NextTrainingAdapter(
    private val context: Context,
    private val exercises: List<ExerciseRequest>,
    private val onExerciseClicked: (ExerciseRequest) -> Unit,
    var completedExerciseIds: List<Int> // Danh sách ID bài tập đã hoàn thành
) : RecyclerView.Adapter<NextTrainingAdapter.NextTrainingViewHolder>() {

    inner class NextTrainingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val statusIcon: ImageView = itemView.findViewById(R.id.statusIcon)
        val exerciseNameTextView: TextView = itemView.findViewById(R.id.exerciseNameTextView)

        init {
            itemView.setOnClickListener {
                val exercise = exercises[adapterPosition]
                if (isExerciseUnlocked(exercise.id)) { // Sử dụng exercise.id
                    onExerciseClicked(exercise)
                } else {
                    Toast.makeText(context, "Hoàn thành bài tập trước đó để mở khóa!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isExerciseUnlocked(exerciseId: Int): Boolean {
        // Kiểm tra xem bài tập CÓ NẰM TRONG danh sách đã hoàn thành không.  Đảo ngược logic.
        return !completedExerciseIds.contains(exerciseId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextTrainingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_next_training, parent, false)
        return NextTrainingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NextTrainingViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.exerciseNameTextView.text = exercise.exercise_name

        if (isExerciseUnlocked(exercise.id)) {
            holder.statusIcon.setImageResource(R.drawable.ic_unlock)
            holder.statusIcon.setColorFilter(null) // Loại bỏ màu tint
        } else {
            holder.statusIcon.setImageResource(R.drawable.ic_lock)
            holder.statusIcon.setColorFilter(
                ContextCompat.getColor(context, R.color.blue), // Sử dụng ContextCompat
                android.graphics.PorterDuff.Mode.SRC_IN
            ) // Màu xám
        }
    }

    override fun getItemCount() = exercises.size
}
