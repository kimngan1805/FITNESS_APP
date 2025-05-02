package com.example.app_fitness.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.app_fitness.Entity.AddedExercise
import com.example.app_fitness.Entity.ExerciseRequest
import com.example.app_fitness.R
import com.example.app_fitness.RestApi.RetrofitClient
import com.example.app_fitness.databinding.ActivityDashboardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId != -1) {
            loadLatestAddedExercise(userId)
        } else {
            binding.exerciseNameTextView.text = "Chưa đăng nhập"
            binding.workoutImageHeader.setImageResource(R.drawable.activity_hinh)
            Toast.makeText(this, "Vui lòng đăng nhập để xem bài tập", Toast.LENGTH_SHORT).show()
        }

        // Thêm sự kiện click cho nút back
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun loadLatestAddedExercise(userId: Int) {
        RetrofitClient.instance.getUserExercises(userId).enqueue(object : Callback<List<AddedExercise>> {
            override fun onResponse(
                call: Call<List<AddedExercise>>,
                response: Response<List<AddedExercise>>
            ) {
                if (response.isSuccessful) {
                    val addedExercises = response.body() ?: emptyList()
                    if (addedExercises.isNotEmpty()) {
                        // Lấy ID của bài tập mới nhất (ví dụ: bài đầu tiên trong danh sách)
                        val latestExerciseId = addedExercises.first().exercise_id
                        loadExerciseDetails(latestExerciseId)
                    } else {
                        binding.exerciseNameTextView.text = "Chưa có bài tập nào được thêm"
                        binding.workoutImageHeader.setImageResource(R.drawable.activity_hinh)
                    }
                } else {
                    Toast.makeText(this@DashboardActivity, "Lỗi khi tải danh sách bài tập đã thêm", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<AddedExercise>>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "Lỗi kết nối khi tải danh sách bài tập", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadExerciseDetails(exerciseId: Int) {
        RetrofitClient.instance.getExerciseById(exerciseId).enqueue(object : Callback<ExerciseRequest> {
            override fun onResponse(
                call: Call<ExerciseRequest>,
                response: Response<ExerciseRequest>
            ) {
                if (response.isSuccessful) {
                    val exercise = response.body()
                    exercise?.let {
                        binding.exerciseNameTextView.text = it.exercise_name
                        Glide.with(this@DashboardActivity)
                            .load(it.image_url)
                            .into(binding.workoutImageHeader)
                    }
                } else {
                    Toast.makeText(this@DashboardActivity, "Lỗi tải chi tiết bài tập", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ExerciseRequest>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "Lỗi kết nối khi tải chi tiết", Toast.LENGTH_SHORT).show()
            }
        })
    }
}