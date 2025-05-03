package com.example.app_fitness.Entity

import com.google.gson.annotations.SerializedName

data class Comment(
    val id: Int,
    @SerializedName("post_id") val postId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("comment_text") val commentText: String,
    @SerializedName("created_at") val createdAt: String,
    // Thêm trường userName nếu bạn join với bảng users trong API
    @SerializedName("commenter_name") val userName: String? = null
    // Bạn có thể thêm các trường khác như user avatar URL nếu cần
)