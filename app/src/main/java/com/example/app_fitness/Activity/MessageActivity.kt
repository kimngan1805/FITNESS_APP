package com.example.app_fitness.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import android.widget.Toast
import android.view.View
import com.example.app_fitness.R
import com.example.app_fitness.databinding.ActivityMessageBinding // Import binding class
import com.google.android.material.bottomnavigation.BottomNavigationView

class MessageActivity : AppCompatActivity() { // Đổi tên class thành MessageActivity

    private lateinit var binding: ActivityMessageBinding // Khai báo binding class với ActivityMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater) // Khởi tạo binding với ActivityMessageBinding
        setContentView(binding.root)  // Sử dụng binding.root

        // Sử dụng binding để truy cập các view
        val messagesTitle: TextView = binding.messagesTitle
        val emptyMessagesIcon: ImageView = binding.emptyMessagesIcon
        val emptyMessagesText: TextView = binding.emptyMessagesText
        val emptyMessagesDescription: TextView = binding.emptyMessagesDescription
        val addMessageButton: Button = binding.addMessageButton
        val backButton: ImageView = binding.backButton // Khởi tạo nút back

        // Thiết lập các thuộc tính (màu chữ, text, v.v.)
        messagesTitle.setTextColor(resources.getColor(R.color.black, theme))
        emptyMessagesText.setTextColor(resources.getColor(R.color.black, theme))
        emptyMessagesDescription.setTextColor(resources.getColor(R.color.black, theme))

        // Thiết lập text cho button
        addMessageButton.text = "+"
        addMessageButton.textSize = 24f

        // Xử lý sự kiện click cho button
        addMessageButton.setOnClickListener {
            Toast.makeText(this, "Chuyển đến màn hình soạn tin nhắn...", Toast.LENGTH_SHORT).show()
        }

        // Xử lý sự kiện click cho nút back
        backButton.setOnClickListener {
            onBackPressed() // Gọi phương thức onBackPressed() của Activity
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_workouts -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    // Đang ở trang này rồi, không làm gì
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
    }
}
