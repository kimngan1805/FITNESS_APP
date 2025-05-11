package com.example.app_fitness.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_fitness.Adapter.DailyFoodListAdapter
import com.example.app_fitness.Adapter.FoodListAdapter
import com.example.app_fitness.R
import com.example.app_fitness.ViewModel.FoodEntryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AnalysisActivity : AppCompatActivity(), FoodListAdapter.OnItemLongClickListener {

    private lateinit var foodListRecyclerView: RecyclerView
    private lateinit var foodListAdapter: FoodListAdapter
    private val foodItems = mutableListOf<String>()
    private lateinit var addButton: Button
    private lateinit var foodNameInput: EditText
    private lateinit var quantityInput: EditText
    private lateinit var calculateCaloriesButton: Button
    private var userId: Int = -1
    private lateinit var foodEntryViewModel: FoodEntryViewModel

    private lateinit var dailyFoodListRecyclerView: RecyclerView
    private lateinit var dailyFoodListAdapter: DailyFoodListAdapter
    private lateinit var emptyDailyFoodMessage: TextView
    private lateinit var totalCaloriesTextView: TextView
    private lateinit var predictedCaloriesTextView: TextView// Thêm biến cho textview hiển thị lượng calo dự đoán
    private lateinit var refreshButton: ImageButton // Khai báo nút refresh

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis)

        // Khởi tạo các view hiện tại
        foodListRecyclerView = findViewById(R.id.food_list_recycler_view)
        addButton = findViewById(R.id.add_food_button)
        foodNameInput = findViewById(R.id.food_name_input)
        quantityInput = findViewById(R.id.quantity_input)
        calculateCaloriesButton = findViewById(R.id.calculate_calories_button)
        totalCaloriesTextView =
            findViewById(R.id.calories_consumed) // Ánh xạ TextView tổng calo
        predictedCaloriesTextView = findViewById(R.id.calories_predicted) //ánh xạ text view lượng calo dự đoán
        // Khởi tạo view cho danh sách hàng ngày
        dailyFoodListRecyclerView = findViewById(R.id.daily_food_list_recycler_view)
        emptyDailyFoodMessage = findViewById(R.id.empty_daily_food_message)
        refreshButton = findViewById(R.id.refresh_button) // Ánh xạ nút refresh từ layout
        // Lấy user_id từ SharedPreferences
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        userId = sharedPref.getInt("user_id", -1)

        if (userId == -1) {
            Toast.makeText(
                this,
                "Không tìm thấy thông tin người dùng. Vui lòng đăng nhập lại.",
                Toast.LENGTH_LONG
            ).show()
        }

        // Thiết lập LayoutManager và Adapter cho danh sách thêm mới
        foodListRecyclerView.layoutManager = LinearLayoutManager(this)
        foodListAdapter = FoodListAdapter(foodItems, this)
        foodListRecyclerView.adapter = foodListAdapter

        // Thiết lập LayoutManager và Adapter cho danh sách hàng ngày
        dailyFoodListRecyclerView.layoutManager = LinearLayoutManager(this)
        dailyFoodListAdapter =
            DailyFoodListAdapter(mutableListOf()) // Khởi tạo với MutableList rỗng
        dailyFoodListRecyclerView.adapter = dailyFoodListAdapter

        // Khởi tạo ViewModel
        foodEntryViewModel = ViewModelProvider(this).get(FoodEntryViewModel::class.java)

        // Quan sát LiveData message từ ViewModel
        foodEntryViewModel.message.observe(this) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        // Quan sát LiveData dailyCalories và cập nhật TextView
        foodEntryViewModel.dailyCalories.observe(this) { totalCalories ->
            totalCaloriesTextView.text = "${String.format("%.2f", totalCalories)} kcal"
        }

        foodEntryViewModel.dailyFoodList.observe(this) { dailyFoodList ->  // Quan sát danh sách món ăn hàng ngày
            if (dailyFoodList != null && dailyFoodList.isNotEmpty()) {
                dailyFoodListAdapter.updateList(dailyFoodList)
                dailyFoodListRecyclerView.visibility = RecyclerView.VISIBLE
                emptyDailyFoodMessage.visibility = TextView.GONE
            } else {
                dailyFoodListRecyclerView.visibility = RecyclerView.GONE
                emptyDailyFoodMessage.visibility = TextView.VISIBLE
            }
        }

        // Quan sát LiveData predictedCalories và cập nhật TextView
        foodEntryViewModel.predictedCalories.observe(this) { predictedCalories ->
            predictedCaloriesTextView.text =
                "${String.format("%.2f", predictedCalories)} kcal"  // Định dạng và hiển thị
            Log.d(
                "AnalysisActivity",
                "Predicted calories observed: $predictedCalories"
            ) // Để debug
        }

        // Gọi API để lấy danh sách món ăn hôm nay
        fetchDailyFoodList()
        fetchUserData() // Gọi hàm này để lấy thông tin người dùng

        // Thiết lập OnClickListener cho nút refresh
        refreshButton.setOnClickListener {
            fetchDailyFoodList() // Gọi lại hàm fetchDailyFoodList để tải lại dữ liệu
            fetchUserData()
            Toast.makeText(this, "Đã tải lại thực đơn", Toast.LENGTH_SHORT).show()
        }

        addButton.setOnClickListener {
            val foodName = foodNameInput.text.toString().trim()
            val quantityStr = quantityInput.text.toString().trim()

            if (foodName.isNotEmpty() && quantityStr.isNotEmpty()) {
                try {
                    val quantity = quantityStr.toInt()
                    val foodItem = "$foodName - Số lượng: $quantity"
                    foodListAdapter.addFoodItem(foodItem)
                    foodNameInput.text.clear()
                    quantityInput.text.clear()
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Vui lòng nhập số lượng hợp lệ", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Vui lòng nhập tên món ăn và số lượng", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        calculateCaloriesButton.setOnClickListener {
            if (userId != -1) {
                for (item in foodItems) {
                    val parts = item.split(" - Số lượng: ")
                    if (parts.size == 2) {
                        val foodName = parts[0].trim()
                        val quantityStr = parts[1].trim()
                        try {
                            val quantity = quantityStr.toInt()
                            foodEntryViewModel.saveFoodEntry(userId, foodName, quantity)
                            // Sau khi lưu thành công, gọi lại để cập nhật danh sách hàng ngày
                            fetchDailyFoodList()
                        } catch (e: NumberFormatException) {
                            Toast.makeText(
                                this,
                                "Lỗi định dạng số lượng: $item",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        }
                    } else {
                        Toast.makeText(this, "Lỗi định dạng món ăn: $item", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }
                }
                Toast.makeText(
                    this@AnalysisActivity,
                    "Đã gửi yêu cầu lưu thông tin calo",
                    Toast.LENGTH_SHORT
                ).show()
                foodItems.clear()
                foodListAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(
                    this@AnalysisActivity,
                    "Chưa có thông tin người dùng để lưu.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun fetchDailyFoodList() {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        foodEntryViewModel.fetchDailyFoodList(userId, currentDate)
    }

    private fun fetchUserData() {
        foodEntryViewModel.fetchUserData()
    }



    override fun onItemLongClick(position: Int) {
        val popupMenu = PopupMenu(
            this,
            foodListRecyclerView.findViewHolderForAdapterPosition(position)?.itemView
        )
        popupMenu.menuInflater.inflate(R.menu.food_item_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_edit -> {
                    showEditDialog(position)
                    true
                }

                R.id.action_delete -> {
                    showDeleteConfirmationDialog(position)
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showEditDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Sửa món ăn")

        val input = EditText(this)
        input.setText(foodItems[position].split(" - ")[0])
        builder.setView(input)

        builder.setPositiveButton("Lưu") { dialog, _ ->
            val newFoodName = input.text.toString().trim()
            if (newFoodName.isNotEmpty()) {
                val currentItemParts = foodItems[position].split(" - ")
                val quantityPart =
                    if (currentItemParts.size > 1) " - ${currentItemParts[1]}" else ""
                foodListAdapter.updateItem(position, "$newFoodName$quantityPart")
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Hủy") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Bạn có chắc chắn muốn xóa món ăn này?")
            .setPositiveButton("Xóa") { dialog, _ ->
                foodListAdapter.removeItem(position)
                dialog.dismiss()
            }
            .setNegativeButton("Hủy") { dialog, _ -> dialog.cancel() }
            .show()
    }
}

