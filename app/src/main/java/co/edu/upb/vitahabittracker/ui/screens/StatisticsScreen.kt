package co.edu.upb.vitahabittracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import co.edu.upb.vitahabittracker.data.models.Habit
import co.edu.upb.vitahabittracker.data.models.HabitEntry
import co.edu.upb.vitahabittracker.data.models.HabitFrequency
import co.edu.upb.vitahabittracker.ui.theme.BluePrimary
import co.edu.upb.vitahabittracker.ui.theme.GreenPrimary
import co.edu.upb.vitahabittracker.ui.theme.TealPrimary
import java.time.DayOfWeek
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
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDayDialog by remember { mutableStateOf(false) }

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

    // Calcular hábitos por día considerando frecuencia y fecha de finalización
    val habitsPerDay = remember(habits, currentMonth) {
        calculateHabitsPerDay(habits, currentMonth)
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

                // Calendario con puntos
                CalendarGrid(
                    yearMonth = currentMonth,
                    completedDays = completedDaysInMonth,
                    habitsPerDay = habitsPerDay,
                    onDayClick = { day ->
                        selectedDate = currentMonth.atDay(day)
                        showDayDialog = true
                    }
                )
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

    // Diálogo de hábitos del día
    if (showDayDialog && selectedDate != null) {
        DayHabitsDialog(
            date = selectedDate!!,
            habits = habits,
            habitEntries = habitEntries,
            onDismiss = { showDayDialog = false }
        )
    }
}

@Composable
fun DayHabitsDialog(
    date: LocalDate,
    habits: List<Habit>,
    habitEntries: List<HabitEntry>,
    onDismiss: () -> Unit
) {
    // Filtrar los hábitos que deben cumplirse en esta fecha
    val habitsForDay = remember(habits, date) {
        habits.filter { habit -> shouldHabitBeActiveOnDate(habit, date) }
    }

    // Obtener los hábitos completados en esta fecha
    val completedHabitIds = remember(habitEntries, date) {
        habitEntries
            .filter { it.completedDate == date }
            .map { it.habitId }
            .toSet()
    }

    val daysOfWeek = listOf("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")
    val months = listOf(
        "enero", "febrero", "marzo", "abril", "mayo", "junio",
        "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
    )

    val dayOfWeek = daysOfWeek[date.dayOfWeek.value % 7]
    val formattedDate = "$dayOfWeek ${date.dayOfMonth} de ${months[date.monthValue - 1]} de ${date.year}"

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Hábitos del día",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = formattedDate,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Cerrar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )

                // Lista de hábitos
                if (habitsForDay.isEmpty()) {
                    // Sin hábitos para este día
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = GreenPrimary.copy(alpha = 0.3f)
                        )
                        Text(
                            text = "No hay hábitos para este día",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(top = 12.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    // Mostrar hábitos
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        habitsForDay.forEach { habit ->
                            val isCompleted = completedHabitIds.contains(habit.id)

                            HabitItemInDialog(
                                habit = habit,
                                isCompleted = isCompleted
                            )

                            if (habit != habitsForDay.last()) {
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de cerrar
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}

@Composable
fun HabitItemInDialog(
    habit: Habit,
    isCompleted: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted)
                GreenPrimary.copy(alpha = 0.1f)
            else
                MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isCompleted) GreenPrimary.copy(alpha = 0.3f) else Color(0xFFE5E5E5)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habit.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (habit.description.isNotEmpty()) {
                    Text(
                        text = habit.description,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Mostrar frecuencia
                Text(
                    text = when (habit.frequency) {
                        HabitFrequency.DAILY -> "Diario"
                        HabitFrequency.WEEKLY -> "Semanal"
                        HabitFrequency.MONTHLY -> "Mensual"
                    },
                    fontSize = 11.sp,
                    color = BluePrimary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .background(
                            BluePrimary.copy(alpha = 0.1f),
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Ícono de completado
            if (isCompleted) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = "Completado",
                    tint = GreenPrimary,
                    modifier = Modifier.size(32.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            Color(0xFFE5E5E5),
                            CircleShape
                        )
                )
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
    completedDays: Set<Int>,
    habitsPerDay: Map<Int, Int>,
    onDayClick: (Int) -> Unit
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
                        val habitCount = habitsPerDay[dayNumber] ?: 0

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
                                )
                                .clickable { onDayClick(dayNumber) },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = dayNumber.toString(),
                                    fontSize = 12.sp,
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.SemiBold,
                                    color = if (isCompleted) Color.White else Color(0xFF333333)
                                )

                                // Puntos indicadores de hábitos
                                if (habitCount > 0) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.height(4.dp)
                                    ) {
                                        repeat(minOf(habitCount, 5)) { index ->
                                            Box(
                                                modifier = Modifier
                                                    .size(3.dp)
                                                    .background(
                                                        color = if (isCompleted) Color.White.copy(alpha = 0.7f)
                                                        else BluePrimary.copy(alpha = 0.8f),
                                                        shape = CircleShape
                                                    )
                                            )
                                            if (index < minOf(habitCount, 5) - 1) {
                                                Spacer(modifier = Modifier.width(2.dp))
                                            }
                                        }
                                    }
                                }
                            }
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

// ---------- Funciones de cálculo ----------

/**
 * Calcula cuántos hábitos debe cumplir el usuario cada día del mes
 * considerando la frecuencia y la fecha de finalización
 */
fun calculateHabitsPerDay(habits: List<Habit>, yearMonth: YearMonth): Map<Int, Int> {
    val habitsPerDay = mutableMapOf<Int, Int>()
    val daysInMonth = yearMonth.lengthOfMonth()

    for (day in 1..daysInMonth) {
        val currentDate = yearMonth.atDay(day)
        var habitCount = 0

        for (habit in habits) {
            if (shouldHabitBeActiveOnDate(habit, currentDate)) {
                habitCount++
            }
        }

        if (habitCount > 0) {
            habitsPerDay[day] = habitCount
        }
    }

    return habitsPerDay
}

/**
 * Determina si un hábito debe estar activo en una fecha específica
 */
fun shouldHabitBeActiveOnDate(habit: Habit, date: LocalDate): Boolean {
    // Verificar que el hábito esté activo
    if (!habit.isActive) return false

    // Verificar que la fecha sea después de la creación del hábito
    val creationDate = habit.createdAt.toLocalDate()
    if (date.isBefore(creationDate)) return false

    // Verificar la fecha de finalización
    habit.finishDate?.let { finishDateStr ->
        try {
            val finishDate = LocalDate.parse(finishDateStr)
            if (date.isAfter(finishDate)) return false
        } catch (e: Exception) {
            // Si hay error al parsear, ignorar la fecha de finalización
        }
    }

    // Verificar la frecuencia
    return when (habit.frequency) {
        HabitFrequency.DAILY -> true
        HabitFrequency.WEEKLY -> {
            // Para hábitos semanales, solo mostrar en el día de la semana en que se creó
            val creationDayOfWeek = creationDate.dayOfWeek
            date.dayOfWeek == creationDayOfWeek
        }
        HabitFrequency.MONTHLY -> {
            // Para hábitos mensuales, solo mostrar en el día del mes en que se creó
            val creationDayOfMonth = creationDate.dayOfMonth
            date.dayOfMonth == creationDayOfMonth
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