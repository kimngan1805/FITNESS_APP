package com.example.app_fitness.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.app_fitness.Entity.ExerciseRequest
import com.example.app_fitness.R
import com.example.app_fitness.RestApi.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExerciseDetailActivity : AppCompatActivity() {

    private lateinit var workoutImage: ImageView
    private lateinit var workoutTitle: TextView
    private lateinit var workoutDescription: TextView
    private lateinit var workoutNote: TextView
    private lateinit var workoutDay: TextView
    private lateinit var backButton: ImageButton
    private var currentExerciseId: Int = -1 // Biến để lưu ID bài tập

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_detail)
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        // Ánh xạ view
        workoutImage = findViewById(R.id.workoutImage)
        workoutTitle = findViewById(R.id.workoutTitle)
        workoutDescription = findViewById(R.id.supplements)
        workoutNote = findViewById(R.id.nutritionGuide)
        workoutDay = findViewById(R.id.workoutDays)
        backButton = findViewById(R.id.backButton)

        // Lấy dữ liệu từ Intent
        val exerciseName = intent.getStringExtra("exercise_name")
        val imageUrl = intent.getStringExtra("exercise_image_url")
        val levelId = intent.getIntExtra("exercise_level_id", 0)
        val gender = intent.getStringExtra("gender") ?: ""

        // Gán tạm ảnh và tên
        workoutTitle.text = exerciseName
        Glide.with(this).load(imageUrl).into(workoutImage)

        // Gọi lại API để lấy chi tiết (dựa vào level và gender)
        RetrofitClient.instance.getExercises(levelId, gender).enqueue(object : Callback<List<ExerciseRequest>> {
            override fun onResponse(
                call: Call<List<ExerciseRequest>>,
                response: Response<List<ExerciseRequest>>
            ) {
                if (response.isSuccessful) {
                    val list = response.body()
                    val matchedExercise = list?.find { it.exercise_name == exerciseName }

                    matchedExercise?.let {
                        workoutDescription.text = "Mô tả: ${it.description}"
                        workoutNote.text = "Ghi chú: ${it.note}"
                        workoutDay.text = "Ngày: ${it.day}"
                        currentExerciseId = it.id // Lấy ID bài tập
                    }
                }
            }

            override fun onFailure(call: Call<List<ExerciseRequest>>, t: Throwable) {
                Toast.makeText(this@ExerciseDetailActivity, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show()
            }
        })

        backButton.setOnClickListener {
            onBackPressed()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_workouts -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    true
                }
                R.id.menu_feed -> {
                    startActivity(Intent(this, WorkoutLevelActivity::class.java))
                    true
                }
                R.id.menu_messages -> {
                    startActivity(Intent(this, WorkoutLevelActivity::class.java))
                    true
                }
                R.id.menu_handbook -> {
                    startActivity(Intent(this, WorkoutLevelActivity::class.java))
                    true
                }
                R.id.menu_more -> {
                    startActivity(Intent(this, MoreActivity::class.java))
                    true
                }
                else -> false
            }
        }


        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            if (currentExerciseId != -1 && userId != -1) {
                Log.d("AddExercise", "Adding exercise with ID: $currentExerciseId and User ID: $userId") // Thêm dòng log này
                RetrofitClient.instance.addUserExercise(currentExerciseId, userId).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        Log.d("AddExercise", "Response code: ${response.code()}") // Log mã trạng thái HTTP
                        if (response.isSuccessful) {
                            Toast.makeText(this@ExerciseDetailActivity, "Đã thêm bài tập", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ExerciseDetailActivity, DashboardActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@ExerciseDetailActivity, "Lỗi khi thêm bài tập. Mã: ${response.code()}", Toast.LENGTH_SHORT).show()
                            Log.e("AddExercise", "Error adding exercise. Code: ${response.code()}, Body: ${response.errorBody()?.string()}") // Log lỗi chi tiết
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@ExerciseDetailActivity, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.e("AddExercise", "Connection error: ${t.message}") // Log lỗi kết nối
                    }
                })
            } else {
                Toast.makeText(this@ExerciseDetailActivity, "Không thể thêm bài tập. Thiếu thông tin.", Toast.LENGTH_SHORT).show()
                Log.w("AddExercise", "Missing exercise ID or user ID.") // Log cảnh báo thiếu ID
            }
        }
    }
}