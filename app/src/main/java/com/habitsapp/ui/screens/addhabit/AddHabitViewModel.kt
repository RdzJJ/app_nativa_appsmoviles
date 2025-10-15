package com.habitsapp.ui.screens.addhabit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habitsapp.data.model.HabitFrequency
import com.habitsapp.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

data class AddHabitUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val nameError: String? = null
)

@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AddHabitUiState())
    val uiState: StateFlow<AddHabitUiState> = _uiState.asStateFlow()
    
    fun createHabit(
        name: String,
        description: String?,
        frequency: HabitFrequency,
        reminderTime: LocalTime?
    ) {
        if (name.isBlank()) {
            _uiState.value = _uiState.value.copy(nameError = "El nombre del hábito es requerido")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, nameError = null)
            
            try {
                repository.insertHabit(
                    com.habitsapp.data.model.Habit(
                        name = name.trim(),
                        description = description?.trim(),
                        frequency = frequency,
                        reminderTime = reminderTime
                    )
                )
                _uiState.value = _uiState.value.copy(isSuccess = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    nameError = "Error al crear el hábito: ${e.message}"
                )
            }
        }
    }
}

