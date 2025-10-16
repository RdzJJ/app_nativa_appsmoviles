package co.edu.upb.vitahabittracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.upb.vitahabittracker.R
import co.edu.upb.vitahabittracker.data.models.Habit
import co.edu.upb.vitahabittracker.data.models.HabitEntry
import co.edu.upb.vitahabittracker.ui.theme.GreenPrimary
import co.edu.upb.vitahabittracker.ui.theme.BluePrimary
import co.edu.upb.vitahabittracker.ui.theme.TealPrimary
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.*

@Composable
fun StatisticsScreen(
    habits: List<Habit> = emptyList(),
    habitEntries: List<HabitEntry> = emptyList()
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    // Calcular estadísticas
    val stats = remember(habits, habitEntries) {
        calculateStatistics(habits, habitEntries)
    }

    // Días completados del mes actual
    val completedDaysInMonth = remember(habitEntries, currentMonth) {
        habitEntries
            .filter {
                it.completedDate.year == currentMonth.year &&
                        it.completedDate.monthValue == currentMonth.monthValue
            }
            .map { it.completedDate.dayOfMonth }
            .toSet()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header con fondo verde (igual que HomeScreen)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(
                                GreenPrimary.copy(alpha = 0.9f),
                                GreenPrimary.copy(alpha = 0.7f)
                            )
                        ),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.statistics_title),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stats Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatsCard(
                    title = stringResource(R.string.total_habits),
                    value = stats.totalHabits.toString(),
                    color = GreenPrimary,
                    modifier = Modifier.weight(1f)
                )
                StatsCard(
                    title = stringResource(R.string.current_streak),
                    value = stats.currentStreak.toString(),
                    color = BluePrimary,
                    modifier = Modifier.weight(1f)
                )
                StatsCard(
                    title = stringResource(R.string.completion_rate),
                    value = "${stats.completionRate}%",
                    color = TealPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            // Calendar Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Month Navigation
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { currentMonth = currentMonth.minusMonths(1) },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Filled.ChevronLeft,
                                contentDescription = "Mes anterior",
                                tint = BluePrimary
                            )
                        }

                        Text(
                            text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale("es"))} ${currentMonth.year}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        IconButton(
                            onClick = {
                                if (currentMonth < YearMonth.now()) {
                                    currentMonth = currentMonth.plusMonths(1)
                                }
                            },
                            modifier = Modifier.size(40.dp),
                            enabled = currentMonth < YearMonth.now()
                        ) {
                            Icon(
                                Icons.Filled.ChevronRight,
                                contentDescription = "Mes siguiente",
                                tint = if (currentMonth < YearMonth.now()) BluePrimary
                                else BluePrimary.copy(alpha = 0.3f)
                            )
                        }
                    }

                    // Calendar Grid
                    CalendarGrid(currentMonth, completedDaysInMonth)
                }
            }

            // Habits Progress List
            if (habits.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Progreso por Hábito",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        habits.forEach { habit ->
                            val habitStats = calculateHabitStats(habit, habitEntries)
                            HabitProgressItem(
                                habit = habit,
                                completionCount = habitStats.completionCount,
                                currentStreak = habitStats.streak
                            )
                            if (habit != habits.last()) {
                                Divider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                                )
                            }
                        }
                    }
                }
            }

            // Legend
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Leyenda",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(
                                    color = GreenPrimary,
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                        Text(
                            text = "Día con hábitos completados",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surface
                                        .copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                        Text(
                            text = "Día sin completar",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HabitProgressItem(
    habit: Habit,
    completionCount: Int,
    currentStreak: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = habit.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Completado $completionCount veces",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Text(
            text = "$currentStreak días",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = BluePrimary
        )
    }
}

