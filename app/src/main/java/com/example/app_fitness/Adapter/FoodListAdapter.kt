package com.example.app_fitness.Adapter
import java.util.List;
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_fitness.R

class FoodListAdapter(
    private val foodList: MutableList<String>,
    private val itemLongClickListener: OnItemLongClickListener
) : RecyclerView.Adapter<FoodListAdapter.FoodViewHolder>() {

    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int)
    }

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodTextView: TextView = itemView.findViewById(R.id.food_item_text_view)

        init {
            itemView.setOnLongClickListener {
                val adapterPosition = adapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    (itemView.context as? OnItemLongClickListener)?.onItemLongClick(adapterPosition)
                    true // Trả về true để cho biết sự kiện long click đã được xử lý
                } else {
                    false
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentItem = foodList[position]
        holder.foodTextView.text = currentItem
    }

    override fun getItemCount() = foodList.size

    fun addFoodItem(foodItem: String) {
        foodList.add(foodItem)
        notifyItemInserted(foodList.size - 1)
    }

    fun removeItem(position: Int) {
        foodList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateItem(position: Int, newItem: String) {
        foodList[position] = newItem
        notifyItemChanged(position)
    }
}