package com.example.app_fitness.Activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.app_fitness.Adapter.HistoryAdapter
import com.example.app_fitness.Entity.ExerciseHistoryItem
import com.example.app_fitness.R
import com.example.app_fitness.RestApi.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : AppCompatActivity() {

    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private var userId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        userId = sharedPref.getInt("user_id", -1)

        historyRecyclerView = findViewById(R.id.historyRecyclerView)
        adapter = HistoryAdapter()
        historyRecyclerView.adapter = adapter

        if (userId != -1) {
            fetchHistory(userId)
        }
        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Quay lại màn trước đó
        }
    }

    private fun fetchHistory(userId: Int) {
        RetrofitClient.instance.getExerciseHistory(userId).enqueue(object :
            Callback<List<ExerciseHistoryItem>> {
            override fun onResponse(call: Call<List<ExerciseHistoryItem>>, response: Response<List<ExerciseHistoryItem>>) {
                if (response.isSuccessful) {
                    adapter.setItems(response.body() ?: emptyList())
                }
            }

            override fun onFailure(call: Call<List<ExerciseHistoryItem>>, t: Throwable) {
                Toast.makeText(this@HistoryActivity, "Lỗi kết nối", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
