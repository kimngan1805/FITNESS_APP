package com.example.app_fitness.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_fitness.Entity.Comment
import com.example.app_fitness.databinding.ItemCommentBinding
import java.text.SimpleDateFormat
import java.util.*

class CommentAdapter(private val commentList: List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        val userAvatar: ImageView = binding.commentUserAvatar
        val userName: TextView = binding.commentUserName
        val createdAt: TextView = binding.commentCreatedAt
        val commentText: TextView = binding.commentText
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentItem = commentList[position]
        holder.userName.text = currentItem.userName
        holder.commentText.text = currentItem.commentText
        holder.createdAt.text = formatDate(currentItem.createdAt)

    }

    private fun formatDate(timestamp: String?): String {
        timestamp?.let {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            return try {
                val date = inputFormat.parse(it)
                date?.let { outputFormat.format(it) } ?: ""
            } catch (e: Exception) {
                it
            }
        }
        return ""
    }

    override fun getItemCount() = commentList.size
}