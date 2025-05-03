package com.example.app_fitness.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_fitness.Entity.Comment
import com.example.app_fitness.databinding.ItemCommentInlineBinding

class InlineCommentAdapter(private val commentList: List<Comment>) :
    RecyclerView.Adapter<InlineCommentAdapter.InlineCommentViewHolder>() {

    class InlineCommentViewHolder(binding: ItemCommentInlineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val userName = binding.inlineCommentUserName
        val commentText = binding.inlineCommentText
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InlineCommentViewHolder {
        val binding = ItemCommentInlineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InlineCommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InlineCommentViewHolder, position: Int) {
        val currentItem = commentList[position]
        holder.userName.text = currentItem.userName ?: "Anonymous"
        holder.commentText.text = currentItem.commentText
    }

    override fun getItemCount() = commentList.size
}