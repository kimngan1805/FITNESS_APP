package com.example.app_fitness.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_fitness.MainActivity
import com.example.app_fitness.R
import com.example.app_fitness.Response.SignUpResponse
import com.example.app_fitness.RestApi.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    private lateinit var fullnameEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var signinText: TextView
    private lateinit var passwordStrengthTextView: TextView
    private lateinit var emailErrorTextView: TextView
    private lateinit var btnLogin: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Ánh xạ view
        fullnameEditText = findViewById(R.id.fullname)
        usernameEditText = findViewById(R.id.username)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        confirmPasswordEditText = findViewById(R.id.confirm_password)
        registerButton = findViewById(R.id.button_dangki)
        signinText = findViewById(R.id.textin)
        passwordStrengthTextView =
            findViewById(R.id.password_strength) // TextView hiển thị độ mạnh mật khẩu
        emailErrorTextView = findViewById(R.id.emailErrorTextView) // TextView hiển thị lỗi email
        btnLogin = findViewById(R.id.button_signin)
        // kiểm tra tính hợp lệ email
        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }
            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val email = emailEditText.text.toString().trim()
                if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailErrorTextView.text = "Vui lòng nhập đúng định dạng email"
                    emailErrorTextView.setTextColor(resources.getColor(android.R.color.holo_red_dark))
                } else {
                    emailErrorTextView.text = ""
                }
            }

            override fun afterTextChanged(editable: Editable?) {}
        })
        btnLogin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

       //kiểm tra độ mạnh mật khẩu
        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val password = passwordEditText.text.toString().trim()
                val passwordStrength = checkPasswordStrength(password)
                passwordStrengthTextView.text = passwordStrength.first
                passwordStrengthTextView.setTextColor(passwordStrength.second)
            }

            override fun afterTextChanged(editable: Editable?) {}
        })

        // Bắt sự kiện khi bấm nút đăng ký
        registerButton.setOnClickListener {
            val fullname = fullnameEditText.text.toString().trim()
            val username = usernameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            // Kiểm tra dữ liệu
            if (fullname.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailErrorTextView.text = "Vui lòng nhập đúng định dạng email"
                emailErrorTextView.setTextColor(resources.getColor(android.R.color.holo_red_dark))
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Gửi request đăng ký
            RetrofitClient.instance.registerUser(fullname, username, email, password)
                .enqueue(object : Callback<SignUpResponse> {
                    override fun onResponse(
                        call: Call<SignUpResponse>,
                        response: Response<SignUpResponse>
                    ) {
                        if (response.isSuccessful) {
                            val signUpResponse = response.body()
                            signUpResponse?.let {
                                if (it.success) {
                                    // Đăng ký thành công và có user_id
                                    val userId = it.user_id ?: -1

                                    if (userId != -1) {
                                        // Lưu user_id vào SharedPreferences
                                        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
                                        val editor = sharedPref.edit()
                                        editor.putInt("user_id", userId)
                                        editor.apply()
                                        Log.d("SignUpActivity", "user_id: ${it.user_id}") // Kiểm tra giá trị user_id

                                        Toast.makeText(
                                            this@SignUpActivity,
                                            "Đăng ký thành công: ${it.message}",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        // Chuyển đến màn hình chính
                                        val intent =
                                            Intent(this@SignUpActivity, BodyParameterActivity::class.java)
                                        startActivity(intent)
                                        finish() // Kết thúc màn hình hiện tại
                                    } else {
                                        Toast.makeText(
                                            this@SignUpActivity,
                                            "Lỗi lấy thông tin người dùng",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        this@SignUpActivity,
                                        "Đăng ký thất bại: ${it.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        } else {
                            Toast.makeText(
                                this@SignUpActivity,
                                "Đăng ký thất bại! Lỗi từ server.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Lỗi kết nối: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }

        // Hàm kiểm tra độ mạnh của mật khẩu
        private fun checkPasswordStrength(password: String): Pair<String, Int> {
            return when {
                password.length < 6 -> "Mật khẩu yếu" to resources.getColor(android.R.color.holo_red_dark)
                password.matches(Regex(".*\\d.*")) && password.matches(Regex(".*[a-zA-Z].*")) && !password.matches(Regex(".*[!@#$%^&*(),.?\":{}|<>].*")) -> "Mật khẩu vừa" to resources.getColor(android.R.color.holo_orange_dark)
                password.matches(Regex(".*\\d.*")) && password.matches(Regex(".*[a-zA-Z].*")) && password.matches(Regex(".*[!@#$%^&*(),.?\":{}|<>].*")) -> "Mật khẩu mạnh" to resources.getColor(android.R.color.holo_green_dark)
                else -> "Mật khẩu yếu" to resources.getColor(android.R.color.holo_red_dark)
            }
        }
}
