package com.habitsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habitsapp.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class StatisticsData(
    val totalHabits: Int = 0,
    val completedToday: Int = 0,
    val totalCompletions: Int = 0,
    val averageCompletionRate: Float = 0f,
    val longestStreak: Int = 0
)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {
    
    private val _statistics = MutableStateFlow(StatisticsData())
    val statistics: StateFlow<StatisticsData> = _statistics.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadStatistics()
    }
    
    private fun loadStatistics() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val totalHabits = repository.getTotalActiveHabits()
                val completedToday = repository.getCompletionsCountForDate(LocalDate.now())
                
                // Calculate average completion rate and longest streak
                val habits = repository.getAllHabitsWithCompletions()
                habits.collect { habitsList ->
                    val totalCompletions = habitsList.sumOf { it.completions.size }
                    val averageCompletionRate = if (habitsList.isNotEmpty()) {
                        habitsList.map { it.completionRate }.average().toFloat()
                    } else 0f
                    val longestStreak = habitsList.maxOfOrNull { it.longestStreak } ?: 0
                    
                    _statistics.value = StatisticsData(
                        totalHabits = totalHabits,
                        completedToday = completedToday,
                        totalCompletions = totalCompletions,
                        averageCompletionRate = averageCompletionRate,
                        longestStreak = longestStreak
                    )
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun refreshStatistics() {
        loadStatistics()
    }
}

