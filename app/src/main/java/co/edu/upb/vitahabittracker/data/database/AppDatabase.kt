package co.edu.upb.vitahabittracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import co.edu.upb.vitahabittracker.data.models.Habit
import co.edu.upb.vitahabittracker.data.models.HabitEntry
import co.edu.upb.vitahabittracker.data.models.Reminder

@Database(
    entities = [Habit::class, HabitEntry::class, Reminder::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitEntryDao(): HabitEntryDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vita_habitos_database"
                ).build()
                instance = newInstance
                newInstance
            }
        }
    }
}
