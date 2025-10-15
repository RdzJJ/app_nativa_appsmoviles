package com.habitsapp.data.repository

import com.habitsapp.data.database.HabitDao
import com.habitsapp.data.model.Habit
import com.habitsapp.data.model.HabitCompletion
import com.habitsapp.data.model.HabitWithCompletions
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepository @Inject constructor(
    private val habitDao: HabitDao
) {
    
    fun getAllHabitsWithCompletions(): Flow<List<HabitWithCompletions>> {
        return habitDao.getHabitsWithCompletions()
    }
    
    suspend fun getHabitById(habitId: Long): Habit? {
        return habitDao.getHabitById(habitId)
    }
    
    suspend fun getHabitWithCompletions(habitId: Long): HabitWithCompletions? {
        return habitDao.getHabitWithCompletions(habitId)
    }
    
    suspend fun insertHabit(habit: Habit): Long {
        return habitDao.insertHabit(habit)
    }
    
    suspend fun updateHabit(habit: Habit) {
        habitDao.updateHabit(habit)
    }
    
    suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit)
    }
    
    suspend fun deactivateHabit(habitId: Long) {
        habitDao.deactivateHabit(habitId)
    }
    
    suspend fun markHabitCompleted(habitId: Long, date: LocalDate = LocalDate.now(), notes: String? = null) {
        val completion = HabitCompletion(
            habitId = habitId,
            completionDate = date,
            notes = notes
        )
        habitDao.insertHabitCompletion(completion)
    }
    
    suspend fun markHabitIncomplete(habitId: Long, date: LocalDate = LocalDate.now()) {
        val completion = habitDao.getCompletionForHabitOnDate(habitId, date)
        completion?.let { habitDao.deleteHabitCompletion(it) }
    }
    
    suspend fun isHabitCompletedOnDate(habitId: Long, date: LocalDate): Boolean {
        return habitDao.getCompletionForHabitOnDate(habitId, date) != null
    }
    
    suspend fun getCompletionsForDate(date: LocalDate): List<HabitCompletion> {
        return habitDao.getCompletionsForDate(date)
    }
    
    suspend fun getTotalActiveHabits(): Int {
        return habitDao.getTotalActiveHabits()
    }
    
    suspend fun getCompletionsCountForDate(date: LocalDate): Int {
        return habitDao.getCompletionsCountsForDate(date)
    }
    
    suspend fun getTotalCompletionsForHabit(habitId: Long): Int {
        return habitDao.getTotalCompletionsForHabit(habitId)
    }
}

