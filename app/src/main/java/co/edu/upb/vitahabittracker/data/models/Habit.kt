package co.edu.upb.vitahabittracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String = "",
    val frequency: HabitFrequency = HabitFrequency.DAILY,
    val color: String = "#4CAF50",
    val icon: String = "✓",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val isActive: Boolean = true
)

enum class HabitFrequency {
    DAILY, WEEKLY, MONTHLY
}
