package com.example.app_fitness.Adapter
import android.content.Context
import android.util.Log
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
    private val exercises: List<ExerciseDetailRequest>,
    private val completedIds: List<Int>,
    private val onExerciseClicked: (ExerciseDetailRequest) -> Unit
) : RecyclerView.Adapter<NextTrainingAdapter.NextTrainingViewHolder>() {

    private val firstLockedOrder: Int = run {
        val completedOrders = exercises.filter { it.detailId in completedIds }.map { it.unlockOrder }
        val maxCompletedOrder = completedOrders.maxOrNull() ?: 0
        maxCompletedOrder + 1
    }


    inner class NextTrainingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val statusIcon: ImageView = itemView.findViewById(R.id.statusIcon)
        val exerciseNameTextView: TextView = itemView.findViewById(R.id.exerciseNameTextView)

        init {
            itemView.setOnClickListener {
                Log.d("AdapterDebug", "Completed IDs: $completedIds")

                val exercise = exercises[adapterPosition]
                if (completedIds.contains(exercise.detailId) || exercise.unlockOrder == firstLockedOrder) {
                    onExerciseClicked(exercise)
                } else {
                    Toast.makeText(context, "Bài này chưa mở đâu nha 😅", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextTrainingViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_next_training, parent, false)
        return NextTrainingViewHolder(view)
    }

    override fun onBindViewHolder(holder: NextTrainingViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.exerciseNameTextView.text = exercise.detailName

        when {

            completedIds.contains(exercise.detailId) -> {
                holder.statusIcon.setImageResource(R.drawable.ic_check)
                holder.itemView.alpha = 1.0f
                holder.itemView.isEnabled = true
            }
            exercise.unlockOrder == firstLockedOrder -> {
                holder.statusIcon.setImageResource(R.drawable.ic_unlock)
                holder.itemView.alpha = 1.0f
                holder.itemView.isEnabled = true
            }
            else -> {
                holder.statusIcon.setImageResource(R.drawable.ic_lock)
                holder.itemView.alpha = 0.5f
                holder.itemView.isEnabled = false
            }
        }
    }


    override fun getItemCount(): Int = exercises.size
}
