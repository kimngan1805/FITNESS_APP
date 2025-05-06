package com.example.app_fitness.Activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_fitness.R
import com.example.app_fitness.Adapter.CommunityAdapter // Import UserAdapter
import com.example.app_fitness.Entity.Community // Import User
import com.example.app_fitness.databinding.ActivityCommunityBinding  // Import ActivityCommunityBinding

class CommunityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommunityBinding
    private lateinit var userAdapter: CommunityAdapter
    private val userList = ArrayList<Community>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khởi tạo và cấu hình RecyclerView
        val recyclerView: RecyclerView = binding.userList
        recyclerView.layoutManager = LinearLayoutManager(this)
        userAdapter = CommunityAdapter(this, userList) // Khởi tạo adapter
        recyclerView.adapter = userAdapter

        // Thêm dữ liệu người dùng (ví dụ)
        loadUsers()

        // Xử lý nút back
        val backButton: ImageView = binding.backButton
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadUsers() {
        // Tạo dữ liệu mẫu
        userList.add(Community("Yến Linh Châu", "25 years old, Can Tho, Thanh Pho Can Tho, Vietnam", R.drawable.ic_user))
        userList.add(Community("Vy Bông", "17 years old, Da Lat, Tinh Lam Dong, Vietnam", R.drawable.ic_user))
        userList.add(Community("Linh Huỳnh", "25 years old, Long An, Tinh Tien Giang, Vietnam", R.drawable.ic_user))
        userList.add(Community("Viet Ho", "35 years old, Hue, Tinh Thua Thien Hue, Vietnam", R.drawable.ic_user))
        userList.add(Community("Trần Khánh Đoan Lê", "22 years old, Hoc Mon, Ho Chi Minh City, Vietnam", R.drawable.ic_user))
        userList.add(Community("Chanh Trung", "41 years old, Ho Chi Minh City, Ho Chi Minh City, Vietnam", R.drawable.ic_user))
        userList.add(Community("Chương Lê", "29 years old, Ben Tre, Tinh Ben Tre, Vietnam", R.drawable.ic_user))

        userAdapter.notifyDataSetChanged() // Cập nhật RecyclerView
    }
}

