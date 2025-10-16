package co.edu.upb.vitahabittracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.upb.vitahabittracker.R
import co.edu.upb.vitahabittracker.data.models.Habit
import co.edu.upb.vitahabittracker.ui.theme.BluePrimary
import co.edu.upb.vitahabittracker.ui.theme.GreenPrimary

@Composable
fun HomeScreen(
    habits: List<Habit> = emptyList(),
    onAddHabitClick: () -> Unit,
    onHabitClick: (Habit) -> Unit,
    onDeleteHabit: (Habit) -> Unit,
    onCompleteHabit: (Habit) -> Unit,
    completedHabitsToday: Set<Int> = emptySet()
) {
    // Estado para controlar el diálogo de confirmación
    var habitToDelete by remember { mutableStateOf<Habit?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Box(
        modifier =
            Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier =
                Modifier.fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(
                        top = 16.dp,
                        bottom = 96.dp
                    ) // Space for nav bar (80dp) + extra spacing
        ) {
            // Header
            Text(
                text = stringResource(R.string.home_title),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Hoy es ${java.time.LocalDate.now()}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Habits List or Empty State
            if (habits.isEmpty()) {
                // Empty State
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp).padding(bottom = 16.dp),
                        tint = GreenPrimary.copy(alpha = 0.3f)
                    )

                    Text(
                        text = stringResource(R.string.no_habits),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = stringResource(R.string.no_habits_description),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    Button(
                        onClick = onAddHabitClick,
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(stringResource(R.string.add_habit))
                    }
                }
            } else {
                // Habits List
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(habits) { habit ->
                        HabitCard(
                            habit = habit,
                            onClick = { onHabitClick(habit) },
                            onDelete = {
                                habitToDelete = habit
                                showDeleteDialog = true
                            },
                            onComplete = { onCompleteHabit(habit) },
                            isCompletedToday = completedHabitsToday.contains(habit.id)
                        )
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }

        // FAB
        FloatingActionButton(
            onClick = onAddHabitClick,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = GreenPrimary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) { Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_habit)) }
    }

    // Diálogo de confirmación de eliminación
    if (showDeleteDialog && habitToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                habitToDelete = null
            },
            icon = {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = {
                Text(
                    text = stringResource(R.string.delete_habit_title),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = stringResource(
                        R.string.delete_habit_message,
                        habitToDelete!!.name
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteHabit(habitToDelete!!)
                        showDeleteDialog = false
                        habitToDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        habitToDelete = null
                    }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun HabitCard(
    habit: Habit,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onComplete: () -> Unit,
    isCompletedToday: Boolean
) {
    Card(
        modifier =
            Modifier.fillMaxWidth()
                .padding(bottom = 12.dp)
                .alpha(if (isCompletedToday) 0.6f else 1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                Text(
                    text = habit.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (isCompletedToday) TextDecoration.LineThrough else null
                )

                if (habit.description.isNotEmpty()) {
                    Text(
                        text = habit.description,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Chip(
                        label = { Text(habit.frequency.name, fontSize = 12.sp) },
                        modifier = Modifier.height(28.dp)
                    )

                    Text(
                        text = "0 días",
                        fontSize = 12.sp,
                        color = BluePrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(80.dp)
            ) {
                IconButton(
                    onClick = onComplete,
                    modifier =
                        Modifier.size(48.dp)
                            .background(
                                color =
                                    if (isCompletedToday) GreenPrimary
                                    else GreenPrimary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                ) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = stringResource(R.string.completed_today),
                        tint =
                            if (isCompletedToday) MaterialTheme.colorScheme.onPrimary
                            else GreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.delete),
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun Chip(label: @Composable () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier =
            modifier.background(
                color = BluePrimary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) { label() }
    }
}