package co.edu.upb.vitahabittracker.data.repository

import co.edu.upb.vitahabittracker.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            if (email.isEmpty() || password.isEmpty()) {
                return Result.failure(Exception("Email and password are required"))
            }
            if (!email.contains("@")) {
                return Result.failure(Exception("Invalid email format"))
            }
            if (password.length < 6) {
                return Result.failure(Exception("Password must be at least 6 characters"))
            }

            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Login failed")

            // Get user data from Firestore
            val userDoc = firestore.collection("users").document(firebaseUser.uid).get().await()
            
            val user = if (userDoc.exists()) {
                User(
                    id = firebaseUser.uid,
                    name = userDoc.getString("name") ?: firebaseUser.displayName ?: email.substringBefore("@"),
                    email = firebaseUser.email ?: email,
                    bio = userDoc.getString("bio") ?: "Nuevo usuario de Vita Habitos",
                    joinDate = userDoc.getString("joinDate") ?: java.time.LocalDate.now().toString()
                )
            } else {
                // Create user document if it doesn't exist
                val newUser = User(
                    id = firebaseUser.uid,
                    name = firebaseUser.displayName ?: email.substringBefore("@"),
                    email = firebaseUser.email ?: email,
                    bio = "Nuevo usuario de Vita Habitos",
                    joinDate = java.time.LocalDate.now().toString()
                )
                saveUserToFirestore(newUser)
                newUser
            }

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Error en el login"))
        }
    }

    suspend fun signup(email: String, password: String, name: String): Result<User> {
        return try {
            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                return Result.failure(Exception("All fields are required"))
            }
            if (!email.contains("@")) {
                return Result.failure(Exception("Invalid email format"))
            }
            if (password.length < 6) {
                return Result.failure(Exception("Password must be at least 6 characters"))
            }

            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Signup failed")

            // Update Firebase Auth profile
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            firebaseUser.updateProfile(profileUpdates).await()

            // Create user in Firestore
            val user = User(
                id = firebaseUser.uid,
                name = name,
                email = firebaseUser.email ?: email,
                bio = "Nuevo usuario de Vita Habitos",
                joinDate = java.time.LocalDate.now().toString()
            )
            
            saveUserToFirestore(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Error en el registro"))
        }
    }

    private suspend fun saveUserToFirestore(user: User) {
        val userData = hashMapOf(
            "name" to user.name,
            "email" to user.email,
            "bio" to user.bio,
            "joinDate" to user.joinDate
        )
        firestore.collection("users").document(user.id).set(userData).await()
    }

    suspend fun logout(): Result<Unit> {
        return try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return User(
            id = firebaseUser.uid,
            name = firebaseUser.displayName ?: "",
            email = firebaseUser.email ?: "",
            bio = "",
            joinDate = ""
        )
    }
}