@Composable
fun StatsCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun CalendarGrid(
    yearMonth: YearMonth,
    completedDays: Set<Int>
) {
    val firstDay = yearMonth.atDay(1)
    val lastDay = yearMonth.atEndOfMonth()
    val startDayOfWeek = (firstDay.dayOfWeek.value % 7)
    val daysInMonth = lastDay.dayOfMonth

    val dayLabels = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

    Column {
        // Day labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            dayLabels.forEach { label ->
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }

        // Calendar days
        var currentDay = 1
        repeat((daysInMonth + startDayOfWeek + 6) / 7) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(7) { dayOfWeek ->
                    val dayNumber = currentDay - startDayOfWeek
                    if (dayNumber in 1..daysInMonth) {
                        val isCompleted = dayNumber in completedDays
                        val isToday = yearMonth == YearMonth.now() && dayNumber == LocalDate.now().dayOfMonth

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                                .background(
                                    color = when {
                                        isCompleted -> GreenPrimary
                                        isToday -> BluePrimary.copy(alpha = 0.2f)
                                        else -> MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                                    },
                                    shape = RoundedCornerShape(6.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dayNumber.toString(),
                                fontSize = 12.sp,
                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.SemiBold,
                                color = if (isCompleted) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    } else {
                        Box(modifier = Modifier.weight(1f))
                    }
                    currentDay++
                }
            }
        }
    }
}

// Funciones de utilidad para calcular estadísticas
data class Statistics(
    val totalHabits: Int,
    val currentStreak: Int,
    val completionRate: Int
)

data class HabitStatistics(
    val completionCount: Int,
    val streak: Int
)

fun calculateStatistics(habits: List<Habit>, entries: List<HabitEntry>): Statistics {
    val totalHabits = habits.size

    // Calcular racha actual (días consecutivos con al menos un hábito completado)
    val currentStreak = calculateCurrentStreak(entries)

    // Calcular tasa de cumplimiento (últimos 30 días)
    val completionRate = calculateCompletionRate(habits, entries)

    return Statistics(totalHabits, currentStreak, completionRate)
}

fun calculateCurrentStreak(entries: List<HabitEntry>): Int {
    if (entries.isEmpty()) return 0

    val sortedDates = entries.map { it.completedDate }.distinct().sortedDescending()
    if (sortedDates.isEmpty()) return 0

    val today = LocalDate.now()
    // Si el último día completado no es hoy ni ayer, la racha se rompió
    if (sortedDates.first() != today && sortedDates.first() != today.minusDays(1)) {
        return 0
    }

    var streak = 0
    var currentDate = if (sortedDates.first() == today) today else today.minusDays(1)

    for (date in sortedDates) {
        if (date == currentDate) {
            streak++
            currentDate = currentDate.minusDays(1)
        } else {
            break
        }
    }

    return streak
}

fun calculateCompletionRate(habits: List<Habit>, entries: List<HabitEntry>): Int {
    if (habits.isEmpty()) return 0

    val last30Days = LocalDate.now().minusDays(29)
    val recentEntries = entries.filter { it.completedDate >= last30Days }

    // Total esperado: número de hábitos × 30 días
    val expectedCompletions = habits.size * 30
    val actualCompletions = recentEntries.size

    return if (expectedCompletions > 0) {
        ((actualCompletions.toFloat() / expectedCompletions) * 100).toInt()
    } else 0
}

fun calculateHabitStats(habit: Habit, entries: List<HabitEntry>): HabitStatistics {
    val habitEntries = entries.filter { it.habitId == habit.id }

    val completionCount = habitEntries.size

    // Calcular racha del hábito específico
    val streak = calculateHabitStreak(habitEntries)

    return HabitStatistics(completionCount, streak)
}

fun calculateHabitStreak(entries: List<HabitEntry>): Int {
    if (entries.isEmpty()) return 0

    val sortedDates = entries.map { it.completedDate }.distinct().sortedDescending()
    if (sortedDates.isEmpty()) return 0

    val today = LocalDate.now()
    if (sortedDates.first() != today && sortedDates.first() != today.minusDays(1)) {
        return 0
    }

    var streak = 0
    var currentDate = if (sortedDates.first() == today) today else today.minusDays(1)

    for (date in sortedDates) {
        if (date == currentDate) {
            streak++
            currentDate = currentDate.minusDays(1)
        } else {
            break
        }
    }

    return streak
}