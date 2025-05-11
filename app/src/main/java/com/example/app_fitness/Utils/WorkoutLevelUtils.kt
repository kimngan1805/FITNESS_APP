package com.example.app_fitness.Utils


object WorkoutLevelUtils {
    val workoutLevelFactors = mapOf(
        "Không vận động" to 1.2,
        "Tập luyện nhẹ nhàng 1-2 lần/tuần" to 1.375,
        "Tập luyện vừa phải 3-4 lần/tuần" to 1.55,
        "Tập luyện nhiều 5-6 lần/tuần" to 1.725,
        "Vận động liên tục" to 1.9
    )
}
