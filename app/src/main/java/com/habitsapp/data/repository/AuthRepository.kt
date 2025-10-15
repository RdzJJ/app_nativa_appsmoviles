package com.habitsapp.data.repository

import com.habitsapp.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Singleton

@Singleton
class AuthRepository {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    // Dummy login - will be replaced with Firebase
    suspend fun login(email: String, password: String): Result<User> {
        return try {
            // Simulate network delay
            kotlinx.coroutines.delay(1000)
            
            // Create a dummy user for now
            val user = User(
                id = email.hashCode().toString(),
                email = email,
                fullName = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                phone = "+34 612 345 678",
                bio = "Amante de los hábitos saludables",
                totalHabitsCreated = 0,
                bestStreak = 0
            )
            
            _currentUser.value = user
            _isLoggedIn.value = true
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Dummy signup - will be replaced with Firebase
    suspend fun signup(email: String, password: String, fullName: String): Result<User> {
        return try {
            // Simulate network delay
            kotlinx.coroutines.delay(1000)
            
            // Create a new dummy user
            val user = User(
                id = email.hashCode().toString(),
                email = email,
                fullName = fullName,
                phone = "",
                bio = "",
                totalHabitsCreated = 0,
                bestStreak = 0
            )
            
            _currentUser.value = user
            _isLoggedIn.value = true
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        _currentUser.value = null
        _isLoggedIn.value = false
    }

    suspend fun updateProfile(user: User): Result<User> {
        return try {
            _currentUser.value = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUser(): User? = _currentUser.value
}
