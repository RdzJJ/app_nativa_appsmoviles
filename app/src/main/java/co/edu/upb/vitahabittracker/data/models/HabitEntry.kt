package co.edu.upb.vitahabittracker.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "habit_entries",
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HabitEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val habitId: Int,
    val completedDate: LocalDate,
    val completedTime: String = "",
    val notes: String = ""
)
