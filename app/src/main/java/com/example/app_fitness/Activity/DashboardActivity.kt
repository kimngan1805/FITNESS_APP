package com.example.app_fitness.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.app_fitness.Entity.AddedExercise
import com.example.app_fitness.Entity.ExerciseRequest
import com.example.app_fitness.Adapter.NextTrainingAdapter
import com.example.app_fitness.Entity.ExerciseDetailRequest
import com.example.app_fitness.R
import com.example.app_fitness.RestApi.RetrofitClient
import com.example.app_fitness.databinding.ActivityDashboardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var nextTrainingAdapter: NextTrainingAdapter
    private val nextTrainingExercises = mutableListOf<ExerciseDetailRequest>()
    private var completedExerciseIds: List<Int> = emptyList() // Danh sách ID bài tập đã hoàn thành
    private var userId: Int = -1
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
// load completed trước,  sau đó  load bài tập
            loadCompletedExerciseIds {
                loadNextTrainingExercises()
            }
        } else {
            binding.exerciseNameTextView.text = "Chưa đăng nhập"
            binding.workoutImageHeader.setImageResource(R.drawable.activity_hinh)
            Toast.makeText(this, "Vui lòng đăng nhập để xem bài tập", Toast.LENGTH_SHORT).show()
            nextTrainingAdapter = NextTrainingAdapter(
                this,
                mutableListOf(), // danh sách trống
                emptyList()      // completedIds cũng trống
            ) { exercise ->
                val intent = Intent(this, ExerciseVideoDetailActivity::class.java)
                intent.putExtra("exercise", exercise)
                startActivity(intent)
            }
            binding.nextTrainingRecyclerView.adapter = nextTrainingAdapter

        }

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
// phần popup menu
        binding.menuButton.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.drawer_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.nav_workouts -> {
                        val intent = Intent(this@DashboardActivity, WorkoutPlanActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    R.id.nav_diet -> {
                        val intent = Intent(this@DashboardActivity, AnalysisActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.nav_sports_nutrition -> {
                        Toast.makeText(
                            this@DashboardActivity,
                            "Sports nutri... selected",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }

                    R.id.add_workout -> {
                        val intent = Intent(this@DashboardActivity, HistoryActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()
        }

// phần bottom navigation menu
        val bottomNav = binding.bottomNavigation
        bottomNav.selectedItemId = R.id.menu_workouts

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_workouts -> {
                    // Đang ở Dashboard -> không làm gì
                    true
                }
                R.id.menu_feed -> {
                    startActivity(Intent(this, FeedActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.menu_messages -> {
                    startActivity(Intent(this, MessageActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.menu_handbook -> {
                    startActivity(Intent(this, HandbookActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.menu_more -> {
                    startActivity(Intent(this, MoreActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }

    }
    // done bên video detail load lai trang
    override fun onResume() {
        super.onResume()
        if (userId != -1) {
            loadLatestAddedExercise(userId)
            loadCompletedExerciseIds {
                loadNextTrainingExercises()
            }
        }
    }

    // phần hiển thị tên với ảnh bài tập ở trên
    private fun loadLatestAddedExercise(userId: Int) {
        RetrofitClient.instance.getUserExercises(userId)
            .enqueue(object : Callback<List<AddedExercise>> {
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
                        Toast.makeText(
                            this@DashboardActivity,
                            "Lỗi khi tải danh sách bài tập đã thêm",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<AddedExercise>>, t: Throwable) {
                    Toast.makeText(
                        this@DashboardActivity,
                        "Lỗi kết nối khi tải danh sách bài tập",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    // tải bài tập theo exercise_id được thêm
    private fun loadExerciseDetails(exerciseId: Int) {
        RetrofitClient.instance.getExerciseById(exerciseId)
            .enqueue(object : Callback<ExerciseRequest> {
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
                        Toast.makeText(
                            this@DashboardActivity,
                            "Lỗi tải chi tiết bài tập",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ExerciseRequest>, t: Throwable) {
                    Toast.makeText(
                        this@DashboardActivity,
                        "Lỗi kết nối khi tải chi tiết",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    // kiểm tra lấy bài tập đã hoàn thành nè
    private fun loadCompletedExerciseIds(onLoaded: () -> Unit) {
        RetrofitClient.instance.getCompletedExercises(userId)
            .enqueue(object : Callback<List<Int>> {
                override fun onResponse(call: Call<List<Int>>, response: Response<List<Int>>) {
                    if (response.isSuccessful) {
                        completedExerciseIds = response.body() ?: emptyList()
                    }
                    onLoaded()
                }

                override fun onFailure(call: Call<List<Int>>, t: Throwable) {
                    completedExerciseIds = emptyList()
                    onLoaded()
                }
            })
    }
    // phần này để xử lý chỗ next training cho các bài tập thuôc exercise_id tương ứng
    private fun loadNextTrainingExercises() {
        RetrofitClient.instance.getUserExercises(userId)
            .enqueue(object : Callback<List<AddedExercise>> {
                override fun onResponse(
                    call: Call<List<AddedExercise>>,
                    response: Response<List<AddedExercise>>
                ) {
                    if (response.isSuccessful) {
                        val addedExercises = response.body() ?: emptyList()
                        if (addedExercises.isNotEmpty()) {
                            val latestExerciseId = addedExercises.first().exercise_id
                            // Gọi getNextTraining với exerciseId
                            RetrofitClient.instance.getNextTraining(userId = userId, exerciseId = latestExerciseId)
                                .enqueue(object : Callback<List<ExerciseDetailRequest>> {
                                    override fun onResponse(
                                        call: Call<List<ExerciseDetailRequest>>,
                                        response: Response<List<ExerciseDetailRequest>>
                                    ) {
                                        Log.d("NextTraining", "Dữ liệu trả về: ${response.body()}")
                                        Log.d("NextTraining", "Dữ liệu bài tập tiếp theo: ${response.body()}")

                                        if (response.isSuccessful) {
                                            val exercises = response.body() ?: emptyList()
                                            nextTrainingExercises.clear()
                                            nextTrainingExercises.addAll(exercises)

                                            nextTrainingAdapter = NextTrainingAdapter(
                                                this@DashboardActivity,
                                                nextTrainingExercises,
                                                completedExerciseIds
                                            ) { exercise ->
                                                val intent = Intent(
                                                    this@DashboardActivity,
                                                    ExerciseVideoDetailActivity::class.java
                                                )
                                                intent.putExtra("exercise", exercise)
                                                startActivity(intent)
                                            }
                                            binding.nextTrainingRecyclerView.adapter = nextTrainingAdapter

                                        } else {
                                            Toast.makeText(
                                                this@DashboardActivity,
                                                "Lỗi tải bài tập tiếp theo",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<List<ExerciseDetailRequest>>, t: Throwable) {
                                        Toast.makeText(
                                            this@DashboardActivity,
                                            "Lỗi kết nối khi tải bài tập tiếp theo",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                        } else {
                            // Xử lý trường hợp không có bài tập đã thêm
                            nextTrainingExercises.clear()
                            nextTrainingAdapter = NextTrainingAdapter(
                                this@DashboardActivity,
                                emptyList(),
                                completedExerciseIds
                            ) { /* ... */ }
                            binding.nextTrainingRecyclerView.adapter = nextTrainingAdapter
                        }
                    } else {
                        Toast.makeText(
                            this@DashboardActivity,
                            "Lỗi khi tải danh sách bài tập đã thêm",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<AddedExercise>>, t: Throwable) {
                    Toast.makeText(
                        this@DashboardActivity,
                        "Lỗi kết nối khi tải danh sách bài tập",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }


}
