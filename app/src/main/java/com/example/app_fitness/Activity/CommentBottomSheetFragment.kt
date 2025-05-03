package com.example.app_fitness.Activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_fitness.Adapter.CommentAdapter
import com.example.app_fitness.Entity.Comment
import com.example.app_fitness.RestApi.ApiService
import com.example.app_fitness.RestApi.RetrofitClient
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.app_fitness.databinding.FragmentCommentBottomSheetBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_POST_ID = "post_id"

class CommentBottomSheetFragment : BottomSheetDialogFragment() {

    private var postId: Int? = null
    private lateinit var binding: FragmentCommentBottomSheetBinding
    private lateinit var commentAdapter: CommentAdapter
    private val comments = mutableListOf<Comment>()
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postId = it.getInt(ARG_POST_ID)
        }
        apiService = RetrofitClient.instance
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommentBottomSheetBinding.inflate(inflater, container, false)
        binding.commentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        commentAdapter = CommentAdapter(comments)
        binding.commentsRecyclerView.adapter = commentAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postId?.let { loadComments(it) }
    }

    private fun loadComments(postId: Int) {
        apiService.getComments(postId).enqueue(object : Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (response.isSuccessful) {
                    val fetchedComments = response.body()
                    fetchedComments?.let {
                        comments.clear()
                        comments.addAll(it)
                        commentAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e("CommentBottomSheet", "Failed to load comments: ${response.code()}")
                    // Xử lý lỗi (ví dụ: hiển thị Toast)
                }
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                Log.e("CommentBottomSheet", "Error loading comments: ${t.message}")
                // Xử lý lỗi kết nối (ví dụ: hiển thị Toast)
            }
        })
    }

    companion object {
        fun newInstance(postId: Int) =
            CommentBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POST_ID, postId)
                }
            }
    }
}