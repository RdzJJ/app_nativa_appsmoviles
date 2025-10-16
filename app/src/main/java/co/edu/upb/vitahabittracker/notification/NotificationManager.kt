package co.edu.upb.vitahabittracker.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import co.edu.upb.vitahabittracker.R

class NotificationManager(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val reminderChannel = NotificationChannel(
                REMINDER_CHANNEL_ID,
                "Recordatorios de Hábitos",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de recordatorios para tus hábitos"
            }

            val habitChannel = NotificationChannel(
                HABIT_CHANNEL_ID,
                "Actualizaciones de Hábitos",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones sobre tus hábitos"
            }

            notificationManager.createNotificationChannels(listOf(reminderChannel, habitChannel))
        }
    }

    fun showReminderNotification(habitName: String, notificationId: Int = REMINDER_NOTIFICATION_ID) {
        val notification = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Recordatorio de Hábito")
            .setContentText("¡Es hora de registrar: $habitName!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    fun showHabitCompletedNotification(habitName: String, notificationId: Int = HABIT_NOTIFICATION_ID) {
        val notification = NotificationCompat.Builder(context, HABIT_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("¡Hábito Completado!")
            .setContentText("Excelente: $habitName ha sido registrado.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    companion object {
        const val REMINDER_CHANNEL_ID = "habit_reminders"
        const val HABIT_CHANNEL_ID = "habit_updates"
        const val REMINDER_NOTIFICATION_ID = 1001
        const val HABIT_NOTIFICATION_ID = 1002
    }
}
