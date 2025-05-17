package com.example.app_fitness.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
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
        binding.toolbar.findViewById<Button>(R.id.editProfileButton).setOnClickListener {
            val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
            val fullname = sharedPref.getString("fullname", "")
            val age = sharedPref.getInt("age", -1)
            val userId = sharedPref.getInt("user_id", -1)

            val email = binding.emailValue.text.toString()
            val weight = binding.weightValue.text.toString()
            val height = binding.heightValue.text.toString()
            val gender = binding.genderValue.text.toString()
            val workout = binding.workoutValue.text.toString()
            val medical = binding.medicalValue.text.toString()
            val improvement = binding.improvementValue.text.toString()

            val intent = Intent(this, ActivityEditProfile::class.java).apply {
                putExtra("fullname", fullname)
                putExtra("email", email)
                putExtra("weight", weight)
                putExtra("height", height)
                putExtra("gender", gender)
                putExtra("workout", workout)
                putExtra("medical", medical)
                putExtra("improvement", improvement)
            }
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
                binding.workoutValue.text = userProfile.workoutLevel
                binding.medicalValue.text = userProfile.medicalCondition
                binding.improvementValue.text = userProfile.improvementGoal

            } catch (e: Exception) {
                Log.e("Profile", "Error: ${e.message}")
            }
        }

        // Nếu có ảnh đại diện
        binding.profileImageView.setImageResource(R.drawable.ic_user_profile) // hoặc load từ URL bằng Glide
    }
}
