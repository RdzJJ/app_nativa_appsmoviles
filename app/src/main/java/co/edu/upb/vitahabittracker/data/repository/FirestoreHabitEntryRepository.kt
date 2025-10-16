package co.edu.upb.vitahabittracker.data.repository

import co.edu.upb.vitahabittracker.data.models.HabitEntry
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.time.LocalDate
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreHabitEntryRepository(private val userId: String) {
    private val firestore = FirebaseFirestore.getInstance()
    private val entriesCollection =
            firestore.collection("users").document(userId).collection("habitEntries")

    // Real-time flow of habit entries for the current user
    fun getHabitEntriesFlow(): Flow<List<HabitEntry>> = callbackFlow {
        val listener: ListenerRegistration =
                entriesCollection.addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    val entries =
                            snapshot?.documents?.mapNotNull { doc ->
                                try {
                                    HabitEntry(
                                            id = doc.id.toIntOrNull() ?: 0,
                                            habitId = doc.getLong("habitId")?.toInt() ?: 0,
                                            completedDate =
                                                    LocalDate.parse(
                                                            doc.getString("completedDate")
                                                                    ?: LocalDate.now().toString()
                                                    ),
                                            completedTime = doc.getString("completedTime") ?: "",
                                            notes = doc.getString("notes") ?: ""
                                    )
                                } catch (e: Exception) {
                                    null
                                }
                            }
                                    ?: emptyList()

                    trySend(entries)
                }

        awaitClose { listener.remove() }
    }

    suspend fun getHabitEntries(): List<HabitEntry> {
        return try {
            val snapshot = entriesCollection.get().await()
            snapshot.documents.mapNotNull { doc ->
                try {
                    HabitEntry(
                            id = doc.id.toIntOrNull() ?: 0,
                            habitId = doc.getLong("habitId")?.toInt() ?: 0,
                            completedDate =
                                    LocalDate.parse(
                                            doc.getString("completedDate")
                                                    ?: LocalDate.now().toString()
                                    ),
                            completedTime = doc.getString("completedTime") ?: "",
                            notes = doc.getString("notes") ?: ""
                    )
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getEntriesForHabit(habitId: Int): List<HabitEntry> {
        return try {
            val snapshot = entriesCollection.whereEqualTo("habitId", habitId).get().await()

            snapshot.documents.mapNotNull { doc ->
                try {
                    HabitEntry(
                            id = doc.id.toIntOrNull() ?: 0,
                            habitId = doc.getLong("habitId")?.toInt() ?: 0,
                            completedDate =
                                    LocalDate.parse(
                                            doc.getString("completedDate")
                                                    ?: LocalDate.now().toString()
                                    ),
                            completedTime = doc.getString("completedTime") ?: "",
                            notes = doc.getString("notes") ?: ""
                    )
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addHabitEntry(entry: HabitEntry): Result<HabitEntry> {
        return try {
            val entryData =
                    hashMapOf(
                            "habitId" to entry.habitId,
                            "completedDate" to entry.completedDate.toString(),
                            "completedTime" to entry.completedTime,
                            "notes" to entry.notes
                    )

            val docRef = entriesCollection.document(entry.id.toString())
            docRef.set(entryData).await()

            Result.success(entry)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteHabitEntry(entryId: Int): Result<Unit> {
        return try {
            entriesCollection.document(entryId.toString()).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteEntriesForHabit(habitId: Int): Result<Unit> {
        return try {
            val snapshot = entriesCollection.whereEqualTo("habitId", habitId).get().await()
            snapshot.documents.forEach { doc -> doc.reference.delete().await() }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
