package co.edu.upb.vitahabittracker.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import co.edu.upb.vitahabittracker.notification.NotificationManager

class ReminderWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        return try {
            val habitId = inputData.getInt("habitId", -1)
            val habitName = inputData.getString("habitName") ?: "Tu hábito"

            if (habitId != -1) {
                val notificationManager = NotificationManager(applicationContext)
                notificationManager.showReminderNotification(habitName, habitId)
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
