package com.example.app_fitness.Activity
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.app_fitness.R

class ProteinActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_protein_list)  // Đổi tên file XML nếu khác

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            // Khi nhấn nút back, đóng activity này, quay về màn hình trước
            finish()
        }
    }
}