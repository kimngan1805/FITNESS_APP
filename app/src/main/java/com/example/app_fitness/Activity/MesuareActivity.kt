package com.example.app_fitness.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_fitness.R
import com.example.app_fitness.Response.UserDataResponse
import com.example.app_fitness.RestApi.RetrofitClient
import com.example.app_fitness.databinding.ActivityMesuareBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MesuareActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMesuareBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMesuareBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)
        if (userId != -1) {
            loadUserData(userId)
        } else {
            Toast.makeText(this, "Không tìm thấy user ID", Toast.LENGTH_SHORT).show()
        }
        // Thiết lập sự kiện click cho nút back
        binding.backButton.setOnClickListener {
            finish() // Đóng Activity hiện tại và quay lại Activity trước đó
        }

        // Thiết lập sự kiện click cho nút add (nếu cần xử lý)
        binding.addButton.setOnClickListener {
            // Xử lý sự kiện khi nút add được nhấn
            // Ví dụ: Mở một dialog hoặc Activity mới để thêm dữ liệu
        }

        // Thiết lập sự kiện click cho nút Statistics Tab
        binding.statisticsTabButton.setOnClickListener {
            // Xử lý sự kiện khi tab Statistics được chọn
            // Ví dụ: Thay đổi màu nền và màu chữ để hiển thị trạng thái chọn
            binding.statisticsTabButton.setBackgroundResource(R.drawable.rounded_border_black)
            binding.statisticsTabButton.setTextColor(resources.getColor(R.color.teal_200))
            binding.measurementsTabButton.setBackgroundResource(R.drawable.rounded_border_black)
            binding.measurementsTabButton.setTextColor(resources.getColor(android.R.color.black))
        }

        // Thiết lập sự kiện click cho nút Measurements Tab
        binding.measurementsTabButton.setOnClickListener {
            // Xử lý sự kiện khi tab Measurements được chọn
            // Ví dụ: Thay đổi màu nền và màu chữ để hiển thị trạng thái chọn
            binding.measurementsTabButton.setBackgroundResource(R.drawable.rounded_border_black)
            binding.measurementsTabButton.setTextColor(resources.getColor(R.color.teal_200))
            binding.statisticsTabButton.setBackgroundResource(R.drawable.rounded_border_black)
            binding.statisticsTabButton.setTextColor(resources.getColor(android.R.color.black))
        }

        // Thiết lập sự kiện click cho layout hiển thị cân nặng
        binding.weightLayout.setOnClickListener {
            // Xử lý sự kiện khi người dùng nhấn vào thông tin cân nặng
            // Ví dụ: Mở một màn hình chi tiết hoặc dialog để chỉnh sửa cân nặng
        }
        binding.addButton.setOnClickListener {
            val intent = Intent(this, CreateMeasurementActivity::class.java)
            startActivity(intent)
        }
        // Hiển thị dữ liệu ban đầu (ví dụ: ngày và cân nặng)
        binding.dateTextView.text = "8/4/2025" // Thay bằng dữ liệu thực tế của bạn
        binding.weightTextView.text = "47 kg"   // Thay bằng dữ liệu thực tế của bạn
    }
    private fun loadUserData(userId: Int) {
        val apiService = RetrofitClient.instance
        val call = apiService.getUserData(userId)

        call.enqueue(object : Callback<UserDataResponse> {
            override fun onResponse(call: Call<UserDataResponse>, response: Response<UserDataResponse>) {
                if (response.isSuccessful) {
                    val userData = response.body()
                    if (userData != null && userData.success) {
                        binding.weightTextView.text = "${userData.weight} kg"
                        binding.heightTextView.text = "${userData.height} cm"
                    }
                } else {
                    Toast.makeText(this@MesuareActivity, "Lỗi khi tải dữ liệu người dùng", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserDataResponse>, t: Throwable) {
                Toast.makeText(this@MesuareActivity, "Lỗi mạng: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}