package com.example.app_fitness.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_fitness.Entity.ExerciseRequest
import com.example.app_fitness.Entity.TodayPlanItem
import com.example.app_fitness.R // Thay thế bằng package name của bạn

class TodayPlanAdapter(private val items: List<ExerciseRequest>) : RecyclerView.Adapter<TodayPlanAdapter.ViewHolder>() {

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
            .inflate(R.layout.item_today_plan, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Load ảnh từ url (dùng Glide)
        Glide.with(holder.imageView.context)
            .load(item.image_url)
            .placeholder(R.drawable.activity_hinh) // bạn thêm ảnh placeholder trong drawable
            .into(holder.imageView)

        holder.titleTextView.text = item.exercise_name
        holder.subtitleTextView.text = item.description

        // Ví dụ giả định progress tạm thời 50% cho tất cả (bạn tùy chỉnh theo dữ liệu nếu có)
        val progressValue = item.progress
        holder.progressBar.progress = progressValue
        holder.progressTextView.text = "$progressValue%"

        holder.levelTextView.text = item.level

        // Thay đổi màu background theo level
        when (item.level.lowercase()) {
            "cơ bản", "beginner" -> {
                holder.levelBackgroundView.setBackgroundColor(Color.parseColor("#FFA500")) // màu cam
            }
            "trung cấp", "intermediate" -> {
                holder.levelBackgroundView.setBackgroundColor(Color.parseColor("#008000")) // màu xanh lá
            }
            "nâng cao", "advanced" -> {
                holder.levelBackgroundView.setBackgroundColor(Color.parseColor("#FF0000")) // màu đỏ
            }
            else -> {
                holder.levelBackgroundView.setBackgroundColor(Color.GRAY) // màu mặc định
            }
        }
    }

    override fun getItemCount() = items.size
}