package com.example.app_fitness.Activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_fitness.R
import com.example.app_fitness.Response.UserDataResponse
import com.example.app_fitness.RestApi.RetrofitClient
import com.example.app_fitness.databinding.ActivityCreateMeasurementBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateMeasurementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateMeasurementBinding
    private var currentWeight = 47
    private var hasChanges = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMeasurementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        val initialFields = mapOf(
            "height" to binding.heightEditText.text.toString(),
            "result" to binding.resultEditText.text.toString(),
            "bodyFat" to binding.bodyFatEditText.text.toString(),
            "neck" to binding.neckEditText.text.toString(),
            "shouder" to binding.shouderEditText.text.toString(),
            "chest" to binding.chestEditText.text.toString(),
            "waist" to binding.waistEditText.text.toString()
        )

        val fields = listOf(
            binding.heightEditText,
            binding.resultEditText,
            binding.bodyFatEditText,
            binding.neckEditText,
            binding.shouderEditText,
            binding.chestEditText,
            binding.waistEditText
        )

        fields.forEachIndexed { index, editText ->
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val key = initialFields.keys.elementAt(index)
                    hasChanges = hasChanges || editText.text.toString() != initialFields[key]
                }
            }
        }

        binding.weightEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hasChanges = hasChanges || binding.weightEditText.text.toString().toIntOrNull() != currentWeight
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
            val date = binding.dateTextView.text.toString()
            val result = binding.resultEditText.text.toString()
            val weightInput = binding.weightEditText.text.toString()
            val weight = weightInput.toFloatOrNull() ?: currentWeight.toFloat()
            val height = binding.heightEditText.text.toString().toIntOrNull() ?: 0
            val bodyFat = binding.bodyFatEditText.text.toString().toIntOrNull() ?: 0
            val neck = binding.neckEditText.text.toString().toFloatOrNull() ?: 0f
            val shoulder = binding.shouderEditText.text.toString().toFloatOrNull() ?: 0f
            val chest = binding.chestEditText.text.toString().toFloatOrNull() ?: 0f
            val waist = binding.waistEditText.text.toString().toFloatOrNull() ?: 0f

            val debugMessage = """
                userId: $userId
                date: $date
                result: $result
                weight: $weight
                height: $height
                bodyFat: $bodyFat
                neck: $neck
                shoulder: $shoulder
                chest: $chest
                waist: $waist
            """.trimIndent()

            Log.d("DEBUG_MEASUREMENT", debugMessage)

            AlertDialog.Builder(this)
                .setTitle("Dữ liệu sẽ gửi")
                .setMessage(debugMessage)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()

                    if (userId == -1) {
                        Toast.makeText(this, "Lỗi: không tìm thấy userId", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    RetrofitClient.instance.saveMeasurement(
                        userId = userId,
                        date = date,
                        result = result.ifBlank { "Không rõ" },
                        weight = weight,
                        height = height,
                        bodyFat = bodyFat,
                        neck = neck,
                        shoulder = shoulder,
                        chest = chest,
                        waist = waist
                    ).enqueue(object : Callback<UserDataResponse> {
                        override fun onResponse(call: Call<UserDataResponse>, response: Response<UserDataResponse>) {
                            Log.d("API_RESPONSE", "raw: ${response.raw()}")
                            Log.d("API_BODY", "body: ${response.body()}")
                            Log.d("API_CODE", "code: ${response.code()}")

                            if (response.isSuccessful && response.body()?.success == true) {
                                Toast.makeText(this@CreateMeasurementActivity, "Lưu thành công", Toast.LENGTH_SHORT).show()
                                hasChanges = false
                                finish()
                            } else {
                                val errorMsg = response.errorBody()?.string()
                                Log.e("API", "errorBody: $errorMsg")
                                Log.e("API", "Lỗi: ${response.body()?.message ?: errorMsg}")
                                Toast.makeText(this@CreateMeasurementActivity, "Lưu thất bại: ${response.body()?.message ?: "Lỗi không xác định"}", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<UserDataResponse>, t: Throwable) {
                            Log.e("API_FAILURE", "Lỗi mạng: ${t.message}")
                            Toast.makeText(this@CreateMeasurementActivity, "Lỗi mạng: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                .show()
        }

        binding.dateLayout.setOnClickListener {
            // TODO: Chọn ngày
        }

        binding.weightContainer.setOnClickListener {
            showWeightPickerDialog()
        }
        binding.dateLayout.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showQuitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Thoát khi chưa lưu?")
            .setMessage("Bạn có chắc chắn muốn thoát mà không lưu?")
            .setPositiveButton("Thoát") { dialog, _ ->
                finish()
                dialog.dismiss()
            }
            .setNegativeButton("Tiếp tục chỉnh sửa", null)
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
            hasChanges = true
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
            hasChanges = true
        }

        dialog.show()
    }

    private fun showDatePickerDialog() {
        // Lấy ngày hiện tại làm mặc định
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Khởi tạo DatePickerDialog
        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDayOfMonth ->
            // Chuyển đổi ngày từ "DD/MM/YYYY" sang "YYYY-MM-DD"
            val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear" // Định dạng ngày
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            try {
                val date = inputFormat.parse(selectedDate)
                val formattedDate = outputFormat.format(date)

                // Cập nhật TextView với ngày đã được chuyển đổi
                binding.dateTextView.text = formattedDate
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, year, month, dayOfMonth)

        // Hiển thị DatePickerDialog
        datePickerDialog.show()
    }
}
