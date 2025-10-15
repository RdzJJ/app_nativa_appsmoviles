package com.habitsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habitsapp.data.model.Habit
import com.habitsapp.data.model.HabitFrequency
import com.habitsapp.data.model.HabitWithCompletions
import com.habitsapp.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {
    
    private val _habits = MutableStateFlow<List<HabitWithCompletions>>(emptyList())
    val habits: StateFlow<List<HabitWithCompletions>> = _habits.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadHabits()
    }
    
    private fun loadHabits() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getAllHabitsWithCompletions().collect { habitsList ->
                    _habits.value = habitsList
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun createHabit(
        name: String,
        description: String?,
        frequency: HabitFrequency,
        reminderTime: LocalTime?
    ) {
        viewModelScope.launch {
            try {
                val habit = Habit(
                    name = name,
                    description = description,
                    frequency = frequency,
                    reminderTime = reminderTime
                )
                repository.insertHabit(habit)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    fun updateHabit(habit: Habit) {
        viewModelScope.launch {
            try {
                repository.updateHabit(habit)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            try {
                repository.deleteHabit(habit)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    fun toggleHabitCompletion(habitId: Long, date: LocalDate = LocalDate.now()) {
        viewModelScope.launch {
            try {
                val isCompleted = repository.isHabitCompletedOnDate(habitId, date)
                if (isCompleted) {
                    repository.markHabitIncomplete(habitId, date)
                } else {
                    repository.markHabitCompleted(habitId, date)
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}

