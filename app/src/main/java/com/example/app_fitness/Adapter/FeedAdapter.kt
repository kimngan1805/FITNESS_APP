package com.example.app_fitness.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_fitness.Entity.Comment
import com.example.app_fitness.Entity.FeedItem
import com.example.app_fitness.R
import com.example.app_fitness.Response.CommentCountResponse
import com.example.app_fitness.RestApi.RetrofitClient
import com.example.app_fitness.databinding.ItemFeedBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FeedAdapter(private val feedList: List<FeedItem>, private val currentUsername: String?) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {


    class FeedViewHolder(binding: ItemFeedBinding) : RecyclerView.ViewHolder(binding.root) {

        val userAvatar: ImageView = binding.userAvatar
        val userName: TextView = binding.userName
        val postTime: TextView = binding.postTime
        val postImageVideo: ImageView = binding.postImageVideo
        val postDescription: TextView = binding.postDescription
        val postHashtag: TextView = binding.postHashtag
        val likeCount: TextView = binding.likeCount
        val commentCount: TextView = binding.commentCount
        val bookmarkButton: ImageView = binding.bookmarkButton
        val commentIcon: ImageView = binding.commentIcon
        val viewComments: TextView? = binding.viewComments
        val commentsContainer: LinearLayout = binding.commentsContainer
        val nestedCommentsRecyclerView: RecyclerView = binding.nestedCommentsRecyclerView
        val commentInput: EditText = binding.commentInput // Thay TextView bằng EditText
        val sendCommentButton: ImageView = binding.sendCommentButton
        val currentUserName: TextView = binding.currentUserName
        private var initialCommentCount: Int = 0 // Lưu trữ số lượng bình luận ban đầu

        private var currentPostId: Int? = null

        init {
            viewComments?.setOnClickListener {
                currentPostId?.let { postId ->
                    loadActualCommentCount(postId)
                    commentsContainer.visibility = if (commentsContainer.visibility == View.GONE) View.VISIBLE else View.GONE
                    // Tải bình luận khi container được hiển thị và chưa có adapter
                    if (commentsContainer.visibility == View.VISIBLE && nestedCommentsRecyclerView.adapter == null) {
                        loadInlineComments(postId)
                    }
                }
            }
            commentIcon.setOnClickListener {
                currentPostId?.let {
                    // Xử lý click để xem/thêm bình luận (chuyển trang hoặc hiển thị dialog)
                    // Bạn có thể gọi một callback ở Activity để xử lý việc này nếu cần
                    Log.d("FeedAdapter", "Comment icon clicked for post ID: $it")
                }
            }


            sendCommentButton.setOnClickListener {
                currentPostId?.let { postId ->
                    val commentText = commentInput.text.toString().trim()
                    if (commentText.isNotEmpty()) {
                        val sharedPref = itemView.context.getSharedPreferences("UserData", AppCompatActivity.MODE_PRIVATE)
                        val userId = sharedPref.getInt("user_id", -1)

                        if (userId != -1) {
                            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            val createdAt = sdf.format(Date())

                            addCommentToServer(postId, userId, commentText, createdAt)
                            commentInput.text.clear()
                        } else {
                            Log.e("FeedAdapter", "User ID not found")
                        }
                    } else {
                        // Thông báo bình luận trống
                    }
                }
            }


        }

        private fun addCommentToServer(postId: Int, userId: Int, commentText: String, createdAt: String) {
            val apiService = RetrofitClient.instance
            apiService.addComment(postId, userId, commentText, createdAt).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("FeedAdapter", "Comment added successfully")
                        // Gọi callback để refresh bình luận nếu cần
                    } else {
                        Log.e("FeedAdapter", "Failed to add comment: ${response.code()}")
                        // Xử lý lỗi
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("FeedAdapter", "Error adding comment: ${t.message}")
                    // Xử lý lỗi kết nối
                }
            })
        }

        fun bind(currentItem: FeedItem) {
            currentPostId = currentItem.id
            userName.text = currentItem.admin // Lưu ý: "admin" ở đây có thể là tên người đăng bài
            postTime.text = currentItem.postTime
            postDescription.text = currentItem.description
            postHashtag.text = currentItem.hashtag
            likeCount.text = currentItem.like_count.toString()
            commentCount.text = currentItem.comment_count.toString()
            initialCommentCount = currentItem.comment_count // Lưu trữ giá trị ban đầu
            bookmarkButton.setImageResource(if (currentItem.is_bookmarked) R.drawable.ic_save else R.drawable.ic_save)
            Log.d("FeedAdapter", "Loading image URL: ${currentItem.image_url}")

            Glide.with(itemView.context)
                .load(currentItem.image_url)
                .placeholder(R.drawable.activity_hinh)
                .error(R.drawable.ic_launcher_background)
                .into(postImageVideo)

            bookmarkButton.setOnClickListener {
                currentItem.is_bookmarked = !currentItem.is_bookmarked
                bookmarkButton.setImageResource(if (currentItem.is_bookmarked) R.drawable.ic_save else R.drawable.ic_save)
                // Bạn sẽ cần gọi API để cập nhật trạng thái bookmark trên server
            }

            // Hiển thị "Xem bình luận..." nếu có bình luận
            if (currentItem.comment_count > 0 && viewComments != null) {
                viewComments.visibility = View.VISIBLE
                viewComments.text = "Xem ${initialCommentCount} bình luận..."
            } else if (viewComments != null) {
                viewComments.visibility = View.GONE
            }

            // Hiển thị tên người dùng ở phần nhập bình luận
            currentUserName.text = (itemView.context.getSharedPreferences("UserData", AppCompatActivity.MODE_PRIVATE)
                .getString("fullname", "Your Name")) ?: "Your Name" // Lấy fullname đã lưu
        }

        private fun loadActualCommentCount(postId: Int) {
            val apiService = RetrofitClient.instance

            // Sử dụng endpoint getCommentCount.php (như đã định nghĩa trước đó)
            apiService.getCommentCount(postId).enqueue(object : Callback<CommentCountResponse> {
                override fun onResponse(call: Call<CommentCountResponse>, response: Response<CommentCountResponse>) {
                    if (response.isSuccessful) {
                        val actualCount = response.body()?.count ?: 0
                        if (viewComments != null) {
                            viewComments.text = "Xem ${actualCount} bình luận..."
                        }
                    } else {
                        Log.e("FeedAdapter", "Failed to load actual comment count: ${response.code()}")
                        // Nếu lỗi, vẫn giữ số lượng ban đầu hoặc hiển thị thông báo lỗi
                        if (viewComments != null) {
                            viewComments.text = "Xem ${initialCommentCount} bình luận..."
                        }
                    }
                }

                override fun onFailure(call: Call<CommentCountResponse>, t: Throwable) {
                    Log.e("FeedAdapter", "Error loading actual comment count: ${t.message}")
                    // Nếu lỗi, vẫn giữ số lượng ban đầu hoặc hiển thị thông báo lỗi
                    if (viewComments != null) {
                        viewComments.text = "Xem ${initialCommentCount} bình luận..."
                    }
                }
            })
        }

        private fun loadInlineComments(postId: Int) {
            val apiService = RetrofitClient.instance

            apiService.getComments(postId).enqueue(object : Callback<List<Comment>> {
                override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                    if (response.isSuccessful) {
                        val fetchedComments = response.body() ?: emptyList()
                        nestedCommentsRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
                        nestedCommentsRecyclerView.adapter = CommentAdapter(fetchedComments)
                    } else {
                        Log.e("FeedAdapter", "Failed to load inline comments: ${response.code()}")
                        // Xử lý lỗi
                    }
                }

                override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                    Log.e("FeedAdapter", "Error loading inline comments: ${t.message}")
                    // Xử lý lỗi
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val binding = ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val currentItem = feedList[position]
        // Truyền tên người dùng vào bind view holder (lúc này chúng ta đã lấy nó trong bind)
        holder.bind(currentItem)
    }

    override fun getItemCount() = feedList.size



}