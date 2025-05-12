package com.example.app_fitness.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.app_fitness.R
import com.example.app_fitness.databinding.ActivityMoreBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ví dụ xử lý sự kiện click vào "Log out"
        binding.apply {
            // binding là đối tượng chứa toàn bộ View trong activity_more.xml
            // Ví dụ xử lý logout
            val logoutRow = binding.root.findViewById<LinearLayout>(R.id.rowLogOut) // Correct ID to rowLogOut

            logoutRow.setOnClickListener {
                // Clear user session (e.g., SharedPreferences)
                val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.clear() // Clear all stored data
                editor.apply()

                // Redirect to SignInActivity
                val intent = Intent(this@MoreActivity, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear back stack
                startActivity(intent)
                finish() // Finish the current activity
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_workouts -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
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

        binding.root.findViewById<LinearLayout>(R.id.rowMyProfile).setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        binding.root.findViewById<LinearLayout>(R.id.rowMesuare).setOnClickListener {
            val intent = Intent(this, MesuareActivity::class.java)
            startActivity(intent)
        }

        binding.root.findViewById<LinearLayout>(R.id.rowMyAnalysis).setOnClickListener {
            val intent = Intent(this, AnalysisActivity::class.java)
            startActivity(intent)
        }

    }


}
