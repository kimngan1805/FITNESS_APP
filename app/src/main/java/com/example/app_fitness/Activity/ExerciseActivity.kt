package com.example.app_fitness.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_fitness.Adapter.ExerciseAdapter
import com.example.app_fitness.Entity.ExerciseRequest
import com.example.app_fitness.R
import com.example.app_fitness.RestApi.RetrofitClient
import com.example.app_fitness.databinding.ActivityWorkoutExceciseBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorkoutExceciseBinding
    private lateinit var exerciseAdapter: ExerciseAdapter
    private var exercises: List<ExerciseRequest> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutExceciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val levelId = intent.getIntExtra("level_id", -1)
        val gender = intent.getStringExtra("gender") ?: "null"

        Log.d("ExerciseActivity", "Received level_id: $levelId, gender: $gender")


        binding.recyclerViewExercise.layoutManager = LinearLayoutManager(this)

        fetchExercises(levelId, gender)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }


        val bottomNavigationView = binding.bottomNavigationView

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_workouts -> {
                    // Về lại WorkoutLevelActivity
                    startActivity(Intent(this, DashboardActivity::class.java))
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

    private fun fetchExercises(levelId: Int, gender: String) {
        val call = RetrofitClient.instance.getExercises(levelId, gender)
        call.enqueue(object : Callback<List<ExerciseRequest>> {
            override fun onResponse(call: Call<List<ExerciseRequest>>, response: Response<List<ExerciseRequest>>) {
                Log.d("API_RAW", "Response body: ${response.body()}")
                Log.d("API_RAW", "Response code: ${response.code()}")
                Log.d("API_RAW", "Error body: ${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        exercises = body
                        exerciseAdapter = ExerciseAdapter(exercises)
                        binding.recyclerViewExercise.adapter = exerciseAdapter
                    } else {
                        Toast.makeText(this@ExerciseActivity, "Body null (lỗi parse JSON)", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ExerciseActivity, "Lỗi HTTP: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<List<ExerciseRequest>>, t: Throwable) {
                Toast.makeText(this@ExerciseActivity, "Lỗi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
