package com.example.app_fitness.Activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_fitness.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val fullname = sharedPref.getString("fullname", null)
        val age = sharedPref.getInt("age", -1)

        Log.d("ProfileActivity", "Fullname: $fullname, Age: $age")

        binding.nameTextView.text = fullname ?: "Không có tên"
        binding.ageTextView.text = if (age != -1) "$age tuổi" else "Không có tuổi"

        // Mặc định hiển thị tab Photos
        showPhotosTab()

        // Sự kiện nhấn nút "Photos"
        binding.photosTabButton.setOnClickListener {
            showPhotosTab()
        }

        // Sự kiện nhấn nút "Info"
        binding.infoTabButton.setOnClickListener {
            showInfoTab()
        }

        // Nút quay lại (back)
        binding.backButton.setOnClickListener {
            Log.d("Profile", "Back button clicked") // Thêm dòng log này
            finish() // Đặt breakpoint ở đây
        }

        // Nút chỉnh sửa (edit)
        binding.editButton.setOnClickListener {
            Log.d("Profile", "Edit button clicked") // Thêm dòng log này
            val intent = Intent(this, ActivityEditProfile::class.java)
            startActivity(intent) // Đặt breakpoint ở đây
        }


        // Sự kiện click icon camera
        binding.cameraIcon.setOnClickListener {
            showImagePickerDialog()
        }
    }

    private fun showPhotosTab() {
        binding.photosContainer.visibility = View.VISIBLE
        binding.infoContainer.visibility = View.GONE

        binding.photosTabButton.setTextColor(resources.getColor(android.R.color.white))
        binding.infoTabButton.setTextColor(resources.getColor(android.R.color.darker_gray))
    }

    private fun showInfoTab() {
        binding.photosContainer.visibility = View.GONE
        binding.infoContainer.visibility = View.VISIBLE

        binding.photosTabButton.setTextColor(resources.getColor(android.R.color.darker_gray))
        binding.infoTabButton.setTextColor(resources.getColor(android.R.color.white))
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Take Photo", "Upload Image")
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Choose an option")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_CAMERA)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CAMERA -> {
                    val bitmap = data?.extras?.get("data") as? Bitmap
                    bitmap?.let {
                        binding.avatarImageView.setImageBitmap(it)
                    }
                }
                REQUEST_GALLERY -> {
                    val imageUri: Uri? = data?.data
                    imageUri?.let {
                        binding.avatarImageView.setImageURI(it)
                    }
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CAMERA = 100
        private const val REQUEST_GALLERY = 101
    }
}
