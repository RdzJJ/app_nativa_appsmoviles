package com.habitsapp.di

import android.content.Context
import androidx.room.Room
import com.habitsapp.data.database.HabitDao
import com.habitsapp.data.database.HabitsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideHabitsDatabase(@ApplicationContext context: Context): HabitsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            HabitsDatabase::class.java,
            "habits_database"
        ).build()
    }
    
    @Provides
    fun provideHabitDao(database: HabitsDatabase): HabitDao {
        return database.habitDao()
    }
}

