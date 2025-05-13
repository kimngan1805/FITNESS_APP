package com.example.app_fitness.Adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.app_fitness.Entity.ExerciseDetailRequest
import com.example.app_fitness.R

class NextTrainingAdapter(
    private val context: Context,
    private val exercises: MutableList<ExerciseDetailRequest>,
    private val onExerciseClicked: (ExerciseDetailRequest) -> Unit
) : RecyclerView.Adapter<NextTrainingAdapter.NextTrainingViewHolder>() {

    inner class NextTrainingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val statusIcon: ImageView = itemView.findViewById(R.id.statusIcon)
        val exerciseNameTextView: TextView = itemView.findViewById(R.id.exerciseNameTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                val exercise = exercises[position]
                onExerciseClicked(exercise)
            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextTrainingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_next_training, parent, false)
        return NextTrainingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NextTrainingViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.exerciseNameTextView.text = exercise.detailName
        holder.statusIcon.setImageResource(R.drawable.ic_unlock) // Mặc định hiển thị mở khóa
        holder.statusIcon.setColorFilter(null)


    }


    override fun getItemCount() = exercises.size
}
