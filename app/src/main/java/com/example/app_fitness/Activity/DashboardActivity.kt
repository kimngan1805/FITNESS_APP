package com.example.app_fitness.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.app_fitness.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nhận dữ liệu
        val exerciseName = intent.getStringExtra("exercise_name")
        val imageUrl = intent.getStringExtra("exercise_image_url")

        // Hiển thị tên bài tập
        binding.exerciseNameTextView.text = exerciseName

        // Hiển thị hình ảnh
        Glide.with(this)
            .load(imageUrl)
            .into(binding.workoutImageHeader)

        // Thêm sự kiện click cho nút back
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Gọi phương thức onBackPressed của Activity
        }
    }
}