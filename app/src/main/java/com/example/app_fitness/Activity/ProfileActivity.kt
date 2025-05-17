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
import androidx.lifecycle.lifecycleScope
import com.example.app_fitness.R
import com.example.app_fitness.RestApi.RetrofitClient.instance
import com.example.app_fitness.databinding.ActivityProfileBinding
import kotlinx.coroutines.launch

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

        lifecycleScope.launch {
            try {
                val userProfile = instance.getUserProfile(userId)
                binding.nameValue.text= fullname
                // Cập nhật UI
                binding.emailValue.text = userProfile.email
                binding.weightValue.text = "${userProfile.weight} kg"
                binding.heightValue.text = "${userProfile.height} cm"
                binding.genderValue.text = userProfile.gender
                binding.workoutValue.text = userProfile.workout_level
                binding.medicalValue.text = userProfile.medical_condition
                binding.improvementValue.text = userProfile.improvement_goal

            } catch (e: Exception) {
                Log.e("Profile", "Error: ${e.message}")
            }
        }

        // Nếu có ảnh đại diện
        binding.profileImageView.setImageResource(R.drawable.ic_user_profile) // hoặc load từ URL bằng Glide
    }
}
