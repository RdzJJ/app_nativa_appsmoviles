package com.habitsapp.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.habitsapp.data.model.Habit
import com.habitsapp.data.model.HabitCompletion
import java.time.LocalDate


@Database(
    entities = [Habit::class, HabitCompletion::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HabitsDatabase : RoomDatabase() {
    
    abstract fun habitDao(): HabitDao
    
    companion object {
        @Volatile
        private var INSTANCE: HabitsDatabase? = null
        
        fun getDatabase(context: Context): HabitsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HabitsDatabase::class.java,
                    "habits_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

