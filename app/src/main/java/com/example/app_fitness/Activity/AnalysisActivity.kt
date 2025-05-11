package com.example.app_fitness.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_fitness.Adapter.FoodListAdapter
import com.example.app_fitness.R

class AnalysisActivity : AppCompatActivity(), FoodListAdapter.OnItemLongClickListener {

    private lateinit var foodListRecyclerView: RecyclerView
    private lateinit var foodListAdapter: FoodListAdapter
    private val foodItems = mutableListOf<String>()
    private lateinit var addButton: Button
    private lateinit var foodNameInput: EditText
    private lateinit var quantityInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis)

        foodListRecyclerView = findViewById(R.id.food_list_recycler_view)
        addButton = findViewById(R.id.add_food_button)
        foodNameInput = findViewById(R.id.food_name_input)
        quantityInput = findViewById(R.id.quantity_input)

        // Thiết lập LayoutManager cho RecyclerView
        foodListRecyclerView.layoutManager = LinearLayoutManager(this)

        // Khởi tạo Adapter và gán cho RecyclerView, truyền this làm listener
        foodListAdapter = FoodListAdapter(foodItems, this)
        foodListRecyclerView.adapter = foodListAdapter

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
                Toast.makeText(this, "Vui lòng nhập tên món ăn và số lượng", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemLongClick(position: Int) {
        val popupMenu = PopupMenu(this, foodListRecyclerView.findViewHolderForAdapterPosition(position)?.itemView)
        popupMenu.menuInflater.inflate(R.menu.food_item_menu, popupMenu.menu) // Tạo file menu XML

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
        input.setText(foodItems[position].split(" - ")[0]) // Hiển thị tên món ăn hiện tại
        builder.setView(input)

        builder.setPositiveButton("Lưu") { dialog, _ ->
            val newFoodName = input.text.toString().trim()
            if (newFoodName.isNotEmpty()) {
                val currentItemParts = foodItems[position].split(" - ")
                val quantityPart = if (currentItemParts.size > 1) " - ${currentItemParts[1]}" else ""
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