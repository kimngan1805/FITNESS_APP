package com.example.app_fitness.Activity

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_fitness.Adapter.CategoryAdapter
import com.example.app_fitness.Entity.CategoryRequest
import com.example.app_fitness.MainActivity
import com.example.app_fitness.R
import com.example.app_fitness.RestApi.RetrofitClient
import com.example.app_fitness.Utils.GridSpacingItemDecoration
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


        // xử lý recyclie view  hiển tji bài tập với nhấn vào 1 item qua level
        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(this)
        categoryAdapter = CategoryAdapter(categoryList) { category ->
            // Handle item click
            val intent = Intent(this, WorkoutLevelActivity::class.java)

            startActivity(intent) // Chuyển đến Activity mà không cần truyền dữ liệu
        }
        binding.categoryRecyclerView.adapter = categoryAdapter

        // xử lý phần dropdown cho giới tính
        val genderOptions = resources.getStringArray(R.array.gender_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genderSpinner.adapter = adapter

        // mặc định nữ
        fetchCategories("Nữ")

        //  xử lý load danh sách theo giới tính , gọi hàm fetch
        binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedGender = genderOptions[position]
                fetchCategories(selectedGender)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing if nothing is selected
            }
        }

// bottom menu (kệ đi)
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

    // lấy danh sách theo fiowis tính từ csdl bùm vào đây
    private fun fetchCategories(gender: String) {
        val apiService = RetrofitClient.instance
        val call = apiService.getCategories(gender)
        binding.categoryRecyclerView.layoutManager = GridLayoutManager(this, 2)
        val spacingInDp = 8
        val spacingInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            spacingInDp.toFloat(),
            resources.displayMetrics
        ).toInt()
        binding.categoryRecyclerView.addItemDecoration(
            GridSpacingItemDecoration(2, spacingInPx, true)
        )
        call.enqueue(object : Callback<List<CategoryRequest>> {

            override fun onResponse(call: Call<List<CategoryRequest>>, response: Response<List<CategoryRequest>>) {
                if (response.isSuccessful) {
                    val categories = response.body() ?: emptyList()
                    categoryList = categories.filter { it.gender == gender } // Filter by gender
                    categoryAdapter = CategoryAdapter(categoryList) { category ->
                        val selectedGender = binding.genderSpinner.selectedItem.toString()
                        // click vào 1 plan tương ứng qua trang level
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