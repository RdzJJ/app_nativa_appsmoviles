package co.edu.upb.vitahabittracker.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.edu.upb.vitahabittracker.data.models.HabitEntry
import co.edu.upb.vitahabittracker.data.repository.FirestoreHabitEntryRepository
import co.edu.upb.vitahabittracker.notification.NotificationManager
import co.edu.upb.vitahabittracker.utils.SessionManager
import java.time.LocalDate
import java.time.LocalTime

class HabitCompletionWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val habitId = inputData.getInt("habitId", -1)
            val habitName = inputData.getString("habitName") ?: "Tu hábito"

            if (habitId == -1) {
                return Result.failure()
            }

            // Get current user from session
            val sessionManager = SessionManager(applicationContext)
            val currentUser = sessionManager.getUser()

            if (currentUser == null) {
                return Result.failure()
            }

            // Initialize Firestore repository
            val habitEntryRepository = FirestoreHabitEntryRepository(currentUser.id)

            // Check if habit is already completed today
            val today = LocalDate.now()
            val existingEntries = habitEntryRepository.getHabitEntriesForDate(today)
            val isAlreadyCompleted = existingEntries.any { it.habitId == habitId }

            if (!isAlreadyCompleted) {
                // Create new habit entry
                val newEntry = HabitEntry(
                    id = System.currentTimeMillis().toInt(),
                    habitId = habitId,
                    completedDate = today,
                    completedTime = LocalTime.now().toString()
                )

                // Add entry to Firestore
                val result = habitEntryRepository.addHabitEntry(newEntry)
                
                if (result.isSuccess) {
                    // Show completion notification
                    val notificationManager = NotificationManager(applicationContext)
                    notificationManager.showHabitCompletedNotification(habitName)
                    
                    Result.success()
                } else {
                    Result.retry()
                }
            } else {
                // Already completed, just show a message
                val notificationManager = NotificationManager(applicationContext)
                notificationManager.showAlreadyCompletedNotification(habitName)
                Result.success()
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
