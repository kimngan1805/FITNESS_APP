package com.example.app_fitness.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_fitness.Adapter.BestForYouAdapter
import com.example.app_fitness.Adapter.BestForYouItem
import com.example.app_fitness.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class SuggestActivity: AppCompatActivity() {
    private lateinit var recyclerViewBestForYou: RecyclerView
    private lateinit var bestForYouAdapter: BestForYouAdapter
    private val bestForYouItemList = mutableListOf<BestForYouItem>()
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggest)
        recyclerViewBestForYou = findViewById(R.id.recyclerViewBestForYou)
        // Thay LinearLayoutManager bằng GridLayoutManager với 2 cột
        recyclerViewBestForYou.layoutManager = GridLayoutManager(this, 2)

        // Thêm dữ liệu mẫu vào danh sách
        bestForYouItemList.add(BestForYouItem("Belly fat burner", "10 min", "Beginner", R.drawable.hinh_dangki))
        bestForYouItemList.add(BestForYouItem("Lose Fat", "10 min", "Beginner", R.drawable.hinh_dangnhap))
        bestForYouItemList.add(BestForYouItem("Plank", "5 min", "Expert", R.drawable.activity_hinh))
        bestForYouItemList.add(BestForYouItem("Build Whider", "30 min", "Intermediate", R.drawable.sports_nutrition_image))
        // Thêm các item khác tại đây

        bestForYouAdapter = BestForYouAdapter(bestForYouItemList)
        recyclerViewBestForYou.adapter = bestForYouAdapter

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
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


        val discoverExercisesCard = findViewById<CardView>(R.id.discoverExercisesCard)
        discoverExercisesCard.setOnClickListener {
            val intent = Intent(this, WorkoutPlanActivity::class.java)
            startActivity(intent)
        }

    }

}