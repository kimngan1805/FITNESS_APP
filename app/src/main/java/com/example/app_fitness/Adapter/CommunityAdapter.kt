package com.example.app_fitness.Adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_fitness.Entity.Community
import com.example.app_fitness.R

class CommunityAdapter(private val context: Context, private val userList: List<Community>) :
    RecyclerView.Adapter<CommunityAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatarImageView: ImageView = itemView.findViewById(R.id.user_avatar)
        val nameTextView: TextView = itemView.findViewById(R.id.user_name)
        val infoTextView: TextView = itemView.findViewById(R.id.user_info)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false) // Sử dụng user_item.xml
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.avatarImageView.setImageResource(currentUser.avatar)
        holder.nameTextView.text = currentUser.name
        holder.infoTextView.text = currentUser.info
    }

    override fun getItemCount() = userList.size
}