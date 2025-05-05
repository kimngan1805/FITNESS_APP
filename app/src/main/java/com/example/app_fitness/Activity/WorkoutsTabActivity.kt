package com.example.app_fitness.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.app_fitness.Adapter.NextTrainingAdapter
import com.example.app_fitness.Entity.AddedExercise
import com.example.app_fitness.Entity.ExerciseRequest
import com.example.app_fitness.R
import com.example.app_fitness.RestApi.RetrofitClient
import com.example.app_fitness.databinding.ActivityDashboardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkoutsTabActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityDashboardBinding
//    private lateinit var nextTrainingAdapter: NextTrainingAdapter
//    private val nextTrainingExercises = mutableListOf<ExerciseRequest>()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityDashboardBinding.inflate(layoutInflater)
//        setContentView(binding.root) // Sử dụng layout của DashboardActivity
//
//        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
//        val userId = sharedPref.getInt("user_id", -1)
//
//        // Ẩn nút menu (vì đây là trang Workouts chuyên biệt)
//        binding.menuButton.visibility = View.GONE
//
//        // Thay đổi text của back button (nếu cần) hoặc giữ nguyên chức năng back
//        binding.backButton.setOnClickListener {
//            onBackPressedDispatcher.onBackPressed()
//        }
//
//        // Cấu hình RecyclerView cho "Next training"
//        binding.nextTrainingRecyclerView.layoutManager = LinearLayoutManager(this)
//        nextTrainingAdapter = NextTrainingAdapter(this, nextTrainingExercises) { exercise ->
//            val intent = Intent(this, ExerciseVideoDetailActivity::class.java)
//            intent.putExtra("exercise", exercise)
//            startActivity(intent)
//        }
//        binding.nextTrainingRecyclerView.adapter = nextTrainingAdapter
//
//        if (userId != -1) {
//            loadLatestAddedExercise(userId)
//            loadNextTrainingExercises()
//        } else {
//            binding.exerciseNameTextView.text = "Chưa đăng nhập"
//            binding.workoutImageHeader.setImageResource(R.drawable.activity_hinh)
//            Toast.makeText(this, "Vui lòng đăng nhập để xem bài tập", Toast.LENGTH_SHORT).show()
//        }
//
//
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//        val completedExercises = getCompletedExerciseIds()
//        nextTrainingAdapter.updateCompletedExercises(completedExercises)
//    }
//
//    private fun loadLatestAddedExercise(userId: Int) {
//        RetrofitClient.instance.getUserExercises(userId).enqueue(object : Callback<List<AddedExercise>> {
//            override fun onResponse(
//                call: Call<List<AddedExercise>>,
//                response: Response<List<AddedExercise>>
//            ) {
//                if (response.isSuccessful) {
//                    val addedExercises = response.body() ?: emptyList()
//                    if (addedExercises.isNotEmpty()) {
//                        val latestExerciseId = addedExercises.first().exercise_id
//                        loadExerciseDetails(latestExerciseId)
//                    } else {
//                        binding.exerciseNameTextView.text = "Chưa có bài tập nào được thêm"
//                        binding.workoutImageHeader.setImageResource(R.drawable.activity_hinh)
//                    }
//                } else {
//                    Toast.makeText(this@WorkoutsTabActivity, "Lỗi khi tải danh sách bài tập đã thêm", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<List<AddedExercise>>, t: Throwable) {
//                Toast.makeText(this@WorkoutsTabActivity, "Lỗi kết nối khi tải danh sách bài tập", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    private fun loadExerciseDetails(exerciseId: Int) {
//        RetrofitClient.instance.getExerciseById(exerciseId).enqueue(object : Callback<ExerciseRequest> {
//            override fun onResponse(
//                call: Call<ExerciseRequest>,
//                response: Response<ExerciseRequest>
//            ) {
//                if (response.isSuccessful) {
//                    val exercise = response.body()
//                    exercise?.let {
//                        binding.exerciseNameTextView.text = it.exercise_name
//                        Glide.with(this@WorkoutsTabActivity)
//                            .load(it.image_url)
//                            .into(binding.workoutImageHeader)
//                    }
//                } else {
//                    Toast.makeText(this@WorkoutsTabActivity, "Lỗi tải chi tiết bài tập", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<ExerciseRequest>, t: Throwable) {
//                Toast.makeText(this@WorkoutsTabActivity, "Lỗi kết nối khi tải chi tiết", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    private fun loadNextTrainingExercises() {
//        RetrofitClient.instance.getExercises(null, null).enqueue(object : Callback<List<ExerciseRequest>> {
//            override fun onResponse(
//                call: Call<List<ExerciseRequest>>,
//                response: Response<List<ExerciseRequest>>
//            ) {
//                if (response.isSuccessful) {
//                    val exercises = response.body() ?: emptyList()
//                    val sortedExercises = exercises.sortedBy { it.unlock_order }
//                    nextTrainingExercises.clear()
//                    nextTrainingExercises.addAll(sortedExercises)
//                    nextTrainingAdapter.notifyDataSetChanged()
//                    val completedExercises = getCompletedExerciseIds()
//                    nextTrainingAdapter.updateCompletedExercises(completedExercises)
//                } else {
//                    Toast.makeText(this@WorkoutsTabActivity, "Lỗi tải bài tập tiếp theo", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<List<ExerciseRequest>>, t: Throwable) {
//                Toast.makeText(this@WorkoutsTabActivity, "Lỗi kết nối khi tải bài tập tiếp theo", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    private fun getCompletedExerciseIds(): Set<Int> {
//        val sharedPref = getSharedPreferences("CompletedExercises", MODE_PRIVATE)
//        return sharedPref.getStringSet("completed_ids", emptySet())?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
//    }
}