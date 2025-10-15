package com.habitsapp.ui.screens.edithabit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habitsapp.data.model.Habit
import com.habitsapp.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditHabitUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val habit: Habit? = null,
    val nameError: String? = null
)

@HiltViewModel
class EditHabitViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(EditHabitUiState())
    val uiState: StateFlow<EditHabitUiState> = _uiState.asStateFlow()
    
    fun loadHabit(habitId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val habit = repository.getHabitById(habitId)
                _uiState.value = _uiState.value.copy(
                    habit = habit,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    nameError = "Error al cargar el hábito: ${e.message}"
                )
            }
        }
    }
    
    fun updateHabit(habit: Habit) {
        if (habit.name.isBlank()) {
            _uiState.value = _uiState.value.copy(nameError = "El nombre del hábito es requerido")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, nameError = null)
            
            try {
                repository.updateHabit(habit)
                _uiState.value = _uiState.value.copy(isSuccess = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    nameError = "Error al actualizar el hábito: ${e.message}"
                )
            }
        }
    }
}

