package com.habitsapp.data.model

import androidx.room.Embedded
import androidx.room.Relation
import java.time.LocalDate

data class HabitWithCompletions(
    @Embedded val habit: Habit,
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId"
    )
    val completions: List<HabitCompletion>
) {
    val currentStreak: Int
        get() = calculateCurrentStreak()

    val longestStreak: Int
        get() = calculateLongestStreak()

    val completionRate: Float
        get() = calculateCompletionRate()

    val isCompletedToday: Boolean
        get() = completions.any {
            it.completionDate == LocalDate.now()
        }

    private fun calculateCurrentStreak(): Int {
        if (completions.isEmpty()) return 0

        val sortedCompletions = completions
            .map { it.completionDate }
            .sortedDescending()

        val today = LocalDate.now()
        val yesterday = today.minusDays(1)

        // Si no está completado hoy ni ayer, la racha es 0
        if (sortedCompletions.first() != today && sortedCompletions.first() != yesterday) {
            return 0
        }

        var streak = 0
        var currentDate = if (sortedCompletions.first() == today) today else yesterday

        for (completionDate in sortedCompletions) {
            if (completionDate == currentDate) {
                streak++
                currentDate = currentDate.minusDays(1)
            } else if (completionDate < currentDate) {
                break
            }
        }

        return streak
    }

    private fun calculateLongestStreak(): Int {
        val sortedCompletions = completions
            .map { it.completionDate }
            .sorted()

        if (sortedCompletions.isEmpty()) return 0

        var maxStreak = 1
        var currentStreak = 1

        for (i in 1 until sortedCompletions.size) {
            val prevDate = sortedCompletions[i - 1]
            val currentDate = sortedCompletions[i]

            if (currentDate == prevDate.plusDays(1)) {
                currentStreak++
                maxStreak = maxOf(maxStreak, currentStreak)
            } else if (currentDate != prevDate) {
                currentStreak = 1
            }
        }

        return maxStreak
    }

    private fun calculateCompletionRate(): Float {
        // Convertir timestamp (milisegundos) a LocalDate
        val creationDate = java.time.Instant
            .ofEpochMilli(habit.createdAt)
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate()

        val daysSinceCreation = java.time.temporal.ChronoUnit.DAYS.between(
            creationDate,
            LocalDate.now()
        ).toInt().coerceAtLeast(1)

        return (completions.size.toFloat() / daysSinceCreation * 100f).coerceAtMost(100f)
    }
}