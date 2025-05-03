package com.example.app_fitness.Entity

data class FeedItem(
    val id: Int,
    val admin: String,
    var postTime: String?,
    val image_url: String, // Sửa thành image_url
    val description: String,
    val hashtag: String,
    val like_count: Int, // Sửa thành like_count
    val comment_count: Int, // Sửa thành comment_count
    var is_bookmarked: Boolean = false // Sửa thành is_bookmarked (nếu cần)
)