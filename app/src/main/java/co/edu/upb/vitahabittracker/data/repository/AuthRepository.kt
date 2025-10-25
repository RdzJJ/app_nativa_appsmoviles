package co.edu.upb.vitahabittracker.data.repository

import co.edu.upb.vitahabittracker.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.regex.Pattern

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    
    companion object {
        // RFC 5322 compliant email regex pattern
        private const val EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
        
        /**
         * Validates an email address format using regex pattern
         * @param email The email address to validate
         * @return true if the email is valid, false otherwise
         */
        fun isValidEmail(email: String): Boolean {
            if (email.isBlank()) return false
            
            val pattern = Pattern.compile(EMAIL_PATTERN)
            val matcher = pattern.matcher(email)
            
            return matcher.matches()
        }
    }

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            if (email.isEmpty() || password.isEmpty()) {
                return Result.failure(Exception("El correo y la contraseña son obligatorios"))
            }
            if (!isValidEmail(email)) {
                return Result.failure(Exception("Formato de correo inválido"))
            }
            if (password.length < 6) {
                return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
            }

            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Error al iniciar sesión")

            // Get user data from Firestore
            val userDoc = firestore.collection("users").document(firebaseUser.uid).get().await()

            val user =
                    if (userDoc.exists()) {
                        User(
                                id = firebaseUser.uid,
                                name = userDoc.getString("name")
                                                ?: firebaseUser.displayName
                                                        ?: email.substringBefore("@"),
                                email = firebaseUser.email ?: email,
                                joinDate = userDoc.getString("joinDate")
                                                ?: java.time.LocalDate.now().toString()
                        )
                    } else {
                        // Create user document if it doesn't exist
                        val newUser =
                                User(
                                        id = firebaseUser.uid,
                                        name = firebaseUser.displayName
                                                        ?: email.substringBefore("@"),
                                        email = firebaseUser.email ?: email,
                                        joinDate = java.time.LocalDate.now().toString()
                                )
                        saveUserToFirestore(newUser)
                        newUser
                    }

            Result.success(user)
        } catch (e: Exception) {
            val errorMessage =
                    when {
                        e.message?.contains("password", ignoreCase = true) == true ||
                                e.message?.contains(
                                        "INVALID_LOGIN_CREDENTIALS",
                                        ignoreCase = true
                                ) == true ||
                                e.message?.contains("wrong-password", ignoreCase = true) == true ||
                                e.message?.contains("user-not-found", ignoreCase = true) == true ||
                                e.message?.contains("invalid-credential", ignoreCase = true) ==
                                        true -> "Correo o contraseña incorrectos"
                        e.message?.contains("network", ignoreCase = true) == true ->
                                "Error de conexión. Verifica tu internet"
                        e.message?.contains("too-many-requests", ignoreCase = true) == true ->
                                "Demasiados intentos. Intenta más tarde"
                        e.message?.contains("user-disabled", ignoreCase = true) == true ->
                                "Esta cuenta ha sido deshabilitada"
                        else -> "Correo o contraseña incorrectos"
                    }
            Result.failure(Exception(errorMessage))
        }
    }

    suspend fun signup(email: String, password: String, name: String): Result<User> {
        return try {
            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                return Result.failure(Exception("Todos los campos son obligatorios"))
            }
            if (!isValidEmail(email)) {
                return Result.failure(Exception("Formato de correo inválido"))
            }
            if (password.length < 6) {
                return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
            }

            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Error al crear la cuenta")

            // Update Firebase Auth profile
            val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(name).build()
            firebaseUser.updateProfile(profileUpdates).await()

            // Create user in Firestore
            val user =
                    User(
                            id = firebaseUser.uid,
                            name = name,
                            email = firebaseUser.email ?: email,
                            joinDate = java.time.LocalDate.now().toString()
                    )

            saveUserToFirestore(user)
            Result.success(user)
        } catch (e: Exception) {
            val errorMessage =
                    when {
                        e.message?.contains("email-already-in-use", ignoreCase = true) == true ||
                                e.message?.contains("already in use", ignoreCase = true) == true ->
                                "Este correo ya está registrado"
                        e.message?.contains("invalid-email", ignoreCase = true) == true ->
                                "Correo electrónico inválido"
                        e.message?.contains("weak-password", ignoreCase = true) == true ->
                                "La contraseña es muy débil"
                        e.message?.contains("network", ignoreCase = true) == true ->
                                "Error de conexión. Verifica tu internet"
                        e.message?.contains("too-many-requests", ignoreCase = true) == true ->
                                "Demasiados intentos. Intenta más tarde"
                        else -> "Error al crear la cuenta. Intenta de nuevo"
                    }
            Result.failure(Exception(errorMessage))
        }
    }

    private suspend fun saveUserToFirestore(user: User) {
        val userData =
                hashMapOf(
                        "name" to user.name,
                        "email" to user.email,
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
                joinDate = ""
        )
    }

    suspend fun updateUserProfile(newName: String): Result<Unit> {
        return try {
            if (newName.isEmpty()) {
                return Result.failure(Exception("El nombre no puede estar vacío"))
            }

            val firebaseUser = auth.currentUser ?: throw Exception("No hay usuario autenticado")

            // Update Firebase Auth display name
            val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(newName).build()
            firebaseUser.updateProfile(profileUpdates).await()

            // Update Firestore
            firestore.collection("users").document(firebaseUser.uid).update(mapOf("name" to newName)).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> {
        return try {
            if (currentPassword.isEmpty() || newPassword.isEmpty()) {
                return Result.failure(Exception("Todos los campos son obligatorios"))
            }
            if (newPassword.length < 6) {
                return Result.failure(Exception("La nueva contraseña debe tener al menos 6 caracteres"))
            }
            if (currentPassword == newPassword) {
                return Result.failure(Exception("La nueva contraseña debe ser diferente a la actual"))
            }

            val firebaseUser = auth.currentUser ?: throw Exception("No hay usuario autenticado")
            val email = firebaseUser.email ?: throw Exception("No se pudo obtener el correo del usuario")

            // Re-authenticate with current password
            val credential = com.google.firebase.auth.EmailAuthProvider.getCredential(email, currentPassword)
            firebaseUser.reauthenticate(credential).await()

            // Update password
            firebaseUser.updatePassword(newPassword).await()

            Result.success(Unit)
        } catch (e: Exception) {
            val errorMessage = when {
                e.message?.contains("wrong-password", ignoreCase = true) == true ||
                        e.message?.contains("INVALID_LOGIN_CREDENTIALS", ignoreCase = true) == true ->
                    "La contraseña actual es incorrecta"
                e.message?.contains("weak-password", ignoreCase = true) == true ->
                    "La nueva contraseña es muy débil"
                e.message?.contains("network", ignoreCase = true) == true ->
                    "Error de conexión. Verifica tu internet"
                else -> e.message ?: "Error al cambiar la contraseña"
            }
            Result.failure(Exception(errorMessage))
        }
    }
}
