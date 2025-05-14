package com.example.app_fitness.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_fitness.Response.LoginResponse
import com.example.app_fitness.RestApi.RetrofitClient
import com.example.app_fitness.databinding.ActivitySignInBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sử dụng ViewBinding để liên kết với layout activity_sign_in.xml
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Xử lý sự kiện khi người dùng nhấn nút Đăng nhập
        binding.buttonDangnhap.setOnClickListener {
            val username = binding.username.text.toString().trim()
            val password = binding.password.text.toString().trim()

            // Kiểm tra nếu người dùng chưa nhập tên đăng nhập hoặc mật khẩu
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@SignInActivity, "Vui lòng nhập tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show()
            } else {
                signIn(username, password)
            }
        }
        // Xử lý sự kiện khi nhấn nút Đăng ký từ LinearLayout đầu tiên
        binding.buttonSignup.setOnClickListener {
            // Điều hướng đến SignUpActivity
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Xử lý sự kiện khi nhấn vào TextView "Nếu bạn chưa có tài khoản? Đăng kí ngay"
        binding.textsign.setOnClickListener {
            // Điều hướng đến SignUpActivity
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(username: String, password: String) {
        // Sử dụng RetrofitClient để lấy instance của ApiService
        val apiService = RetrofitClient.instance
        // Gọi API đăng nhập
        val call = apiService.signIn(username, password)

        // Thực hiện cuộc gọi API
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.success) {
                        Log.d("SignInActivity", "Response: ${response.body()}")
                        Toast.makeText(this@SignInActivity, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()

                        // Lấy fullname và age từ response
                        val fullname = loginResponse.fullname
                        val age = loginResponse.age
                        val userId = loginResponse.user_id
                        Log.d("DEBUG_LOGIN", "user_id từ API: $userId")

                        // Chuyển tới màn hình chính hoặc màn hình khác sau khi đăng nhập thành công
                        val intent = Intent(this@SignInActivity, WorkoutPlanActivity::class.java)

                        intent.putExtra("fullname", fullname)  // Truyền dữ liệu fullname
                        intent.putExtra("age", age)  // Truyền dữ liệu age
                        // Ngay sau khi đăng nhập thành công:
                        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
                        with(sharedPref.edit()) {// lưu vào đem qua trang bên kia
                            putString("fullname", fullname)
                            putInt("user_id", userId ?: -1)
                            putInt("age", age ?: -1)
                            apply()
                        }

                        Log.d("Activity", "Fullname: $fullname, Age: $age,user_id:$userId")
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@SignInActivity, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SignInActivity, "Lỗi kết nối đến máy chủ", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@SignInActivity, "Lỗi mạng: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
