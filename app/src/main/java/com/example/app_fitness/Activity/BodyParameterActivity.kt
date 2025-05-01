package com.example.app_fitness.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_fitness.Entity.UserInfoRequest
import com.example.app_fitness.MainActivity
import com.example.app_fitness.R
import com.example.app_fitness.Response.UpdateUserResponse
import com.example.app_fitness.RestApi.RetrofitClient
import com.example.app_fitness.databinding.ActivityBodyParameterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BodyParameterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sử dụng View Binding để gắn layout
        val binding = ActivityBodyParameterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Đặt adapter cho Spinner với dữ liệu từ strings.xml
        val workoutLevels = resources.getStringArray(R.array.workout_levels)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, workoutLevels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.workoutLevelSpinner.adapter = adapter

        // Đặt OnItemSelectedListener cho Spinner để xử lý khi người dùng chọn một mục
        binding.workoutLevelSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                // Lấy giá trị người dùng đã chọn
                val selectedLevel = parentView?.getItemAtPosition(position).toString()

                // Hiển thị giá trị đã chọn trong TextView
                binding.selectedWorkoutLevelTextView.text = "Mức độ tập luyện: $selectedLevel"
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Nếu không có gì được chọn, có thể hiển thị "Chưa chọn"
                binding.selectedWorkoutLevelTextView.text = "Mức độ tập luyện: Chưa chọn"
            }
        }
// Xử lý khi nhấn vào nút Tiếp tục
        binding.continueButton.setOnClickListener {
            // Lấy user_id từ SharedPreferences

            val weight = binding.weightEditText.text.toString()
            val height = binding.heightEditText.text.toString()
            val age = binding.ageEditText.text.toString()
            val medicalCondition = binding.medicalConditionEditText.text.toString()
            val workoutLevel = binding.workoutLevelSpinner.selectedItem.toString() // Lấy mức độ tập luyện đã chọn
            val improvementGoal = binding.improvementEditText.selectedItem.toString()

            // Kiểm tra các trường nhập liệu không được để trống
            if (weight.isNotEmpty() && height.isNotEmpty() && age.isNotEmpty() && workoutLevel != "Chưa chọn" && improvementGoal.isNotEmpty()) {
                // Gọi API để gửi dữ liệu khảo sát
                val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
                val userId = sharedPref.getInt("user_id", -1) // -1 là giá trị mặc định nếu không tìm thấy user_id
                // Gửi yêu cầu API
                val apiService = RetrofitClient.instance
                val call = apiService.submitSurvey(
                    userId,
                    weight.toFloat(),
                    height.toInt(),
                    age.toInt(),
                    medicalCondition,
                    workoutLevel,
                    improvementGoal
                )

                // Enqueue API call
                call.enqueue(object : Callback<UpdateUserResponse> {
                    override fun onResponse(call: Call<UpdateUserResponse>, response: Response<UpdateUserResponse>) {
                        if (response.isSuccessful) {
                            val updateResponse = response.body()
                            Toast.makeText(this@BodyParameterActivity, updateResponse?.message, Toast.LENGTH_LONG).show()
                            // Chuyển đến MainActivity sau khi lưu thành công
                            val intent = Intent(this@BodyParameterActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish() // Kết thúc màn hình hiện tại (BodyParameterActivity)
                        } else {
                            Toast.makeText(this@BodyParameterActivity, "Có lỗi xảy ra khi lưu dữ liệu", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<UpdateUserResponse>, t: Throwable) {
                        Toast.makeText(this@BodyParameterActivity, "Kết nối thất bại: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
