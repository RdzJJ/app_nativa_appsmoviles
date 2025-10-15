package com.habitsapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val frequency: HabitFrequency,
    val reminderTime: LocalTime? = null,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val color: String = "#6750A4" // Default color
)

enum class HabitFrequency(val displayName: String, val intervalHours: Long) {
    HOURLY("Cada hora", 1),
    DAILY("Diario", 24),
    WEEKLY("Semanal", 168), // 7 * 24
    MONTHLY("Mensual", 720) // 30 * 24 (approximate)
}

