package com.example.app_fitness.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_fitness.Adapter.CategoryAdapter
import com.example.app_fitness.Entity.CategoryRequest
import com.example.app_fitness.MainActivity
import com.example.app_fitness.R
import com.example.app_fitness.RestApi.RetrofitClient
import com.example.app_fitness.databinding.ActivityWorkoutPlanBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkoutPlanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorkoutPlanBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private var categoryList: List<CategoryRequest> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Set up RecyclerView
        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(this)
        categoryAdapter = CategoryAdapter(categoryList) { category ->
            // Handle item click
            val intent = Intent(this, WorkoutLevelActivity::class.java)

            startActivity(intent) // Chuyển đến Activity mà không cần truyền dữ liệu
        }
        binding.categoryRecyclerView.adapter = categoryAdapter

        // Set up Spinner for gender selection
        val genderOptions = resources.getStringArray(R.array.gender_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genderSpinner.adapter = adapter

        // Fetch categories for default gender ("Nữ")
        fetchCategories("Nữ")

        // Handle gender selection
        binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedGender = genderOptions[position]
                fetchCategories(selectedGender)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing if nothing is selected
            }
        }


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
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
                    startActivity(Intent(this, MainActivity::class.java))
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

    private fun fetchCategories(gender: String) {
        val apiService = RetrofitClient.instance
        val call = apiService.getCategories(gender) // Pass gender to the API

        call.enqueue(object : Callback<List<CategoryRequest>> {
            override fun onResponse(call: Call<List<CategoryRequest>>, response: Response<List<CategoryRequest>>) {
                if (response.isSuccessful) {
                    // Filter categories based on the gender selected
                    val categories = response.body() ?: emptyList()
                    categoryList = categories.filter { it.gender == gender } // Filter by gender
                    categoryAdapter = CategoryAdapter(categoryList) { category ->
                        val selectedGender = binding.genderSpinner.selectedItem.toString()
                        val intent = Intent(this@WorkoutPlanActivity, WorkoutLevelActivity::class.java)
                        intent.putExtra("gender", selectedGender)
                        startActivity(intent)
                    }
                    binding.categoryRecyclerView.adapter = categoryAdapter
                } else {
                    Toast.makeText(this@WorkoutPlanActivity, "Failed to load categories", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CategoryRequest>>, t: Throwable) {
                Toast.makeText(this@WorkoutPlanActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}