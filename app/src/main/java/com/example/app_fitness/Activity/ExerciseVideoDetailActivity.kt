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

class ExerciseVideoDetailActivity : AppCompatActivity() {

    private var youtubePlayerView: YouTubePlayerView? = null
    private lateinit var exerciseTitleTextView: TextView
    private lateinit var doneButton: Button
    private var currentExercise: ExerciseRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_video_detail)

        youtubePlayerView = findViewById(R.id.youtube_player_view)
        exerciseTitleTextView = findViewById(R.id.exerciseTitleTextView)
        doneButton = findViewById(R.id.doneButton)

        currentExercise = intent.getParcelableExtra<ExerciseRequest>("exercise")

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
            currentExercise?.let {
                val completedExercises = getSharedPreferences("CompletedExercises", MODE_PRIVATE)
                    .getStringSet("completed_ids", mutableSetOf())?.toMutableSet()
                completedExercises?.add(it.id.toString())
                getSharedPreferences("CompletedExercises", MODE_PRIVATE).edit()
                    .putStringSet("completed_ids", completedExercises).apply()

                Toast.makeText(this, "${it.exercise_name} đã hoàn thành!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun extractVideoIdFromUrl(url: String): String {
        val videoIdRegex = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|/video/)([\\w-]+)"
        val pattern = Regex(videoIdRegex)
        val matchResult = pattern.find(url)
        return matchResult?.value ?: ""
    }

    override fun onDestroy() {
        super.onDestroy()
        youtubePlayerView?.release()
    }
}