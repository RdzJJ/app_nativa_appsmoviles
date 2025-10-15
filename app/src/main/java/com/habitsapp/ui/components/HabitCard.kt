package com.habitsapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.habitsapp.data.model.Habit
import com.habitsapp.data.model.HabitWithCompletions
import com.habitsapp.ui.theme.HabitCompleted
import com.habitsapp.ui.theme.HabitPending

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitCard(
    habitWithCompletions: HabitWithCompletions,
    onToggleCompletion: (Long) -> Unit,
    onEditHabit: (Long) -> Unit,
    onDeleteHabit: (com.habitsapp.data.model.Habit) -> Unit,
    modifier: Modifier = Modifier
) {
    val habit = habitWithCompletions.habit
    val isCompleted = habitWithCompletions.isCompletedToday

    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = habit.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    habit.description?.let { description ->
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = { onToggleCompletion(habit.id) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = if (isCompleted) "Marcar como incompleto" else "Marcar como completado",
                        tint = if (isCompleted) HabitCompleted else HabitPending
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                HabitStats(
                    currentStreak = habitWithCompletions.currentStreak,
                    completionRate = habitWithCompletions.completionRate,
                    frequency = habit.frequency.displayName
                )

                Row {
                    IconButton(
                        onClick = { onEditHabit(habit.id) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar hábito",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(
                        onClick = { showDeleteDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar hábito",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar hábito") },
            text = { Text("¿Estás seguro de que quieres eliminar este hábito?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteHabit(habit)
                        showDeleteDialog = false
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun HabitStats(
    currentStreak: Int,
    completionRate: Float,
    frequency: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Racha: $currentStreak días",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Cumplimiento: ${completionRate.toInt()}%",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = frequency,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}