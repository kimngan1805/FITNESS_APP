package com.example.app_fitness.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_fitness.Entity.DailyFoodItem
import com.example.app_fitness.R

class DailyFoodListAdapter(private val dailyFoodList: MutableList<DailyFoodItem>) : // Thay đổi ở đây
    RecyclerView.Adapter<DailyFoodListAdapter.DailyFoodViewHolder>() {

    class DailyFoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodNameTextView: TextView = itemView.findViewById(R.id.food_name_text_view)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantity_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyFoodViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_daily_food, parent, false)
        return DailyFoodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DailyFoodViewHolder, position: Int) {
        val currentItem = dailyFoodList[position]
        holder.foodNameTextView.text = currentItem.foodName
        holder.quantityTextView.text = "Số lượng: ${currentItem.quantity}"
    }

    override fun getItemCount() = dailyFoodList.size

    fun updateList(newList: List<DailyFoodItem>) {
        dailyFoodList.clear()
        dailyFoodList.addAll(newList)
        notifyDataSetChanged()
    }
}
