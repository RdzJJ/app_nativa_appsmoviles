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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import co.edu.upb.vitahabittracker.R
import co.edu.upb.vitahabittracker.data.models.Habit
import co.edu.upb.vitahabittracker.data.models.HabitFrequency
import co.edu.upb.vitahabittracker.ui.theme.BluePrimary
import co.edu.upb.vitahabittracker.ui.theme.GreenPrimary
import java.time.LocalDate
import java.time.DayOfWeek
import androidx.compose.foundation.clickable

@Composable
fun HomeScreen(
    habits: List<Habit> = emptyList(),
    onAddHabitClick: () -> Unit,
    onHabitClick: (Habit) -> Unit,
    onDeleteHabit: (Habit) -> Unit,
    onCompleteHabit: (Habit) -> Unit,
    completedHabitsToday: Set<Int> = emptySet(),
    userName: String = "Usuario" // Nuevo parámetro
) {
    var habitToDelete by remember { mutableStateOf<Habit?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Filter habits for today and habits for other days
    val habitsForToday = remember(habits) {
        habits.filter { isHabitScheduledForToday(it) }
    }
    
    val habitsForOtherDays = remember(habits) {
        habits.filter { !isHabitScheduledForToday(it) }
    }

    // Calculate progress for today
    val todayProgressPercentage = remember(habitsForToday, completedHabitsToday) {
        if (habitsForToday.isEmpty()) {
            0
        } else {
            val completedCount = habitsForToday.count { completedHabitsToday.contains(it.id) }
            (completedCount * 100) / habitsForToday.size
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier =
                    Modifier.fillMaxWidth()
                        .background(
                            brush =
                                Brush.horizontalGradient(
                                    listOf(
                                        GreenPrimary.copy(alpha = 0.9f),
                                        BluePrimary.copy(alpha = 0.8f)
                                    )
                                ),
                            shape =
                                RoundedCornerShape(
                                    bottomStart = 32.dp,
                                    bottomEnd = 32.dp
                                )
                        )
                        .padding(horizontal = 20.dp, vertical = 28.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.home_title),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "¡Bienvenido $userName!",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            // Progress bar for today's habits
            if (habitsForToday.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Progreso de hoy",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$todayProgressPercentage%",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenPrimary
                            )
                        }
                        
                        LinearProgressIndicator(
                            progress = todayProgressPercentage / 100f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = GreenPrimary,
                            trackColor = Color(0xFFE8E8E8)
                        )
                    }
                }
            }

            Column(
                modifier =
                    Modifier.fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp, bottom = 96.dp)
            ) {
                if (habits.isEmpty()) {
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
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = stringResource(R.string.no_habits_description),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            modifier = Modifier.padding(vertical = 8.dp)
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
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        // Hábitos para hoy
                        if (habitsForToday.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Hoy",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.padding(bottom = 12.dp, top = 8.dp)
                                )
                            }
                            items(habitsForToday) { habit ->
                                HabitCard(
                                    habit = habit,
                                    onClick = { onHabitClick(habit) },
                                    onDelete = {
                                        habitToDelete = habit
                                        showDeleteDialog = true
                                    },
                                    onComplete = { onCompleteHabit(habit) },
                                    isCompletedToday = completedHabitsToday.contains(habit.id),
                                    isAvailableToday = true
                                )
                            }
                        }
                        
                        // Hábitos para otros días
                        if (habitsForOtherDays.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Próximamente",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.padding(bottom = 12.dp, top = 16.dp)
                                )
                            }
                            items(habitsForOtherDays) { habit ->
                                HabitCard(
                                    habit = habit,
                                    onClick = { onHabitClick(habit) },
                                    onDelete = {
                                        habitToDelete = habit
                                        showDeleteDialog = true
                                    },
                                    onComplete = { onCompleteHabit(habit) },
                                    isCompletedToday = completedHabitsToday.contains(habit.id),
                                    isAvailableToday = false
                                )
                            }
                        }
                        
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onAddHabitClick,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = GreenPrimary,
            contentColor = Color.White
        ) { Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_habit)) }
    }

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
                Text(stringResource(R.string.delete_habit_title), fontWeight = FontWeight.Bold)
            },
            text = {
                Text(stringResource(R.string.delete_habit_message, habitToDelete!!.name))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteHabit(habitToDelete!!)
                        showDeleteDialog = false
                        habitToDelete = null
                    }
                ) {
                    Text(
                        stringResource(R.string.delete),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        habitToDelete = null
                    }
                ) { Text(stringResource(R.string.cancel)) }
            },
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
    isCompletedToday: Boolean,
    isAvailableToday: Boolean
) {
    Card(
        modifier =
            Modifier.fillMaxWidth()
                .padding(bottom = 12.dp)
                .alpha(if (isCompletedToday) 0.6f else 1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8E8E8))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
                .clickable { onClick() },
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
                    Chip(label = when(habit.frequency) {
                        HabitFrequency.DAILY -> "Diario"
                        HabitFrequency.WEEKLY -> "Semanal"
                        HabitFrequency.MONTHLY -> "Mensual"
                    })
                    
                    if (isAvailableToday) {
                        Text(
                            text = "Disponible hoy",
                            fontSize = 12.sp,
                            color = GreenPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    } else {
                        val daysUntilAvailable = daysUntilHabitAvailable(habit)
                        Text(
                            text = "En $daysUntilAvailable ${if (daysUntilAvailable == 1) "día" else "días"}",
                            fontSize = 12.sp,
                            color = BluePrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(80.dp)
            ) {
                IconButton(
                    onClick = onComplete,
                    enabled = isAvailableToday,
                    modifier =
                        Modifier.size(48.dp)
                            .background(
                                color =
                                    if (isCompletedToday) GreenPrimary
                                    else if (isAvailableToday) GreenPrimary.copy(alpha = 0.1f)
                                    else Color.Gray.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                ) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = stringResource(R.string.completed_today),
                        tint =
                            if (isCompletedToday) MaterialTheme.colorScheme.onPrimary
                            else if (isAvailableToday) GreenPrimary
                            else Color.Gray,
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
fun Chip(label: String) {
    Surface(
        modifier =
            Modifier.background(BluePrimary.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = BluePrimary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

fun formatDateInSpanish(date: java.time.LocalDate): String {
    val daysOfWeek =
        listOf("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")
    val months =
        listOf(
            "enero",
            "febrero",
            "marzo",
            "abril",
            "mayo",
            "junio",
            "julio",
            "agosto",
            "septiembre",
            "octubre",
            "noviembre",
            "diciembre"
        )

    val dayOfWeek = daysOfWeek[date.dayOfWeek.value % 7]
    val dayOfMonth = date.dayOfMonth
    val month = months[date.monthValue - 1]
    val year = date.year

    return "$dayOfWeek $dayOfMonth de $month de $year"
}

// Helper functions for habit scheduling

/**
 * Determines if a habit is scheduled for today
 */
fun isHabitScheduledForToday(habit: Habit): Boolean {
    val today = LocalDate.now()
    
    // Check if habit is active
    if (!habit.isActive) return false
    
    // Check creation date
    val creationDate = habit.createdAt.toLocalDate()
    if (today.isBefore(creationDate)) return false
    
    // Check finish date
    habit.finishDate?.let { finishDateStr ->
        try {
            val finishDate = LocalDate.parse(finishDateStr)
            if (today.isAfter(finishDate)) return false
        } catch (e: Exception) {
            // Ignore parsing errors
        }
    }
    
    // Check frequency
    return when (habit.frequency) {
        HabitFrequency.DAILY -> true
        HabitFrequency.WEEKLY -> {
            if (habit.scheduledWeekday != null) {
                val scheduledDayOfWeek = (habit.scheduledWeekday + 1) % 7
                val actualDayOfWeek = if (today.dayOfWeek.value == 7) 0 else today.dayOfWeek.value
                scheduledDayOfWeek == actualDayOfWeek
            } else {
                val creationDayOfWeek = creationDate.dayOfWeek
                today.dayOfWeek == creationDayOfWeek
            }
        }
        HabitFrequency.MONTHLY -> {
            if (habit.scheduledMonthday != null) {
                today.dayOfMonth == habit.scheduledMonthday
            } else {
                today.dayOfMonth == creationDate.dayOfMonth
            }
        }
    }
}

/**
 * Calculates days until a habit becomes available
 */
fun daysUntilHabitAvailable(habit: Habit): Int {
    val today = LocalDate.now()
    
    return when (habit.frequency) {
        HabitFrequency.DAILY -> 0 // Daily habits are always available
        HabitFrequency.WEEKLY -> {
            if (habit.scheduledWeekday != null) {
                val scheduledDayOfWeek = (habit.scheduledWeekday + 1) % 7
                val actualDayOfWeek = if (today.dayOfWeek.value == 7) 0 else today.dayOfWeek.value
                
                var daysUntil = (scheduledDayOfWeek - actualDayOfWeek + 7) % 7
                if (daysUntil == 0) daysUntil = 7
                daysUntil
            } else {
                val creationDate = habit.createdAt.toLocalDate()
                val creationDayOfWeek = creationDate.dayOfWeek
                val actualDayOfWeek = today.dayOfWeek
                
                var daysUntil = (creationDayOfWeek.value - actualDayOfWeek.value + 7) % 7
                if (daysUntil == 0) daysUntil = 7
                daysUntil
            }
        }
        HabitFrequency.MONTHLY -> {
            if (habit.scheduledMonthday != null) {
                val scheduledDay = habit.scheduledMonthday
                val currentDay = today.dayOfMonth
                val daysInMonth = today.lengthOfMonth()
                
                if (scheduledDay > currentDay) {
                    scheduledDay - currentDay
                } else {
                    (daysInMonth - currentDay) + scheduledDay
                }
            } else {
                val creationDate = habit.createdAt.toLocalDate()
                val creationDay = creationDate.dayOfMonth
                val currentDay = today.dayOfMonth
                val daysInMonth = today.lengthOfMonth()
                
                if (creationDay > currentDay) {
                    creationDay - currentDay
                } else {
                    (daysInMonth - currentDay) + creationDay
                }
            }
        }
    }
}