package com.example.app_fitness.Activity

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_fitness.Entity.ExerciseDetailRequest
import com.example.app_fitness.Entity.ExerciseRequest
import com.example.app_fitness.R
import com.google.android.exoplayer2.util.Log
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.example.app_fitness.RestApi.RetrofitClient
import com.example.app_fitness.Entity.MarkCompletedRequest // Import the data class
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class ExerciseVideoDetailActivity : AppCompatActivity() {

    private var youtubePlayerView: YouTubePlayerView? = null
    private lateinit var exerciseTitleTextView: TextView
    private lateinit var doneButton: Button
    private var currentExercise: ExerciseDetailRequest? = null
    private var userId: Int = -1
    private var completedExerciseIds: List<Int> = emptyList() // Danh sách ID bài tập đã hoàn thành
    private lateinit var backButton: Button // Declare backButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_video_detail)
        youtubePlayerView = findViewById(R.id.youtube_player_view)
        exerciseTitleTextView = findViewById(R.id.exerciseTitleTextView)
        doneButton = findViewById(R.id.doneButton)
        currentExercise = intent.getParcelableExtra<ExerciseDetailRequest>("exercise")
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        userId = sharedPref.getInt("user_id", -1)
        currentExercise?.let {
            Log.d("ExerciseDetail", " Video URL: ${it.videoUrl}")
            exerciseTitleTextView.text = it.detailName
            lifecycle.addObserver(youtubePlayerView!!)
            youtubePlayerView?.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val videoId = extractVideoIdFromUrl(it.videoUrl)
                    if (videoId.isNotEmpty()) {
                        youTubePlayer.loadVideo(videoId, 0f)
                    } else {
                        Toast.makeText(this@ExerciseVideoDetailActivity, "URL YouTube không hợp lệ", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
        // Lấy danh sách bài tập đã hoàn thành
        if (userId != -1) {
            getCompletedExercises()
        } else {
            Toast.makeText(this, "Bạn cần đăng nhập để xem tiến trình", Toast.LENGTH_SHORT).show()
        }
        doneButton.setOnClickListener {
            currentExercise?.let { exercise ->
                if (userId != -1) {
                    markExerciseAsCompleted(userId, exercise.detailId)
                } else {
                    Toast.makeText(this, "Bạn cần đăng nhập để lưu tiến trình", Toast.LENGTH_SHORT).show()
                }
            }
        }
        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // hoặc dùng finish() nếu muốn đơn giản
        }

    }
    private fun extractVideoIdFromUrl(url: String): String {
        val videoIdRegex = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|/video/)([\\w-]+)"
        val pattern = Regex(videoIdRegex)
        val matchResult = pattern.find(url)
        return matchResult?.value ?: ""
    }

    private fun markExerciseAsCompleted(userId: Int, detailId: Int) {
        RetrofitClient.instance.markExerciseCompleted(userId, detailId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ExerciseVideoDetailActivity, "Đã đánh dấu hoàn thành!", Toast.LENGTH_SHORT).show()
                    // Sau khi đánh dấu hoàn thành, cập nhật lại danh sách bài tập đã hoàn thành
                    getCompletedExercises()
                    finish()
                } else {
                    Toast.makeText(this@ExerciseVideoDetailActivity, "Lỗi khi đánh dấu hoàn thành.  Response Code: ${response.code()}", Toast.LENGTH_SHORT).show()
                    Log.e("markExerciseCompleted", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("markExerciseCompleted", "Error: ${t.message}")
                Toast.makeText(this@ExerciseVideoDetailActivity, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun getCompletedExercises() {
        RetrofitClient.instance.getCompletedExercises(userId).enqueue(object : Callback<List<Int>> {
            override fun onResponse(call: Call<List<Int>>, response: Response<List<Int>>) {
                if (response.isSuccessful) {
                    completedExerciseIds = response.body() ?: emptyList()
                    updateUI() // Cập nhật giao diện sau khi lấy dữ liệu
                } else {
                    Toast.makeText(this@ExerciseVideoDetailActivity, "Lỗi khi lấy danh sách bài tập đã hoàn thành", Toast.LENGTH_SHORT).show()
                    Log.e("getCompletedExercises", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Int>>, t: Throwable) {
                Log.e("getCompletedExercises", "Error: ${t.message}")
                Toast.makeText(this@ExerciseVideoDetailActivity, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun updateUI() {
        currentExercise?.let { exercise ->
            if (completedExerciseIds.contains(exercise.detailId)) {
                doneButton.isEnabled = false // Vô hiệu hóa nút "Done"
                doneButton.text = "Đã hoàn thành" // Thay đổi text
            } else {
                doneButton.isEnabled = true
                doneButton.text = "Done"
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        youtubePlayerView?.release()
    }
}
