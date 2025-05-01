package com.example.app_fitness.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_fitness.Entity.CategoryRequest
import com.example.app_fitness.databinding.ItemWorkoutPlanBinding
import com.squareup.picasso.Picasso
class CategoryAdapter(private val categoryList: List<CategoryRequest>, private val onItemClick: (CategoryRequest) -> Unit ) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemWorkoutPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.bind(category)
        // Xử lý sự kiện nhấn vào item
        holder.itemView.setOnClickListener {
            onItemClick(category)  // Gọi callback khi người dùng nhấn vào item
        }
    }

    override fun getItemCount(): Int = categoryList.size

    class CategoryViewHolder(private val binding: ItemWorkoutPlanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryRequest) {
            binding.categoryName.text = category.category_name
            Picasso.get().load(category.image_url).into(binding.categoryImage)
        }
    }
}