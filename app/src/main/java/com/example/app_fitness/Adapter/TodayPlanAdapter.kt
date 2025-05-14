package com.example.app_fitness.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_fitness.Entity.TodayPlanItem
import com.example.app_fitness.R // Thay thế bằng package name của bạn

class TodayPlanAdapter(private val items: List<TodayPlanItem>) : RecyclerView.Adapter<TodayPlanAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.itemImageView)
        val titleTextView: TextView = itemView.findViewById(R.id.itemTitleTextView)
        val subtitleTextView: TextView = itemView.findViewById(R.id.itemSubtitleTextView)
        val progressBar: ProgressBar = itemView.findViewById(R.id.itemProgressBar)
        val progressTextView: TextView = itemView.findViewById(R.id.itemProgressTextView)
        val levelTextView: TextView = itemView.findViewById(R.id.itemLevelTextView)
        val levelBackgroundView: LinearLayout = itemView.findViewById(R.id.itemLevelBackground)    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_today_plan, parent, false) // Tạo file layout item_today_plan.xml
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.imageView.setImageResource(item.imageResId)
        holder.titleTextView.text = item.title
        holder.subtitleTextView.text = item.subtitle
        holder.progressBar.progress = item.progress
        holder.progressTextView.text = item.progressText
        holder.levelTextView.text = item.level
        holder.levelBackgroundView.setBackgroundResource(item.levelBackground)
    }

    override fun getItemCount() = items.size
}