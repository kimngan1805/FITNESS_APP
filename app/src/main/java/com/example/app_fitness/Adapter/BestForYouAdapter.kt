package com.example.app_fitness.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_fitness.R

data class BestForYouItem(
    val title: String,
    val time: String,
    val level: String,
    val imageResource: Int
)

class BestForYouAdapter(private val itemList: List<BestForYouItem>, ) :
    RecyclerView.Adapter<BestForYouAdapter.BestForYouViewHolder>() {

    inner class BestForYouViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.itemImageView)
        val titleTextView: TextView = itemView.findViewById(R.id.itemTitleTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.itemTimeTextView)
        val levelTextView: TextView = itemView.findViewById(R.id.itemLevelTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestForYouViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_suggest, parent, false)
        return BestForYouViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BestForYouViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.titleTextView.text = currentItem.title
        holder.timeTextView.text = currentItem.time
        holder.levelTextView.text = currentItem.level
        holder.imageView.setImageResource(currentItem.imageResource)
        // Nếu bạn cần load hình ảnh từ URL, hãy sử dụng thư viện như Coil hoặc Glide ở đây
    }

    override fun getItemCount() = itemList.size
}