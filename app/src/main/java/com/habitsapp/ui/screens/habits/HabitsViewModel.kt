package com.habitsapp.ui.screens.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habitsapp.data.database.HabitDao
import com.habitsapp.data.model.Habit
import com.habitsapp.data.model.HabitCompletion
import com.habitsapp.data.model.HabitWithCompletions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val habitDao: HabitDao
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val habits: StateFlow<List<HabitWithCompletions>> = habitDao.getHabitsWithCompletions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleHabitCompletion(habitId: Long) {
        viewModelScope.launch {
            try {
                val today = LocalDate.now()
                val existingCompletion = habitDao.getCompletionForHabitOnDate(habitId, today)

                if (existingCompletion != null) {
                    // Ya está completado, lo eliminamos
                    habitDao.deleteHabitCompletion(existingCompletion)
                } else {
                    // No está completado, lo agregamos
                    val completion = HabitCompletion(
                        habitId = habitId,
                        completionDate = today,
                        notes = null
                    )
                    habitDao.insertHabitCompletion(completion)
                }
            } catch (e: Exception) {
                _error.value = "Error al actualizar el hábito: ${e.message}"
            }
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            try {
                habitDao.deleteHabit(habit)
            } catch (e: Exception) {
                _error.value = "Error al eliminar el hábito: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}