package com.example.app_fitness.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.app_fitness.R
import com.example.app_fitness.databinding.ActivityHandbookBinding

class HandbookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHandbookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHandbookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.selectedItemId = R.id.menu_handbook


        binding.sportsNutritionItem.setOnClickListener {
            val intent = Intent(this, ProteinActivity::class.java)
            startActivity(intent)
        }


        binding.ingredientsCalorieItem.setOnClickListener {
            // Xử lý khi click vào mục List of ingredients and calorie count
        }

        binding.encyclopediaItem.setOnClickListener {
            // Xử lý khi click vào mục Encyclopedia
        }

        // Thiết lập Bottom Navigation (nếu bạn muốn xử lý sự kiện chuyển trang)
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_workouts -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    // Đang ở trang này rồi, không làm gì
                    true
                }
                R.id.menu_feed -> {
                    startActivity(Intent(this, FeedActivity::class.java))
                    true
                }
                R.id.menu_messages -> {
                    startActivity(Intent(this, MessageActivity::class.java))
                    true
                }
                R.id.menu_handbook -> {
                    startActivity(Intent(this, HandbookActivity::class.java))
                    true
                }
                R.id.menu_more -> {
                    startActivity(Intent(this, MoreActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}