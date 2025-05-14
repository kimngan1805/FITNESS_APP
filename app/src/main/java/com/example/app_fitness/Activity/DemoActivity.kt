package com.example.app_fitness.Activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.HorizontalScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_fitness.Adapter.TodayPlanAdapter
import com.example.app_fitness.Entity.TodayPlanItem
import com.example.app_fitness.databinding.DemoBinding
import com.example.app_fitness.R // Import R

class DemoActivity : AppCompatActivity() {

    private lateinit var binding: DemoBinding
    private lateinit var horizontalScrollView: HorizontalScrollView
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val todayPlanRecyclerView = binding.todayPlanRecyclerView
        todayPlanRecyclerView.layoutManager = LinearLayoutManager(this)

        horizontalScrollView = binding.horizontalScrollView

        val todayPlanItems = listOf(
            TodayPlanItem(
                "Push Up",
                "100 Push up a day",
                45,
                "45%",
                R.drawable.activity_hinh, // Thay thế bằng ID hình ảnh thực tế
                "Intermediate",
                R.drawable.level_tag_intermediate
            ),
            TodayPlanItem(
                "Sit Up",
                "20 Sit up a day",
                75,
                "75%",
                R.drawable.hinh_dangnhap, // Thay thế bằng ID hình ảnh thực tế
                "Beginner",
                R.drawable.level_tag_beginner
            ),
            TodayPlanItem(
                "Knee Push Up",
                "15 Knee push up a day",
                90,
                "90%",
                R.drawable.hinh_dangki, // Thay thế bằng ID hình ảnh thực tế
                "Beginner",
                R.drawable.level_tag_beginner
            )
            // Thêm các item khác cho "Today Plan"
        )

        val todayPlanAdapter = TodayPlanAdapter(todayPlanItems)
        todayPlanRecyclerView.adapter = todayPlanAdapter
    }

    private val scrollRunnable = object : Runnable {
        override fun run() {
            val currentX = horizontalScrollView.scrollX
            val childWidth = horizontalScrollView.getChildAt(0)?.width ?: 0
            val scrollViewWidth = horizontalScrollView.width

            if (childWidth > scrollViewWidth) {
                val nextScrollX = if (currentX + scrollViewWidth < childWidth) {
                    currentX + scrollViewWidth
                } else {
                    0 // Scroll back to the beginning
                }
                horizontalScrollView.smoothScrollTo(nextScrollX, 0)
                handler.postDelayed(this, 2000) // Scroll every 2 seconds
            }
        }
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(scrollRunnable, 2000) // Start auto scroll when activity resumes
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(scrollRunnable) // Stop auto scroll when activity pauses
    }
}