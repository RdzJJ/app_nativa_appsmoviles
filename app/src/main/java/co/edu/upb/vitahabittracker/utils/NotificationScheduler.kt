package co.edu.upb.vitahabittracker.utils

import android.content.Context
import androidx.work.*
import co.edu.upb.vitahabittracker.data.models.Habit
import co.edu.upb.vitahabittracker.data.models.HabitFrequency
import co.edu.upb.vitahabittracker.workers.ReminderWorker
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class NotificationScheduler(private val context: Context) {

    private val workManager = WorkManager.getInstance(context)

    fun scheduleHabitReminder(habit: Habit) {
        habit.reminderTime?.let { timeString ->
            val time = LocalTime.parse(timeString)
            val now = LocalDateTime.now()
            val scheduledTime = now.toLocalDate().atTime(time)

            // If the time has passed today, schedule for tomorrow
            val targetTime =
                    if (scheduledTime.isBefore(now)) {
                        scheduledTime.plusDays(1)
                    } else {
                        scheduledTime
                    }

            val delay = Duration.between(now, targetTime).toMillis()

            val inputData =
                    Data.Builder()
                            .putInt("habitId", habit.id)
                            .putString("habitName", habit.name)
                            .build()

            val constraints = Constraints.Builder().setRequiresBatteryNotLow(false).build()

            val workRequest =
                    when (habit.frequency) {
                        HabitFrequency.DAILY -> {
                            PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
                                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                                    .setInputData(inputData)
                                    .setConstraints(constraints)
                                    .addTag("habit_reminder_${habit.id}")
                                    .build()
                        }
                        HabitFrequency.WEEKLY -> {
                            PeriodicWorkRequestBuilder<ReminderWorker>(7, TimeUnit.DAYS)
                                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                                    .setInputData(inputData)
                                    .setConstraints(constraints)
                                    .addTag("habit_reminder_${habit.id}")
                                    .build()
                        }
                        HabitFrequency.MONTHLY -> {
                            PeriodicWorkRequestBuilder<ReminderWorker>(30, TimeUnit.DAYS)
                                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                                    .setInputData(inputData)
                                    .setConstraints(constraints)
                                    .addTag("habit_reminder_${habit.id}")
                                    .build()
                        }
                    }

            workManager.enqueueUniquePeriodicWork(
                    "habit_reminder_${habit.id}",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    workRequest
            )
        }
    }

    fun cancelHabitReminder(habitId: Int) {
        workManager.cancelAllWorkByTag("habit_reminder_$habitId")
    }

    fun cancelAllReminders() {
        workManager.cancelAllWork()
    }
}
