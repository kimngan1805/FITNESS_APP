package com.example.app_fitness.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_fitness.Entity.ExerciseHistoryItem
import com.example.app_fitness.R

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var items: List<ExerciseHistoryItem> = emptyList()

    fun setItems(data: List<ExerciseHistoryItem>) {
        items = data
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.exerciseImage)
        val name: TextView = itemView.findViewById(R.id.exerciseName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .placeholder(R.drawable.activity_hinh)
            .into(holder.image)
    }

    override fun getItemCount(): Int = items.size
}
