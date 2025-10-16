package co.edu.upb.vitahabittracker.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "reminders",
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val habitId: Int,
    val time: String, // HH:mm format
    val frequency: ReminderFrequency,
    val isActive: Boolean = true,
    val daysOfWeek: String = "" // Comma separated for weekly reminders
)

enum class ReminderFrequency {
    HOURLY, DAILY, WEEKLY, MONTHLY, CUSTOM
}
