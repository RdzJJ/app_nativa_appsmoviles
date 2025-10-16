package co.edu.upb.vitahabittracker.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import co.edu.upb.vitahabittracker.data.models.Habit
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Insert
    suspend fun insert(habit: Habit): Long

    @Update
    suspend fun update(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

    @Query("SELECT * FROM habits WHERE id = :habitId")
    suspend fun getHabitById(habitId: Int): Habit?

    @Query("SELECT * FROM habits WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getAllActiveHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE isActive = 0 ORDER BY createdAt DESC")
    fun getAllInactiveHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("DELETE FROM habits WHERE id = :habitId")
    suspend fun deleteById(habitId: Int)
}
