package co.edu.upb.vitahabittracker.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import co.edu.upb.vitahabittracker.workers.HabitCompletionWorker

class HabitNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_COMPLETE_HABIT -> {
                val habitId = intent.getIntExtra(EXTRA_HABIT_ID, -1)
                val habitName = intent.getStringExtra(EXTRA_HABIT_NAME) ?: ""
                
                if (habitId != -1) {
                    // Schedule work to complete the habit
                    val workRequest = OneTimeWorkRequestBuilder<HabitCompletionWorker>()
                        .setInputData(
                            workDataOf(
                                "habitId" to habitId,
                                "habitName" to habitName
                            )
                        )
                        .build()
                    
                    WorkManager.getInstance(context).enqueue(workRequest)
                    
                    // Cancel the notification
                    val notificationManager = NotificationManager(context)
                    notificationManager.cancelNotification(habitId)
                }
            }
            ACTION_DISMISS_NOTIFICATION -> {
                val habitId = intent.getIntExtra(EXTRA_HABIT_ID, -1)
                if (habitId != -1) {
                    val notificationManager = NotificationManager(context)
                    notificationManager.cancelNotification(habitId)
                }
            }
        }
    }

    companion object {
        const val ACTION_COMPLETE_HABIT = "co.edu.upb.vitahabittracker.COMPLETE_HABIT"
        const val ACTION_DISMISS_NOTIFICATION = "co.edu.upb.vitahabittracker.DISMISS_NOTIFICATION"
        const val EXTRA_HABIT_ID = "habit_id"
        const val EXTRA_HABIT_NAME = "habit_name"
    }
}
