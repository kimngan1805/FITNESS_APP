package com.example.app_fitness.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.app_fitness.Entity.AddedExercise
import com.example.app_fitness.Entity.ExerciseRequest
import com.example.app_fitness.Adapter.NextTrainingAdapter
import com.example.app_fitness.R
import com.example.app_fitness.Response.HasExercisesResponse
import com.example.app_fitness.RestApi.RetrofitClient
import com.example.app_fitness.databinding.ActivityDashboardBinding
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var nextTrainingAdapter: NextTrainingAdapter
    private val nextTrainingExercises = mutableListOf<ExerciseRequest>()
    private var completedExerciseIds: List<Int> = emptyList() // Danh sách ID bài tập đã hoàn thành
    private var userId: Int = -1
    private var nextExercise: ExerciseRequest? = null // Bài tập tiếp theo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        userId = sharedPref.getInt("user_id", -1)

        // Cấu hình RecyclerView cho "Next training"
        binding.nextTrainingRecyclerView.layoutManager = LinearLayoutManager(this)



        if (userId != -1) {
            loadLatestAddedExercise(userId)
            loadNextTrainingExercises() // Gọi hàm này để lấy danh sách bài tập
            checkAndLoadNextTraining(userId)
        } else {
            binding.exerciseNameTextView.text = "Chưa đăng nhập"
            binding.workoutImageHeader.setImageResource(R.drawable.activity_hinh)
            Toast.makeText(this, "Vui lòng đăng nhập để xem bài tập", Toast.LENGTH_SHORT).show()
            nextTrainingAdapter = NextTrainingAdapter(this, nextTrainingExercises, { exercise ->
                // Handle exercise click
                val intent = Intent(this, ExerciseVideoDetailActivity::class.java)
                intent.putExtra("exercise", exercise)
                startActivity(intent)
            }, emptyList())  // Truyền một danh sách trống nếu không có người dùng.
            binding.nextTrainingRecyclerView.adapter = nextTrainingAdapter
        }

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.menuButton.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.drawer_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.nav_workouts -> {
                        val intent = Intent(this@DashboardActivity, WorkoutsTabActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.nav_diet -> {
                        Toast.makeText(this@DashboardActivity, "Diet selected", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.nav_sports_nutrition -> {
                        Toast.makeText(this@DashboardActivity, "Sports nutri... selected", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.add_workout -> {
                        val intent = Intent(this@DashboardActivity, WorkoutPlanActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (userId != -1) {
            getCompletedExerciseIds() // Gọi lại để cập nhật danh sách đã hoàn thành khi Activity được resume
        }
    }

    private fun checkAndLoadNextTraining(userId: Int) {
        RetrofitClient.instance.checkUserHasExercises(userId).enqueue(object : Callback<HasExercisesResponse> {
            override fun onResponse(
                call: Call<HasExercisesResponse>,
                response: Response<HasExercisesResponse>
            ) {
                if (response.isSuccessful) {
                    val hasExercises = response.body()?.has_exercises ?: false
                    if (hasExercises) {
                        binding.nextTrainingTitleTextView.visibility = View.VISIBLE
                        binding.nextTrainingRecyclerView.visibility = View.VISIBLE
                        loadNextTrainingExercises()
                    } else {
                        binding.nextTrainingTitleTextView.visibility = View.GONE
                        binding.nextTrainingRecyclerView.visibility = View.GONE
                    }
                } else {
                    Toast.makeText(this@DashboardActivity, "Lỗi khi kiểm tra bài tập đã thêm", Toast.LENGTH_SHORT).show()
                    binding.nextTrainingTitleTextView.visibility = View.VISIBLE
                    binding.nextTrainingRecyclerView.visibility = View.VISIBLE
                    loadNextTrainingExercises()
                }
            }

            override fun onFailure(call: Call<HasExercisesResponse>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "Lỗi kết nối khi kiểm tra bài tập đã thêm", Toast.LENGTH_SHORT).show()
                binding.nextTrainingTitleTextView.visibility = View.VISIBLE
                binding.nextTrainingRecyclerView.visibility = View.VISIBLE
                loadNextTrainingExercises()
            }
        })
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


    private fun loadNextTrainingExercises() {
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val userLevel = sharedPref.getString("workout_level", null)
        val userGender = sharedPref.getString("gender", null)
        val userId = sharedPref.getInt("user_id", -1)

        val levelId = getLevelIdFromLevelString(userLevel)

        if (userId != -1) {
            RetrofitClient.instance.getExercises(
                levelId = levelId,
                gender = userGender,
                userId = userId
            ).enqueue(object : Callback<List<ExerciseRequest>> {
                override fun onResponse(
                    call: Call<List<ExerciseRequest>>,
                    response: Response<List<ExerciseRequest>>
                ) {
                    if (response.isSuccessful) {
                        val exercises = response.body() ?: emptyList()
                        nextTrainingExercises.clear()
                        nextTrainingExercises.addAll(exercises.sortedBy { it.unlock_order })

                        // Lấy danh sách bài tập đã hoàn thành và cập nhật adapter
                        getCompletedExerciseIds()
                    } else {
                        Toast.makeText(this@DashboardActivity, "Lỗi tải bài tập tiếp theo", Toast.LENGTH_SHORT).show()
                        Log.e("API Error", "Response Code: ${response.code()}, Message: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<ExerciseRequest>>, t: Throwable) {
                    Toast.makeText(this@DashboardActivity, "Lỗi kết nối khi tải bài tập tiếp theo", Toast.LENGTH_SHORT).show()
                    Log.e("Network Error", t.message ?: "Unknown error")
                }
            })
        }
    }

    private fun getLevelIdFromLevelString(level: String?): Int? {
        return when (level?.toLowerCase(Locale.ROOT)) {
            "beginner" -> 1
            "intermediate" -> 2
            "advanced" -> 3
            else -> null
        }
    }

    private fun getCompletedExerciseIds() {
        RetrofitClient.instance.getCompletedExercises(userId).enqueue(object : Callback<List<Int>> {
            override fun onResponse(call: Call<List<Int>>, response: Response<List<Int>>) {
                if (response.isSuccessful) {
                    completedExerciseIds = response.body() ?: emptyList()
                    // Khởi tạo adapter hoặc cập nhật nếu đã khởi tạo trước đó
                    if (::nextTrainingAdapter.isInitialized) {
                        nextTrainingAdapter.notifyDataSetChanged()
                    } else {
                        nextTrainingAdapter = NextTrainingAdapter(this@DashboardActivity, nextTrainingExercises, { exercise ->
                            val intent = Intent(this@DashboardActivity, ExerciseVideoDetailActivity::class.java)
                            intent.putExtra("exercise", exercise)
                            startActivity(intent)
                        }, completedExerciseIds)
                        binding.nextTrainingRecyclerView.adapter = nextTrainingAdapter
                    }
                    nextTrainingAdapter.completedExerciseIds = completedExerciseIds
                } else {
                    Toast.makeText(this@DashboardActivity, "Lỗi khi lấy danh sách bài tập đã hoàn thành", Toast.LENGTH_SHORT).show()
                    Log.e("API Error", "Response Code: ${response.code()}, Message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Int>>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "Lỗi kết nối khi lấy danh sách bài tập đã hoàn thành", Toast.LENGTH_SHORT).show()
                Log.e("Network Error", t.message ?: "Unknown error")
            }
        })
    }
}
