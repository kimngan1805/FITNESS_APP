package com.example.app_fitness.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_fitness.Adapter.FeedAdapter
import com.example.app_fitness.Entity.FeedItem
import com.example.app_fitness.MainActivity
import com.example.app_fitness.R
import com.example.app_fitness.RestApi.ApiService
import com.example.app_fitness.RestApi.RetrofitClient
import com.example.app_fitness.databinding.ActivityFeedBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding
    private lateinit var feedAdapter: FeedAdapter
    private val feedItems = mutableListOf<FeedItem>()
    private lateinit var apiService: ApiService
    private var isAdmin = false // Biến để kiểm tra quyền admin
    private var currentUsername: String? = null // Thêm biến nà

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        binding.feedRecyclerView.adapter = FeedAdapter(feedItems, currentUsername)
        setContentView(binding.root)

        // Khởi tạo ApiService thông qua RetrofitClient
        apiService = RetrofitClient.instance

        // Kiểm tra vai trò người dùng (ví dụ, từ SharedPreferences)
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val userRole = sharedPref.getString("role", "user")
        currentUsername = sharedPref.getString("fullname", "Your Name") // Lấy fullname
            // "user" là giá trị mặc định nếu không tìm thấy
        isAdmin = userRole == "admin"

        // Hiển thị/ẩn nút đăng bài dựa trên vai trò
        val plusButton: ImageView = binding.header.findViewById(R.id.plus_button)
        plusButton.visibility = if (isAdmin) View.VISIBLE else View.GONE
        plusButton.setOnClickListener {
            // Xử lý sự kiện đăng bài (chuyển sang Activity đăng bài hoặc hiển thị dialog)
            // Ví dụ:
            // val intent = Intent(this, CreatePostActivity::class.java)
            // startActivity(intent)
        }

        // Thiết lập RecyclerView
        binding.feedRecyclerView.layoutManager = LinearLayoutManager(this)
        feedAdapter = FeedAdapter(feedItems, currentUsername)
        binding.feedRecyclerView.adapter = feedAdapter

        // Load dữ liệu từ API
        loadFeeds()

        // Thiết lập SwipeRefreshLayout
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadFeeds()
        }

        // Thiết lập BottomNavigationView
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            handleBottomNavigationSelection(item.itemId)
        }
    }

    private fun loadFeeds() {
        binding.swipeRefreshLayout.isRefreshing = true
        apiService.getFeeds().enqueue(object : Callback<List<FeedItem>> {
            override fun onResponse(call: Call<List<FeedItem>>, response: Response<List<FeedItem>>) {
                binding.swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {
                    val fetchedFeeds = response.body()
                    Log.d("FeedActivity", "Full API Response: ${response.body()}") // Thêm dòng này
                    if (fetchedFeeds != null) {
                        feedItems.clear()
                        feedItems.addAll(fetchedFeeds)
                        // Chuyển đổi định dạng ngày tháng nếu cần
                        feedItems.forEach { it.postTime = formatPostTime(it.postTime) }
                        feedAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e("FeedActivity", "Failed to load feeds: ${response.code()}")
                    // Xử lý lỗi (ví dụ: hiển thị Toast)
                }
            }

            override fun onFailure(call: Call<List<FeedItem>>, t: Throwable) {
                binding.swipeRefreshLayout.isRefreshing = false
                Log.e("FeedActivity", "Error loading feeds: ${t.message}")
                // Xử lý lỗi kết nối (ví dụ: hiển thị Toast)
            }
        })
    }

    private fun handleBottomNavigationSelection(itemId: Int): Boolean {
        when (itemId) {
            R.id.menu_workouts -> {
                startActivity(Intent(this, DashboardActivity::class.java))
                return true
            }
            R.id.menu_feed -> {
                startActivity(Intent(this, FeedActivity::class.java))
                return true
            }
            R.id.menu_messages -> {
                startActivity(Intent(this, MessageActivity::class.java))
                return true
            }
            R.id.menu_handbook -> {
                startActivity(Intent(this, HandbookActivity::class.java))
                return true
            }
            R.id.menu_more -> {
                startActivity(Intent(this, MoreActivity::class.java))
                return true
            }
            else -> return false
        }
    }

    private fun formatPostTime(postTime: String?): String { // Sửa ở đây
        // Định dạng ngày tháng đầu vào (ví dụ: từ MySQL)
        if (postTime == null) {
            return "" // Hoặc giá trị mặc định bạn muốn
        }
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        // Định dạng ngày tháng đầu ra (ví dụ: "Today 18:32", "Yesterday 10:15")
        val outputFormat = SimpleDateFormat("dd MMMM HH:mm", Locale.getDefault())

        return try {
            val date = inputFormat.parse(postTime) ?: Date()
            val now = Date()

            val diff = now.time - date.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            when {
                days > 1 -> outputFormat.format(date)
                days.toInt() == 1 -> "Yesterday " + SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
                hours > 0 -> "${hours.toInt()} hours ago"
                minutes > 0 -> "${minutes.toInt()} minutes ago"
                else -> "Just now"
            }
        } catch (e: Exception) {
            Log.e("FeedActivity", "Error formatting date: ${e.message}")
            postTime ?: ""// Trả về "" nếu postTime null
        }
    }
}