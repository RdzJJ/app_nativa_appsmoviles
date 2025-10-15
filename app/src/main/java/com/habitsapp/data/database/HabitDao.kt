package com.habitsapp.data.database

import androidx.room.*
import com.habitsapp.data.model.Habit
import com.habitsapp.data.model.HabitCompletion
import com.habitsapp.data.model.HabitWithCompletions
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HabitDao {
    
    @Query("SELECT * FROM habits WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getAllActiveHabits(): Flow<List<Habit>>
    
    @Query("SELECT * FROM habits WHERE id = :habitId")
    suspend fun getHabitById(habitId: Long): Habit?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long
    
    @Update
    suspend fun updateHabit(habit: Habit)
    
    @Delete
    suspend fun deleteHabit(habit: Habit)
    
    @Query("UPDATE habits SET isActive = 0 WHERE id = :habitId")
    suspend fun deactivateHabit(habitId: Long)
    
    // Habit Completions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitCompletion(completion: HabitCompletion)
    
    @Delete
    suspend fun deleteHabitCompletion(completion: HabitCompletion)
    
    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId")
    fun getCompletionsForHabit(habitId: Long): Flow<List<HabitCompletion>>
    
    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId AND completionDate = :date")
    suspend fun getCompletionForHabitOnDate(habitId: Long, date: LocalDate): HabitCompletion?
    
    @Query("SELECT * FROM habit_completions WHERE completionDate = :date")
    suspend fun getCompletionsForDate(date: LocalDate): List<HabitCompletion>
    
    // Habit with Completions
    @Transaction
    @Query("SELECT * FROM habits WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getHabitsWithCompletions(): Flow<List<HabitWithCompletions>>
    
    @Transaction
    @Query("SELECT * FROM habits WHERE id = :habitId")
    suspend fun getHabitWithCompletions(habitId: Long): HabitWithCompletions?
    
    // Statistics queries
    @Query("SELECT COUNT(*) FROM habits WHERE isActive = 1")
    suspend fun getTotalActiveHabits(): Int
    
    @Query("SELECT COUNT(*) FROM habit_completions WHERE completionDate = :date")
    suspend fun getCompletionsCountsForDate(date: LocalDate): Int
    
    @Query("SELECT COUNT(*) FROM habit_completions WHERE habitId = :habitId")
    suspend fun getTotalCompletionsForHabit(habitId: Long): Int
    
    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId ORDER BY completionDate DESC LIMIT 1")
    suspend fun getLastCompletionForHabit(habitId: Long): HabitCompletion?
}

