package co.edu.upb.vitahabittracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.upb.vitahabittracker.data.models.Habit
import co.edu.upb.vitahabittracker.data.models.HabitEntry
import co.edu.upb.vitahabittracker.ui.theme.BluePrimary
import co.edu.upb.vitahabittracker.ui.theme.GreenPrimary
import co.edu.upb.vitahabittracker.ui.theme.TealPrimary
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun StatisticsScreen(
    habits: List<Habit> = emptyList(),
    habitEntries: List<HabitEntry> = emptyList()
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    val stats = remember(habits, habitEntries) {
        calculateStatistics(habits, habitEntries)
    }

    val completedDaysInMonth = remember(habitEntries, currentMonth) {
        habitEntries
            .filter {
                it.completedDate.year == currentMonth.year &&
                        it.completedDate.monthValue == currentMonth.monthValue
            }
            .map { it.completedDate.dayOfMonth }
            .toSet()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF9FAFB))
    ) {
        // Header con gradiente similar a HomeScreen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(GreenPrimary, BluePrimary)
                    ),
                    shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                )
                .padding(vertical = 32.dp)
        ) {
            Text(
                text = "Estadísticas",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Sección de métricas principales (espacio reducido)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatsCard("Total de Hábitos", stats.totalHabits.toString(), GreenPrimary, Modifier.weight(1f))
            StatsCard("Racha Actual", stats.currentStreak.toString(), BluePrimary, Modifier.weight(1f))
            StatsCard("Cumplimiento", "${stats.completionRate}%", TealPrimary, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Calendario con borde
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(1.5.dp, GreenPrimary.copy(alpha = 0.35f), RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Navegación de mes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                        Icon(Icons.Filled.ChevronLeft, contentDescription = "Mes anterior", tint = BluePrimary)
                    }

                    Text(
                        text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale("es"))} ${currentMonth.year}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )

                    IconButton(
                        onClick = { if (currentMonth < YearMonth.now()) currentMonth = currentMonth.plusMonths(1) },
                        enabled = currentMonth < YearMonth.now()
                    ) {
                        Icon(
                            Icons.Filled.ChevronRight,
                            contentDescription = "Mes siguiente",
                            tint = if (currentMonth < YearMonth.now()) BluePrimary else Color.Gray.copy(alpha = 0.4f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Calendario
                CalendarGrid(currentMonth, completedDaysInMonth)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Progreso por hábito
        if (habits.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Progreso por Hábito",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        modifier = Modifier.padding(bottom = 12.dp)
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
                                color = Color(0xFFE5E5E5)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
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
                color = Color(0xFF222222)
            )
            Text(
                text = "Completado $completionCount veces",
                fontSize = 12.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(top = 4.dp)
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
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                fontSize = 11.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(top = 6.dp)
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
    val startDayOfWeek = (firstDay.dayOfWeek.value - 1)
    val daysInMonth = lastDay.dayOfMonth

    val dayLabels = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

    Column {
        // Day labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            dayLabels.forEach { label ->
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF777777),
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
                    .padding(bottom = 6.dp),
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
                                        isToday -> BluePrimary.copy(alpha = 0.18f)
                                        else -> Color(0xFFF3F5F7)
                                    },
                                    shape = RoundedCornerShape(6.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dayNumber.toString(),
                                fontSize = 12.sp,
                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.SemiBold,
                                color = if (isCompleted) Color.White else Color(0xFF333333)
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

// ---------- Estadísticas y utilidades ----------

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
    val currentStreak = calculateCurrentStreak(entries)
    val completionRate = calculateCompletionRate(habits, entries)

    return Statistics(totalHabits, currentStreak, completionRate)
}

fun calculateCurrentStreak(entries: List<HabitEntry>): Int {
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

fun calculateCompletionRate(habits: List<Habit>, entries: List<HabitEntry>): Int {
    if (habits.isEmpty()) return 0

    val last30Days = LocalDate.now().minusDays(29)
    val recentEntries = entries.filter { it.completedDate >= last30Days }

    val expectedCompletions = habits.size * 30
    val actualCompletions = recentEntries.size

    return if (expectedCompletions > 0) {
        ((actualCompletions.toFloat() / expectedCompletions) * 100).toInt()
    } else 0
}

fun calculateHabitStats(habit: Habit, entries: List<HabitEntry>): HabitStatistics {
    val habitEntries = entries.filter { it.habitId == habit.id }
    val completionCount = habitEntries.size
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
