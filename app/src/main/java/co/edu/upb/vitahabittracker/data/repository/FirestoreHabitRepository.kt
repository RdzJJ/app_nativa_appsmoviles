package co.edu.upb.vitahabittracker.data.repository

import co.edu.upb.vitahabittracker.data.models.Habit
import co.edu.upb.vitahabittracker.data.models.HabitFrequency
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.time.LocalDateTime
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreHabitRepository(private val userId: String) {
    private val firestore = FirebaseFirestore.getInstance()
    private val habitsCollection =
            firestore.collection("users").document(userId).collection("habits")

    // Real-time flow of habits for the current user
    fun getHabitsFlow(): Flow<List<Habit>> = callbackFlow {
        val listener: ListenerRegistration =
                habitsCollection.whereEqualTo("isActive", true).addSnapshotListener {
                        snapshot,
                        error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    val habits =
                            snapshot?.documents?.mapNotNull { doc ->
                                try {
                                    Habit(
                                            id = doc.id.toIntOrNull() ?: 0,
                                            name = doc.getString("name") ?: "",
                                            description = doc.getString("description") ?: "",
                                            frequency =
                                                    HabitFrequency.valueOf(
                                                            doc.getString("frequency") ?: "DAILY"
                                                    ),
                                            color = doc.getString("color") ?: "#4CAF50",
                                            icon = doc.getString("icon") ?: "✓",
                                            createdAt =
                                                    LocalDateTime.parse(
                                                            doc.getString("createdAt")
                                                                    ?: LocalDateTime.now()
                                                                            .toString()
                                                    ),
                                            isActive = doc.getBoolean("isActive") ?: true,
                                            reminderTime = doc.getString("reminderTime"),
                                            finishDate = doc.getString("finishDate")
                                    )
                                } catch (e: Exception) {
                                    null
                                }
                            }
                                    ?: emptyList()

                    trySend(habits)
                }

        awaitClose { listener.remove() }
    }

    suspend fun getHabits(): List<Habit> {
        return try {
            val snapshot = habitsCollection.whereEqualTo("isActive", true).get().await()
            snapshot.documents.mapNotNull { doc ->
                try {
                    Habit(
                            id = doc.id.toIntOrNull() ?: 0,
                            name = doc.getString("name") ?: "",
                            description = doc.getString("description") ?: "",
                            frequency =
                                    HabitFrequency.valueOf(doc.getString("frequency") ?: "DAILY"),
                            color = doc.getString("color") ?: "#4CAF50",
                            icon = doc.getString("icon") ?: "✓",
                            createdAt =
                                    LocalDateTime.parse(
                                            doc.getString("createdAt")
                                                    ?: LocalDateTime.now().toString()
                                    ),
                            isActive = doc.getBoolean("isActive") ?: true,
                            reminderTime = doc.getString("reminderTime"),
                            finishDate = doc.getString("finishDate")
                    )
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addHabit(habit: Habit): Result<Habit> {
        return try {
            val habitData =
                    hashMapOf(
                            "name" to habit.name,
                            "description" to habit.description,
                            "frequency" to habit.frequency.name,
                            "color" to habit.color,
                            "icon" to habit.icon,
                            "createdAt" to habit.createdAt.toString(),
                            "isActive" to habit.isActive,
                            "reminderTime" to habit.reminderTime,
                            "finishDate" to habit.finishDate
                    )

            val docRef = habitsCollection.document(habit.id.toString())
            docRef.set(habitData).await()

            Result.success(habit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun updateHabit(habit: Habit): Result<Habit> {
        return try {
            val habitData =
                    hashMapOf(
                            "name" to habit.name,
                            "description" to habit.description,
                            "frequency" to habit.frequency.name,
                            "color" to habit.color,
                            "icon" to habit.icon,
                            "isActive" to habit.isActive,
                            "reminderTime" to habit.reminderTime,
                            "finishDate" to habit.finishDate
                    )

            habitsCollection
                    .document(habit.id.toString())
                    .update(habitData as Map<String, Any>)
                    .await()
            Result.success(habit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteHabit(habitId: Int): Result<Unit> {
        return try {
            // Soft delete - mark as inactive
            habitsCollection.document(habitId.toString()).update("isActive", false).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun hardDeleteHabit(habitId: Int): Result<Unit> {
        return try {
            habitsCollection.document(habitId.toString()).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
