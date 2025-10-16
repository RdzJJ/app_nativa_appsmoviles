package co.edu.upb.vitahabittracker.data.repository

import co.edu.upb.vitahabittracker.data.models.User
import kotlinx.coroutines.delay

class AuthRepository {
    suspend fun login(email: String, password: String): Result<User> {
        return try {
            // Simulate API call
            delay(1000)
            
            // Dummy validation
            if (email.isEmpty() || password.isEmpty()) {
                Result.failure(Exception("Email and password are required"))
            } else if (!email.contains("@")) {
                Result.failure(Exception("Invalid email format"))
            } else if (password.length < 6) {
                Result.failure(Exception("Password must be at least 6 characters"))
            } else {
                // Dummy user creation - in real app this would come from Firebase
                val user = User(
                    id = "dummy_${System.currentTimeMillis()}",
                    name = email.substringBefore("@"),
                    email = email,
                    bio = "Nuevo usuario de Vita Habitos",
                    joinDate = java.time.LocalDate.now().toString()
                )
                Result.success(user)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signup(email: String, password: String, name: String): Result<User> {
        return try {
            // Simulate API call
            delay(1500)
            
            // Dummy validation
            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Result.failure(Exception("All fields are required"))
            } else if (!email.contains("@")) {
                Result.failure(Exception("Invalid email format"))
            } else if (password.length < 6) {
                Result.failure(Exception("Password must be at least 6 characters"))
            } else {
                // Dummy user creation
                val user = User(
                    id = "dummy_${System.currentTimeMillis()}",
                    name = name,
                    email = email,
                    bio = "Nuevo usuario de Vita Habitos",
                    joinDate = java.time.LocalDate.now().toString()
                )
                Result.success(user)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> {
        return try {
            delay(500)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
