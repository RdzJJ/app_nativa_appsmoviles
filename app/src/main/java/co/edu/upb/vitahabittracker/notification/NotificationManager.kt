package co.edu.upb.vitahabittracker.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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

    fun showReminderNotification(habitName: String, habitId: Int, notificationId: Int = REMINDER_NOTIFICATION_ID) {
        // Create intent for completing the habit
        val completeIntent = Intent(context, HabitNotificationReceiver::class.java).apply {
            action = HabitNotificationReceiver.ACTION_COMPLETE_HABIT
            putExtra(HabitNotificationReceiver.EXTRA_HABIT_ID, habitId)
            putExtra(HabitNotificationReceiver.EXTRA_HABIT_NAME, habitName)
        }
        
        val completePendingIntent = PendingIntent.getBroadcast(
            context,
            habitId,
            completeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Create intent for dismissing the notification
        val dismissIntent = Intent(context, HabitNotificationReceiver::class.java).apply {
            action = HabitNotificationReceiver.ACTION_DISMISS_NOTIFICATION
            putExtra(HabitNotificationReceiver.EXTRA_HABIT_ID, habitId)
        }
        
        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            habitId + 1000,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_vita)
            .setContentTitle("Recordatorio de Hábito")
            .setContentText("¡Es hora de registrar: $habitName!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .addAction(
                android.R.drawable.ic_menu_agenda,
                "Completar",
                completePendingIntent
            )
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "Más tarde",
                dismissPendingIntent
            )
            .build()

        notificationManager.notify(notificationId, notification)
    }

    fun showHabitCompletedNotification(habitName: String, notificationId: Int = HABIT_NOTIFICATION_ID) {
        val notification = NotificationCompat.Builder(context, HABIT_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_vita)
            .setContentTitle("¡Hábito Completado!")
            .setContentText("Excelente: $habitName ha sido registrado.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    fun showAlreadyCompletedNotification(habitName: String, notificationId: Int = HABIT_NOTIFICATION_ID) {
        val notification = NotificationCompat.Builder(context, HABIT_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_vita)
            .setContentTitle("Ya completado")
            .setContentText("$habitName ya fue registrado hoy.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    fun cancelNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }

    companion object {
        const val REMINDER_CHANNEL_ID = "habit_reminders"
        const val HABIT_CHANNEL_ID = "habit_updates"
        const val REMINDER_NOTIFICATION_ID = 1001
        const val HABIT_NOTIFICATION_ID = 1002
    }
}
