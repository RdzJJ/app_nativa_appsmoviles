package com.habitsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habitsapp.data.model.User
import com.habitsapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        // Collect auth repository state
        viewModelScope.launch {
            authRepository.isLoggedIn.collect { isLoggedIn ->
                _isLoggedIn.value = isLoggedIn
            }
        }
        
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                _currentUser.value = user
            }
        }
    }

    fun login(email: String, password: String) {
        if (validateEmail(email)) {
            if (validatePassword(password)) {
                viewModelScope.launch {
                    _isLoading.value = true
                    _error.value = null
                    
                    val result = authRepository.login(email, password)
                    
                    result.onSuccess { user ->
                        _currentUser.value = user
                        _isLoggedIn.value = true
                        _isLoading.value = false
                    }.onFailure { exception ->
                        _error.value = exception.message
                        _isLoading.value = false
                    }
                }
            } else {
                _error.value = "La contraseña debe tener al menos 6 caracteres"
            }
        } else {
            _error.value = "Correo electrónico inválido"
        }
    }

    fun signup(email: String, password: String, confirmPassword: String, fullName: String) {
        if (validateEmail(email)) {
            if (validatePassword(password)) {
                if (password == confirmPassword) {
                    viewModelScope.launch {
                        _isLoading.value = true
                        _error.value = null
                        
                        val result = authRepository.signup(email, password, fullName)
                        
                        result.onSuccess { user ->
                            _currentUser.value = user
                            _isLoggedIn.value = true
                            _isLoading.value = false
                        }.onFailure { exception ->
                            _error.value = exception.message
                            _isLoading.value = false
                        }
                    }
                } else {
                    _error.value = "Las contraseñas no coinciden"
                }
            } else {
                _error.value = "La contraseña debe tener al menos 6 caracteres"
            }
        } else {
            _error.value = "Correo electrónico inválido"
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _isLoggedIn.value = false
            _currentUser.value = null
        }
    }

    fun updateProfile(user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.updateProfile(user)
            
            result.onSuccess {
                _currentUser.value = it
                _isLoading.value = false
            }.onFailure { exception ->
                _error.value = exception.message
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    private fun validateEmail(email: String): Boolean {
        return email.isNotEmpty() && email.contains("@") && email.contains(".")
    }

    private fun validatePassword(password: String): Boolean {
        return password.length >= 6
    }
}
