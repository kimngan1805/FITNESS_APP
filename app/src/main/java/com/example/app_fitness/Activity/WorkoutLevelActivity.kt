package com.example.app_fitness.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_fitness.Adapter.WorkoutLevelAdapter
import com.example.app_fitness.Entity.WorkoutLevel
import com.example.app_fitness.MainActivity
import com.example.app_fitness.R
import com.example.app_fitness.RestApi.RetrofitClient
import com.example.app_fitness.databinding.ActivityWorkoutLevelBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkoutLevelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorkoutLevelBinding
    private lateinit var workoutLevelAdapter: WorkoutLevelAdapter
    private var workoutLevels: List<WorkoutLevel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutLevelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gender = (intent.getStringExtra("gender") ?: "Nữ").lowercase()
        Log.d("WorkoutLevelActivity", "Gender received: $gender")

        // Setup RecyclerView
        binding.workoutRecyclerView.layoutManager = LinearLayoutManager(this)

        // Gọi API lấy danh sách level theo gender
        fetchWorkoutLevels(gender)

        // Nút back
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        val bottomNavigationView = binding.bottomNavigationView

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_workouts -> {
                    // Đang ở trang này rồi
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

    private fun fetchWorkoutLevels(gender: String) {
        val apiService = RetrofitClient.instance
        val call = apiService.getLevels(gender)

        call.enqueue(object : Callback<List<WorkoutLevel>> {
            override fun onResponse(
                call: Call<List<WorkoutLevel>>,
                response: Response<List<WorkoutLevel>>
            ) {
                if (response.isSuccessful) {
                    val levels = response.body() ?: emptyList()
                    Log.d("WorkoutLevelActivity", "Levels response: $levels")

                    // Kiểm tra log để đảm bảo dữ liệu level_id
                    levels.forEach {
                        Log.d("WorkoutLevelActivity", "Level: ${it.level_name}, ID: ${it.id}")
                    }

                    workoutLevelAdapter = WorkoutLevelAdapter(levels) { selectedLevel ->
                        Log.d("WorkoutLevelActivity", "Selected level_id: ${selectedLevel.id}, gender: $gender")

                        // Kiểm tra trước khi gửi Intent
                        if (selectedLevel.id != 0) {
                            val intent = Intent(this@WorkoutLevelActivity, ExerciseActivity::class.java)
                            intent.putExtra("level_id", selectedLevel.id)
                            intent.putExtra("gender", gender)
                            startActivity(intent)
                        } else {
                            Log.e("WorkoutLevelActivity", "Error: Invalid level_id: ${selectedLevel.id}")
                            Toast.makeText(this@WorkoutLevelActivity, "Invalid level selected", Toast.LENGTH_SHORT).show()
                        }
                    }

                    binding.workoutRecyclerView.adapter = workoutLevelAdapter
                } else {
                    Log.e("WorkoutLevelActivity", "Response not successful: ${response.code()}")
                    Toast.makeText(this@WorkoutLevelActivity, "Error fetching levels", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<WorkoutLevel>>, t: Throwable) {
                Toast.makeText(this@WorkoutLevelActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}
