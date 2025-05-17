package com.example.app_fitness.Activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_fitness.R
import com.example.app_fitness.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        loadProfileData()
    }

    private fun setupToolbar() {
        binding.toolbar.findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish() // hoặc điều hướng khác tùy app
        }

        binding.toolbar.findViewById<Button>(R.id.editProfileButton).setOnClickListener {
            val intent = Intent(this, ActivityEditProfile::class.java)
            startActivity(intent)
        }
    }

    private fun loadProfileData() {
        // Lấy thông tin từ SharedPreferences
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val fullname = sharedPref.getString("fullname", "Chưa có tên")
        val age = sharedPref.getInt("age", -1)
        val userId = sharedPref.getInt("user_id", -1)

        // Gán tên ra giao diện
        binding.nameValue.text = fullname ?: "Không rõ"
        binding.emailValue.text = "ngan@example.com"
        binding.mobileValue.text = "+84 987 654 321"
        binding.genderValue.text = "Nữ"
        binding.dobValue.text = "17-05-2001"
        binding.medicalValue.text = "Hồ Chí Minh"
        binding.improvementValue.text = "Việt Nam"
        binding.heightValue.text = "160"
        binding.heightUnit.text = "CM"
        binding.weightValue.text = "50"
        binding.weightUnit.text = "KG"

        // Nếu có ảnh đại diện
        binding.profileImageView.setImageResource(R.drawable.ic_user_profile) // hoặc load từ URL bằng Glide
    }
}
