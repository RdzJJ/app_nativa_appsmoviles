package co.edu.upb.vitahabittracker.data.repository

import co.edu.upb.vitahabittracker.data.database.HabitDao
import co.edu.upb.vitahabittracker.data.database.HabitEntryDao
import co.edu.upb.vitahabittracker.data.models.Habit
import co.edu.upb.vitahabittracker.data.models.HabitEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class HabitRepository(
    private val habitDao: HabitDao,
    private val habitEntryDao: HabitEntryDao
) {
    fun getAllActiveHabits(): Flow<List<Habit>> = habitDao.getAllActiveHabits()

    fun getAllHabits(): Flow<List<Habit>> = habitDao.getAllHabits()

    suspend fun addHabit(habit: Habit): Long = habitDao.insert(habit)

    suspend fun updateHabit(habit: Habit) = habitDao.update(habit)

    suspend fun deleteHabit(habitId: Int) {
        habitDao.deleteById(habitId)
        habitEntryDao.deleteByHabitId(habitId)
    }

    suspend fun getHabitById(habitId: Int) = habitDao.getHabitById(habitId)

    // Habit Entry Operations
    suspend fun markHabitComplete(habitId: Int, date: LocalDate) {
        val entry = HabitEntry(
            habitId = habitId,
            completedDate = date,
            completedTime = java.time.LocalTime.now().toString()
        )
        habitEntryDao.insert(entry)
    }

    suspend fun isHabitCompletedToday(habitId: Int): Boolean {
        return habitEntryDao.getTodayEntry(habitId) != null
    }

    fun getHabitEntries(habitId: Int): Flow<List<HabitEntry>> = habitEntryDao.getEntriesByHabit(habitId)

    suspend fun getCompletionCount(habitId: Int, startDate: LocalDate, endDate: LocalDate): Int {
        return habitEntryDao.getCompletionCount(habitId, startDate, endDate)
    }

    fun getEntriesByDate(date: LocalDate): Flow<List<HabitEntry>> = habitEntryDao.getEntriesByDate(date)
}
