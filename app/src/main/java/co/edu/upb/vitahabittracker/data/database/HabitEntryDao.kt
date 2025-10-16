package co.edu.upb.vitahabittracker.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import co.edu.upb.vitahabittracker.data.models.HabitEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HabitEntryDao {
    @Insert
    suspend fun insert(entry: HabitEntry): Long

    @Update
    suspend fun update(entry: HabitEntry)

    @Delete
    suspend fun delete(entry: HabitEntry)

    @Query("SELECT * FROM habit_entries WHERE habitId = :habitId AND completedDate = :date")
    suspend fun getEntryByHabitAndDate(habitId: Int, date: LocalDate): HabitEntry?

    @Query("SELECT * FROM habit_entries WHERE habitId = :habitId ORDER BY completedDate DESC")
    fun getEntriesByHabit(habitId: Int): Flow<List<HabitEntry>>

    @Query("SELECT COUNT(*) FROM habit_entries WHERE habitId = :habitId AND completedDate >= date(:startDate) AND completedDate <= date(:endDate)")
    suspend fun getCompletionCount(habitId: Int, startDate: LocalDate, endDate: LocalDate): Int

    @Query("SELECT * FROM habit_entries WHERE habitId = :habitId AND completedDate = date('now')")
    suspend fun getTodayEntry(habitId: Int): HabitEntry?

    @Query("DELETE FROM habit_entries WHERE habitId = :habitId")
    suspend fun deleteByHabitId(habitId: Int)

    @Query("SELECT * FROM habit_entries WHERE completedDate = :date ORDER BY completedTime")
    fun getEntriesByDate(date: LocalDate): Flow<List<HabitEntry>>
}
