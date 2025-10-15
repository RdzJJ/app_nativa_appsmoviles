package com.habitsapp.data.model

data class User(
    val id: String = "",
    val email: String = "",
    val fullName: String = "",
    val phone: String = "",
    val bio: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val totalHabitsCreated: Int = 0,
    val bestStreak: Int = 0
)

// Data class for authentication state
data class AuthState(
    val isLoggedIn: Boolean = false,
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
