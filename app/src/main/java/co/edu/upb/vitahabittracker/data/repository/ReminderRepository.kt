package co.edu.upb.vitahabittracker.data.repository

import co.edu.upb.vitahabittracker.data.database.ReminderDao
import co.edu.upb.vitahabittracker.data.models.Reminder
import kotlinx.coroutines.flow.Flow

class ReminderRepository(private val reminderDao: ReminderDao) {
    fun getRemindersByHabit(habitId: Int): Flow<List<Reminder>> = reminderDao.getRemindersByHabit(habitId)

    fun getAllActiveReminders(): Flow<List<Reminder>> = reminderDao.getAllActiveReminders()

    fun getAllReminders(): Flow<List<Reminder>> = reminderDao.getAllReminders()

    suspend fun addReminder(reminder: Reminder): Long = reminderDao.insert(reminder)

    suspend fun updateReminder(reminder: Reminder) = reminderDao.update(reminder)

    suspend fun deleteReminder(reminder: Reminder) = reminderDao.delete(reminder)

    suspend fun getReminderById(reminderId: Int) = reminderDao.getReminderById(reminderId)

    suspend fun deleteRemindersByHabitId(habitId: Int) = reminderDao.deleteByHabitId(habitId)
}
