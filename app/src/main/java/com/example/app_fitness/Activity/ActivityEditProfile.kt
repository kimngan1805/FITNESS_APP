package com.example.app_fitness.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.app_fitness.R
import com.example.app_fitness.databinding.ActivityEditProfileBinding
class ActivityEditProfile : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var originalData: Map<String, String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

    }

    private fun setupToolbar() {
        binding.toolbar.findViewById<ImageView>(R.id.backButton).setOnClickListener {
            if (hasChanges()) {
                showUnsavedChangesDialog()
            } else {
                finish()
            }
        }



        val intent = intent
        binding.nameValue.setText(intent.getStringExtra("fullname") ?: "")
        binding.emailValue.setText(intent.getStringExtra("email") ?: "")
        binding.genderValue.setText(intent.getStringExtra("gender") ?: "")
        binding.workoutValue.setText(intent.getStringExtra("workout") ?: "")
        binding.medicalValue.setText(intent.getStringExtra("medical") ?: "")
        binding.improvementValue.setText(intent.getStringExtra("improvement") ?: "")
        val weight = intent.getStringExtra("weight")?.replace("kg", "")?.trim() ?: ""
        val height = intent.getStringExtra("height")?.replace("cm", "")?.trim() ?: ""

        binding.weightValue.setText(weight)
        binding.heightValue.setText(height)

        originalData = mapOf(
            "name" to binding.nameValue.text.toString(),
            "email" to binding.emailValue.text.toString(),
            "gender" to binding.genderValue.text.toString(),
            "workout" to binding.workoutValue.text.toString(),
            "medical" to binding.medicalValue.text.toString(),
            "improvement" to binding.improvementValue.text.toString(),
            "weight" to binding.weightValue.text.toString(),
            "height" to binding.heightValue.text.toString()
        )




    }

    private fun hasChanges(): Boolean {
        val currentData = mapOf(
            "name" to binding.nameValue.text.toString(),
            "email" to binding.emailValue.text.toString(),
            "gender" to binding.genderValue.text.toString(),
            "workout" to binding.workoutValue.text.toString(),
            "medical" to binding.medicalValue.text.toString(),
            "improvement" to binding.improvementValue.text.toString(),
            "weight" to binding.weightValue.text.toString(),
            "height" to binding.heightValue.text.toString()
        )

        return currentData != originalData
    }

    private fun showUnsavedChangesDialog() {
        AlertDialog.Builder(this)
            .setTitle("Thoát mà chưa lưu?")
            .setMessage("Bạn có chắc chắn muốn thoát mà không lưu thay đổi?")
            .setPositiveButton("Thoát") { _, _ ->
                finish()
            }
            .setNegativeButton("Ở lại") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }



}
