package com.example.app_fitness.Activity

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.app_fitness.databinding.ActivityCreateMeasurementBinding

class CreateMeasurementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateMeasurementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMeasurementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.closeButton.setOnClickListener {
            finish()
        }

        binding.saveButton.setOnClickListener {
            // Xử lý logic lưu dữ liệu đo lường
            val date = binding.dateTextView.text.toString()
            val result = binding.resultEditText.text.toString()
            val weight = binding.weightEditText.text.toString()
            val height = binding.heightEditText.text.toString()
            // ... (lấy các giá trị khác) ...
            // Thực hiện lưu dữ liệu
            finish()
        }

        binding.dateLayout.setOnClickListener {
            // Xử lý logic chọn ngày
        }

        // Lắng nghe sự kiện click trên dòng "Weight, kg" để focus vào EditText
        binding.weightContainer.setOnClickListener {
            binding.weightEditText.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.weightEditText, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.heightLayout.setOnClickListener {
            binding.heightEditText.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.heightEditText, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}