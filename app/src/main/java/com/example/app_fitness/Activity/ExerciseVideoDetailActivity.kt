package com.example.app_fitness.Activity

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    private var currentExercise: ExerciseRequest? = null
    private var userId: Int = -1 // Store user ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_video_detail)

        youtubePlayerView = findViewById(R.id.youtube_player_view)
        exerciseTitleTextView = findViewById(R.id.exerciseTitleTextView)
        doneButton = findViewById(R.id.doneButton)

        currentExercise = intent.getParcelableExtra<ExerciseRequest>("exercise")

        // Get user ID from shared preferences
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        userId = sharedPref.getInt("user_id", -1)

        currentExercise?.let {
            Log.d("ExerciseDetail", "Gender: ${it.gender}, Video URL: ${it.video_url}")
            exerciseTitleTextView.text = it.exercise_name
            lifecycle.addObserver(youtubePlayerView!!)

            youtubePlayerView?.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val videoId = extractVideoIdFromUrl(it.video_url)
                    if (videoId.isNotEmpty()) {
                        youTubePlayer.loadVideo(videoId, 0f)
                    } else {
                        Toast.makeText(this@ExerciseVideoDetailActivity, "URL YouTube không hợp lệ", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        doneButton.setOnClickListener {
            currentExercise?.let { exercise ->
                if (userId != -1) {
                    markExerciseAsCompleted(userId, exercise.id) // Call the function with individual parameters
                } else {
                    Toast.makeText(this, "Bạn cần đăng nhập để lưu tiến trình", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun extractVideoIdFromUrl(url: String): String {
        val videoIdRegex = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|/video/)([\\w-]+)"
        val pattern = Regex(videoIdRegex)
        val matchResult = pattern.find(url)
        return matchResult?.value ?: ""
    }

    private fun markExerciseAsCompleted(userId: Int, exerciseId: Int) {
        // Remove the HashMap creation
        RetrofitClient.instance.markExerciseCompleted(userId, exerciseId).enqueue(object : Callback<Void> { // Pass userId and exerciseId directly
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ExerciseVideoDetailActivity, "Đã đánh dấu hoàn thành!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@ExerciseVideoDetailActivity, "Lỗi khi đánh dấu hoàn thành.  Response Code: ${response.code()}", Toast.LENGTH_SHORT).show() //Added response code
                    Log.e("markExerciseCompleted", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("markExerciseCompleted", "Error: ${t.message}")
                Toast.makeText(this@ExerciseVideoDetailActivity, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        youtubePlayerView?.release()
    }
}

