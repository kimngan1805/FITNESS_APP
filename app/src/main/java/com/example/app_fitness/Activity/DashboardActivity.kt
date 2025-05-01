package com.example.app_fitness.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.app_fitness.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
