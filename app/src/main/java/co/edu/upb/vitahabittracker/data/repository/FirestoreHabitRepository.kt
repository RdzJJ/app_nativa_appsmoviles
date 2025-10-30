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
                                            finishDate = doc.getString("finishDate"),
                                            scheduledWeekday =
                                                    doc.getLong("scheduledWeekday")?.toInt(),
                                            scheduledMonthday =
                                                    doc.getLong("scheduledMonthday")?.toInt(),
                                            weeklyGoal = doc.getLong("weeklyGoal")?.toInt() ?: 7,
                                            monthlyGoal = doc.getLong("monthlyGoal")?.toInt()
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
                            finishDate = doc.getString("finishDate"),
                            scheduledWeekday = doc.getLong("scheduledWeekday")?.toInt(),
                            scheduledMonthday = doc.getLong("scheduledMonthday")?.toInt(),
                            weeklyGoal = doc.getLong("weeklyGoal")?.toInt() ?: 7,
                            monthlyGoal = doc.getLong("monthlyGoal")?.toInt()
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
            // Build a safe map with only non-null values
            val habitData = mutableMapOf<String, Any>().apply {
                put("name", habit.name)
                put("description", habit.description)
                put("frequency", habit.frequency.name)
                put("color", habit.color)
                put("icon", habit.icon)
                put("createdAt", habit.createdAt.toString())
                put("isActive", habit.isActive)
                
                habit.reminderTime?.let { put("reminderTime", it) }
                habit.finishDate?.let { put("finishDate", it) }
                habit.scheduledWeekday?.let { put("scheduledWeekday", it) }
                habit.scheduledMonthday?.let { put("scheduledMonthday", it) }
                
                put("weeklyGoal", habit.weeklyGoal)
                
                habit.monthlyGoal?.let { put("monthlyGoal", it) }
            }

            val docRef = habitsCollection.document(habit.id.toString())
            docRef.set(habitData).await()

            Result.success(habit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateHabit(habit: Habit): Result<Habit> {
        return try {
            // Build a safe map with only non-null values
            val habitData = mutableMapOf<String, Any>().apply {
                put("name", habit.name)
                put("description", habit.description)
                put("frequency", habit.frequency.name)
                put("color", habit.color)
                put("icon", habit.icon)
                put("isActive", habit.isActive)
                
                habit.reminderTime?.let { put("reminderTime", it) }
                habit.finishDate?.let { put("finishDate", it) }
                habit.scheduledWeekday?.let { put("scheduledWeekday", it) }
                habit.scheduledMonthday?.let { put("scheduledMonthday", it) }
                
                put("weeklyGoal", habit.weeklyGoal)
                
                habit.monthlyGoal?.let { put("monthlyGoal", it) }
            }

            habitsCollection
                    .document(habit.id.toString())
                    .update(habitData)
                    .await()
            Result.success(habit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteHabit(habitId: Int): Result<Unit> {
        return try {
            // Hard delete - permanently remove from Firestore
            habitsCollection.document(habitId.toString()).delete().await()
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
