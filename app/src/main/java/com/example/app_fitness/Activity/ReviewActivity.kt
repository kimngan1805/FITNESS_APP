package com.example.app_fitness.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_fitness.Adapter.ReviewAdapter
import com.example.app_fitness.Adapter.TodayPlanAdapter
import com.example.app_fitness.databinding.DemoBinding
import com.example.app_fitness.R // Import R
import com.example.app_fitness.RestApi.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class ReviewActivity : AppCompatActivity() {

    private lateinit var binding: DemoBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private var currentPosition = 0
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val fullname = sharedPref.getString("fullname", "Người dùng")

        userId = sharedPref.getInt("user_id", -1)  // Gán biến toàn cục

        if (userId == -1) {
            Log.e("User", "UserId not found in SharedPreferences")
            // Có thể xử lý điều hướng sang đăng nhập
        }


        val todayPlanRecyclerView = binding.todayPlanRecyclerView
        todayPlanRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.hellouser.text = "Chào, $fullname 👋"



        binding.discoverExercisesCard.setOnClickListener {
            startActivity(Intent(this, SuggestActivity::class.java))
        }
        bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnItemSelectedListener { item ->
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

        loadExercises()
        loadExercisesToday()  // Gọi hàm sử dụng biến userId toàn cục

    }
    private fun loadExercisesToday() {
        lifecycleScope.launch {
            try {
                val exercises = RetrofitClient.instance.getUserExercisesToday(userId)  // userId lấy từ SharedPreferences hoặc Intent
                val adapter = TodayPlanAdapter(exercises)
                binding.todayPlanRecyclerView.layoutManager = LinearLayoutManager(this@ReviewActivity)
                binding.todayPlanRecyclerView.adapter = adapter
            } catch (e: Exception) {
                Log.e("API", "Error fetching exercises today", e)
            }
        }
    }


    private fun loadExercises() {
        lifecycleScope.launch {
            try {
                val exercises = RetrofitClient.instance.getRandomExercises()
                val adapter = ReviewAdapter(exercises)
                binding.reviewRecyclerView.layoutManager =
                    LinearLayoutManager(this@ReviewActivity, LinearLayoutManager.HORIZONTAL, false)
                binding.reviewRecyclerView.adapter = adapter

                // Auto scroll
                val handler = Handler(Looper.getMainLooper())
                val runnable = object : Runnable {
                    override fun run() {
                        if (currentPosition == exercises.size) {
                            currentPosition = 0
                        }
                        binding.reviewRecyclerView.smoothScrollToPosition(currentPosition)
                        currentPosition++
                        handler.postDelayed(this, 2000) // 2 giây
                    }
                }
                handler.postDelayed(runnable, 2000)

                // Lưu handler và runnable nếu cần dừng lại trong onDestroy()
            } catch (e: Exception) {
                Log.e("API", "Error fetching exercises", e)
            }
        }
    }

}