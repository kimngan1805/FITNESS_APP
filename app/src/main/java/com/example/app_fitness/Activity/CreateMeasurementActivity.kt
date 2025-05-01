package com.example.app_fitness.Activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.app_fitness.R
import com.example.app_fitness.databinding.ActivityCreateMeasurementBinding

class CreateMeasurementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateMeasurementBinding
    private var currentWeight = 47
    private var hasChanges = false // Biến theo dõi xem có thay đổi nào chưa
    private var initialHeight: String? = null
    private var initialResult: String? = null
    private var initialBodyFat: String? = null
    private var initialNeck: String? = null
    private var initialShouder: String? = null
    private var initialChest: String? = null
    private var initialWaist: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMeasurementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khởi tạo giá trị ban đầu
        binding.weightEditText.text.toString().toIntOrNull()?.let {
            currentWeight = it
        }
        initialHeight = binding.heightEditText.text.toString()
        initialResult = binding.resultEditText.text.toString()
        initialBodyFat = binding.bodyFatEditText.text.toString()
        initialNeck = binding.neckEditText.text.toString()
        initialShouder = binding.shouderEditText.text.toString()
        initialChest = binding.chestEditText.text.toString()
        initialWaist = binding.waistEditText.text.toString()

        // Lắng nghe thay đổi trong các trường nhập liệu để cập nhật hasChanges
        binding.weightEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hasChanges = hasChanges || binding.weightEditText.text.toString().toIntOrNull() != currentWeight
            }
        }
        binding.heightEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hasChanges = hasChanges || binding.heightEditText.text.toString() != initialHeight
            }
        }
        binding.resultEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hasChanges = hasChanges || binding.resultEditText.text.toString() != initialResult
            }
        }
        binding.bodyFatEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hasChanges = hasChanges || binding.bodyFatEditText.text.toString() != initialBodyFat
            }
        }
        binding.neckEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hasChanges = hasChanges || binding.neckEditText.text.toString() != initialNeck
            }
        }
        binding.shouderEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hasChanges = hasChanges || binding.shouderEditText.text.toString() != initialShouder
            }
        }
        binding.chestEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hasChanges = hasChanges || binding.chestEditText.text.toString() != initialChest
            }
        }
        binding.waistEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hasChanges = hasChanges || binding.waistEditText.text.toString() != initialWaist
            }
        }

        binding.closeButton.setOnClickListener {
            if (hasChanges) {
                showQuitConfirmationDialog()
            } else {
                finish()
            }
        }

        binding.saveButton.setOnClickListener {
            // Xử lý logic lưu dữ liệu đo lường
            val date = binding.dateTextView.text.toString()
            val result = binding.resultEditText.text.toString()
            val weight = currentWeight.toString()
            val height = binding.heightEditText.text.toString()
            val bodyFat = binding.bodyFatEditText.text.toString()
            val neck = binding.neckEditText.text.toString()
            val shouder = binding.shouderEditText.text.toString()
            val chest = binding.chestEditText.text.toString()
            val waist = binding.waistEditText.text.toString()
            // ... (lấy các giá trị khác) ...
            hasChanges = false // Đánh dấu là đã lưu
            finish()
        }

        binding.dateLayout.setOnClickListener {
            // Xử lý logic chọn ngày
        }

        // Lắng nghe sự kiện click trên dòng "Weight, kg" để hiển thị dialog tùy chỉnh
        binding.weightContainer.setOnClickListener {
            showWeightPickerDialog()
        }

        binding.heightLayout.setOnClickListener {
            // Xử lý logic focus vào ô nhập liệu chiều cao (nếu cần)
        }
    }

    private fun showQuitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Quit without saving?")
            .setMessage("Are you sure you want to quit without saving your changes?")
            .setPositiveButton("Quit") { dialog, _ ->
                finish()
                dialog.dismiss()
            }
            .setNegativeButton("Continue editing") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false) // Ngăn người dùng đóng dialog bằng cách chạm ra ngoài
            .show()
    }

    private fun showWeightPickerDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.weight_picker_dialog, null)

        val weightNumberPicker = dialogView.findViewById<NumberPicker>(R.id.weightNumberPicker)
        val weightHideButton = dialogView.findViewById<Button>(R.id.weightHideButton)
        val weightNextButton = dialogView.findViewById<Button>(R.id.weightNextButton)

        weightNumberPicker.minValue = 30
        weightNumberPicker.maxValue = 150
        weightNumberPicker.value = currentWeight

        weightNumberPicker.setOnValueChangedListener { _, _, newVal ->
            currentWeight = newVal
            hasChanges = true // Đánh dấu là có thay đổi khi chọn weight
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        weightHideButton.setOnClickListener {
            dialog.dismiss()
        }

        weightNextButton.setOnClickListener {
            binding.weightEditText.setText(currentWeight.toString())
            dialog.dismiss()
            hasChanges = true // Đánh dấu là có thay đổi sau khi chọn weight
        }

        dialog.show()
    }
}