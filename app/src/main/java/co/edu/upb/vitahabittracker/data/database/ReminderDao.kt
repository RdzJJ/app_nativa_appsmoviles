package co.edu.upb.vitahabittracker.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import co.edu.upb.vitahabittracker.data.models.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Insert
    suspend fun insert(reminder: Reminder): Long

    @Update
    suspend fun update(reminder: Reminder)

    @Delete
    suspend fun delete(reminder: Reminder)

    @Query("SELECT * FROM reminders WHERE id = :reminderId")
    suspend fun getReminderById(reminderId: Int): Reminder?

    @Query("SELECT * FROM reminders WHERE habitId = :habitId")
    fun getRemindersByHabit(habitId: Int): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE isActive = 1")
    fun getAllActiveReminders(): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders ORDER BY habitId")
    fun getAllReminders(): Flow<List<Reminder>>

    @Query("DELETE FROM reminders WHERE habitId = :habitId")
    suspend fun deleteByHabitId(habitId: Int)
}
