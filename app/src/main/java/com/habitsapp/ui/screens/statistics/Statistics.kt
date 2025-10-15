package com.habitsapp.ui.screens.statistics

data class Statistics(
    val totalHabits: Int = 0,
    val completedToday: Int = 0,
    val totalCompletions: Int = 0,
    val longestStreak: Int = 0,
    val averageCompletionRate: Float = 0f
)
