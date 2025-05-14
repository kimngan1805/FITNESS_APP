package com.example.app_fitness.Entity

data class TodayPlanItem(
    val title: String,
    val subtitle: String,
    val progress: Int,
    val progressText: String,
    val imageResId: Int,
    val level: String,
    val levelBackground: Int // Resource ID cho background của level tag
)