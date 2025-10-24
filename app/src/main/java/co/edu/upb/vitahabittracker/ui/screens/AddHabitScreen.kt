package co.edu.upb.vitahabittracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import co.edu.upb.vitahabittracker.R
import co.edu.upb.vitahabittracker.data.models.HabitFrequency
import co.edu.upb.vitahabittracker.ui.theme.BluePrimary
import co.edu.upb.vitahabittracker.ui.theme.GreenPrimary

@Composable
fun AddHabitDialog(
        onDismiss: () -> Unit,
        onSave:
                (
                        name: String,
                        description: String,
                        frequency: HabitFrequency,
                        reminderTime: String?,
                        finishDate: String?,
                        weekday: Int?,
                        monthday: Int?,
                        weeklyGoal: Int,
                        monthlyGoal: Int?) -> Unit,
        initialHabit: co.edu.upb.vitahabittracker.data.models.Habit? = null,
        isEditing: Boolean = false
) {
        var habitName by remember { mutableStateOf(initialHabit?.name ?: "") }
        var habitDescription by remember { mutableStateOf(initialHabit?.description ?: "") }
        var selectedFrequency by remember {
                mutableStateOf(initialHabit?.frequency ?: HabitFrequency.DAILY)
        }
        var selectedWeekday by remember { mutableStateOf(initialHabit?.scheduledWeekday ?: 0) }
        var selectedMonthday by remember { mutableStateOf(initialHabit?.scheduledMonthday ?: 1) }
        var weeklyGoal by remember { mutableStateOf((initialHabit?.weeklyGoal ?: 7).toString()) }
        var monthlyGoal by remember { mutableStateOf(initialHabit?.monthlyGoal?.toString() ?: "") }
        var enableReminder by remember { mutableStateOf(initialHabit?.reminderTime != null) }
        var selectedHour by remember { mutableStateOf(9) }
        var selectedMinute by remember { mutableStateOf(0) }
        var showTimePicker by remember { mutableStateOf(false) }
        var enableFinishDate by remember { mutableStateOf(initialHabit?.finishDate != null) }
        var selectedFinishDate by remember {
                mutableStateOf<java.time.LocalDate?>(
                        initialHabit?.finishDate?.let { java.time.LocalDate.parse(it) }
                )
        }
        var showDatePicker by remember { mutableStateOf(false) }

        Dialog(
                onDismissRequest = onDismiss,
                properties =
                        DialogProperties(
                                usePlatformDefaultWidth = false,
                                dismissOnBackPress = true,
                                dismissOnClickOutside = true
                        )
        ) {
                Card(
                        modifier = Modifier.fillMaxWidth(0.9f).padding(16.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors =
                                CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                )
                ) {
                        Column(
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .verticalScroll(rememberScrollState())
                                                .padding(24.dp)
                        ) {
                                // Header
                                Row(
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                        Text(
                                                text =
                                                        if (isEditing)
                                                                stringResource(R.string.edit_habit)
                                                        else stringResource(R.string.add_habit),
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                        )
                                        IconButton(
                                                onClick = onDismiss,
                                                modifier = Modifier.size(32.dp)
                                        ) {
                                                Icon(
                                                        Icons.Filled.Close,
                                                        contentDescription =
                                                                stringResource(R.string.cancel),
                                                        tint = MaterialTheme.colorScheme.onSurface
                                                )
                                        }
                                }

                                // Habit Name Field
                                OutlinedTextField(
                                        value = habitName,
                                        onValueChange = { habitName = it },
                                        label = { Text(stringResource(R.string.habit_name)) },
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        singleLine = true,
                                        colors =
                                                OutlinedTextFieldDefaults.colors(
                                                        focusedBorderColor = BluePrimary,
                                                        unfocusedBorderColor =
                                                                BluePrimary.copy(alpha = 0.3f)
                                                )
                                )

                                // Habit Description Field
                                OutlinedTextField(
                                        value = habitDescription,
                                        onValueChange = { habitDescription = it },
                                        label = {
                                                Text(stringResource(R.string.habit_description))
                                        },
                                        modifier =
                                                Modifier.fillMaxWidth()
                                                        .padding(bottom = 24.dp)
                                                        .heightIn(min = 80.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors =
                                                OutlinedTextFieldDefaults.colors(
                                                        focusedBorderColor = BluePrimary,
                                                        unfocusedBorderColor =
                                                                BluePrimary.copy(alpha = 0.3f)
                                                ),
                                        maxLines = 3
                                )

                                // Frequency Selection
                                Text(
                                        text = stringResource(R.string.habit_frequency),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                )

                                Column(modifier = Modifier.padding(bottom = 24.dp)) {
                                        HabitFrequency.values().forEach { frequency ->
                                                Row(
                                                        modifier =
                                                                Modifier.fillMaxWidth()
                                                                        .padding(bottom = 8.dp),
                                                        verticalAlignment =
                                                                Alignment.CenterVertically
                                                ) {
                                                        RadioButton(
                                                                selected =
                                                                        selectedFrequency ==
                                                                                frequency,
                                                                onClick = {
                                                                        selectedFrequency =
                                                                                frequency
                                                                },
                                                                colors =
                                                                        RadioButtonDefaults.colors(
                                                                                selectedColor =
                                                                                        GreenPrimary,
                                                                                unselectedColor =
                                                                                        BluePrimary
                                                                                                .copy(
                                                                                                        alpha =
                                                                                                                0.5f
                                                                                                )
                                                                        )
                                                        )
                                                        Text(
                                                                text =
                                                                        when (frequency) {
                                                                                HabitFrequency
                                                                                        .DAILY ->
                                                                                        stringResource(
                                                                                                R.string
                                                                                                        .daily
                                                                                        )
                                                                                HabitFrequency
                                                                                        .WEEKLY ->
                                                                                        "Semanal"
                                                                                HabitFrequency
                                                                                        .MONTHLY ->
                                                                                        "Mensual"
                                                                        },
                                                                modifier =
                                                                        Modifier.padding(
                                                                                start = 8.dp
                                                                        )
                                                        )
                                                }
                                        }

                                        // Weekly Day Selector
                                        if (selectedFrequency == HabitFrequency.WEEKLY) {
                                                Spacer(modifier = Modifier.height(12.dp))
                                                WeekdaySelectorRow(
                                                        selectedWeekday = selectedWeekday,
                                                        onWeekdaySelected = { selectedWeekday = it }
                                                )
                                        }

                                        // Monthly Day Selector
                                        if (selectedFrequency == HabitFrequency.MONTHLY) {
                                                Spacer(modifier = Modifier.height(12.dp))
                                                MonthdaySelector(
                                                        selectedMonthday = selectedMonthday,
                                                        onMonthdaySelected = {
                                                                selectedMonthday = it
                                                        }
                                                )
                                        }
                                }

                                // Goal Settings Section
                                Text(
                                        text = "Metas de cumplimiento",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                )

                                // Weekly Goal Field
                                OutlinedTextField(
                                        value = weeklyGoal,
                                        onValueChange = { 
                                                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                                        weeklyGoal = it
                                                }
                                        },
                                        label = { Text("Meta semanal (veces por semana)") },
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        singleLine = true,
                                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                                        ),
                                        colors =
                                                OutlinedTextFieldDefaults.colors(
                                                        focusedBorderColor = BluePrimary,
                                                        unfocusedBorderColor =
                                                                BluePrimary.copy(alpha = 0.3f)
                                                ),
                                        supportingText = { 
                                                Text(
                                                        "Ej: 7 para todos los días, 3 para tres veces por semana",
                                                        fontSize = 11.sp,
                                                        color = Color.Gray
                                                )
                                        }
                                )

                                // Monthly Goal Field (Optional)
                                OutlinedTextField(
                                        value = monthlyGoal,
                                        onValueChange = { 
                                                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                                        monthlyGoal = it
                                                }
                                        },
                                        label = { Text("Meta mensual (opcional)") },
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        singleLine = true,
                                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                                        ),
                                        colors =
                                                OutlinedTextFieldDefaults.colors(
                                                        focusedBorderColor = BluePrimary,
                                                        unfocusedBorderColor =
                                                                BluePrimary.copy(alpha = 0.3f)
                                                ),
                                        supportingText = { 
                                                Text(
                                                        "Deja vacío si no necesitas meta mensual",
                                                        fontSize = 11.sp,
                                                        color = Color.Gray
                                                )
                                        }
                                )

                                // Reminder Section
                                Row(
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                        Text(
                                                text = "Crear recordatorio",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Switch(
                                                checked = enableReminder,
                                                onCheckedChange = { enableReminder = it },
                                                colors =
                                                        SwitchDefaults.colors(
                                                                checkedThumbColor = GreenPrimary,
                                                                checkedTrackColor =
                                                                        GreenPrimary.copy(
                                                                                alpha = 0.5f
                                                                        )
                                                        )
                                        )
                                }

                                if (enableReminder) {
                                        OutlinedButton(
                                                onClick = { showTimePicker = true },
                                                modifier =
                                                        Modifier.fillMaxWidth()
                                                                .padding(bottom = 16.dp)
                                                                .height(56.dp),
                                                shape = RoundedCornerShape(12.dp),
                                                colors =
                                                        ButtonDefaults.outlinedButtonColors(
                                                                containerColor =
                                                                        BluePrimary.copy(
                                                                                alpha = 0.05f
                                                                        )
                                                        )
                                        ) {
                                                Icon(
                                                        imageVector =
                                                                androidx.compose.material.icons
                                                                        .Icons.Filled.Schedule,
                                                        contentDescription = null,
                                                        tint = BluePrimary,
                                                        modifier = Modifier.padding(end = 8.dp)
                                                )
                                                Text(
                                                        text =
                                                                String.format(
                                                                        "%02d:%02d",
                                                                        selectedHour,
                                                                        selectedMinute
                                                                ),
                                                        fontSize = 18.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = BluePrimary
                                                )
                                        }
                                }

                                // Finish Date Section
                                Row(
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                        Text(
                                                text = "Fecha de finalización",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Switch(
                                                checked = enableFinishDate,
                                                onCheckedChange = { enableFinishDate = it },
                                                colors =
                                                        SwitchDefaults.colors(
                                                                checkedThumbColor = GreenPrimary,
                                                                checkedTrackColor =
                                                                        GreenPrimary.copy(
                                                                                alpha = 0.5f
                                                                        )
                                                        )
                                        )
                                }

                                if (enableFinishDate) {
                                        OutlinedButton(
                                                onClick = { showDatePicker = true },
                                                modifier =
                                                        Modifier.fillMaxWidth()
                                                                .padding(bottom = 24.dp)
                                                                .height(56.dp),
                                                shape = RoundedCornerShape(12.dp),
                                                colors =
                                                        ButtonDefaults.outlinedButtonColors(
                                                                containerColor =
                                                                        BluePrimary.copy(
                                                                                alpha = 0.05f
                                                                        )
                                                        )
                                        ) {
                                                Icon(
                                                        imageVector = Icons.Filled.DateRange,
                                                        contentDescription = null,
                                                        tint = BluePrimary,
                                                        modifier = Modifier.padding(end = 8.dp)
                                                )
                                                Text(
                                                        text = selectedFinishDate?.toString()
                                                                        ?: "Seleccionar fecha",
                                                        fontSize = 18.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = BluePrimary
                                                )
                                        }
                                } else {
                                        Spacer(modifier = Modifier.height(24.dp))
                                }

                                // Action Buttons
                                Row(
                                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                        OutlinedButton(
                                                onClick = onDismiss,
                                                modifier = Modifier.weight(1f).height(48.dp),
                                                shape = RoundedCornerShape(12.dp),
                                                border =
                                                        androidx.compose.foundation.BorderStroke(
                                                                1.dp,
                                                                BluePrimary
                                                        )
                                        ) { Text(stringResource(R.string.cancel)) }

                                        Button(
                                                onClick = {
                                                        if (habitName.isNotBlank()) {
                                                                // Dismiss first to prevent
                                                                // duplicate calls
                                                                onDismiss()

                                                                val reminderTime =
                                                                        if (enableReminder) {
                                                                                String.format(
                                                                                        "%02d:%02d",
                                                                                        selectedHour,
                                                                                        selectedMinute
                                                                                )
                                                                        } else null
                                                                val finishDate =
                                                                        if (enableFinishDate) {
                                                                                selectedFinishDate
                                                                                        ?.toString()
                                                                        } else null
                                                                val parsedWeeklyGoal = weeklyGoal.toIntOrNull() ?: 7
                                                                val parsedMonthlyGoal = if (monthlyGoal.isNotBlank()) {
                                                                        monthlyGoal.toIntOrNull()
                                                                } else null
                                                                onSave(
                                                                        habitName,
                                                                        habitDescription,
                                                                        selectedFrequency,
                                                                        reminderTime,
                                                                        finishDate,
                                                                        if (selectedFrequency ==
                                                                                        HabitFrequency
                                                                                                .WEEKLY
                                                                        )
                                                                                selectedWeekday
                                                                        else null,
                                                                        if (selectedFrequency ==
                                                                                        HabitFrequency
                                                                                                .MONTHLY
                                                                        )
                                                                                selectedMonthday
                                                                        else null,
                                                                        parsedWeeklyGoal,
                                                                        parsedMonthlyGoal
                                                                )
                                                        }
                                                },
                                                modifier = Modifier.weight(1f).height(48.dp),
                                                enabled = habitName.isNotBlank(),
                                                colors =
                                                        ButtonDefaults.buttonColors(
                                                                containerColor = GreenPrimary,
                                                                disabledContainerColor =
                                                                        GreenPrimary.copy(
                                                                                alpha = 0.5f
                                                                        )
                                                        ),
                                                shape = RoundedCornerShape(12.dp)
                                        ) {
                                                Text(
                                                        if (isEditing) stringResource(R.string.save)
                                                        else stringResource(R.string.save)
                                                )
                                        }
                                }
                        }
                }

                // Time Picker Dialog
                if (showTimePicker) {
                        TimePickerDialog(
                                onDismiss = { showTimePicker = false },
                                onConfirm = { hour, minute ->
                                        selectedHour = hour
                                        selectedMinute = minute
                                        showTimePicker = false
                                },
                                initialHour = selectedHour,
                                initialMinute = selectedMinute
                        )
                }

                // Date Picker Dialog
                if (showDatePicker) {
                        DatePickerDialog(
                                onDismiss = { showDatePicker = false },
                                onConfirm = { date ->
                                        selectedFinishDate = date
                                        showDatePicker = false
                                },
                                initialDate = selectedFinishDate
                        )
                }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
        onDismiss: () -> Unit,
        onConfirm: (hour: Int, minute: Int) -> Unit,
        initialHour: Int,
        initialMinute: Int
) {
        val timePickerState =
                rememberTimePickerState(
                        initialHour = initialHour,
                        initialMinute = initialMinute,
                        is24Hour = false
                )

        AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(text = "Seleccionar hora", fontWeight = FontWeight.Bold) },
                text = {
                        TimePicker(
                                state = timePickerState,
                                colors =
                                        TimePickerDefaults.colors(
                                                clockDialColor = BluePrimary.copy(alpha = 0.1f),
                                                selectorColor = GreenPrimary,
                                                containerColor = MaterialTheme.colorScheme.surface,
                                                timeSelectorSelectedContainerColor = GreenPrimary,
                                                timeSelectorUnselectedContainerColor =
                                                        BluePrimary.copy(alpha = 0.1f)
                                        )
                        )
                },
                confirmButton = {
                        TextButton(
                                onClick = {
                                        onConfirm(timePickerState.hour, timePickerState.minute)
                                }
                        ) { Text("Confirmar", color = GreenPrimary, fontWeight = FontWeight.Bold) }
                },
                dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
                shape = RoundedCornerShape(20.dp)
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
        onDismiss: () -> Unit,
        onConfirm: (date: java.time.LocalDate) -> Unit,
        initialDate: java.time.LocalDate?
) {
        val datePickerState =
                rememberDatePickerState(
                        initialSelectedDateMillis =
                                initialDate?.let {
                                        it.atStartOfDay(java.time.ZoneId.systemDefault())
                                                .toInstant()
                                                .toEpochMilli()
                                }
                                        ?: System.currentTimeMillis()
                )

        androidx.compose.material3.DatePickerDialog(
                onDismissRequest = onDismiss,
                confirmButton = {
                        TextButton(
                                onClick = {
                                        datePickerState.selectedDateMillis?.let { millis ->
                                                val date =
                                                        java.time.Instant.ofEpochMilli(millis)
                                                                .atZone(
                                                                        java.time.ZoneId
                                                                                .systemDefault()
                                                                )
                                                                .toLocalDate()
                                                onConfirm(date)
                                        }
                                }
                        ) { Text("Confirmar", color = GreenPrimary, fontWeight = FontWeight.Bold) }
                },
                dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
        ) {
                DatePicker(
                        state = datePickerState,
                        colors =
                                DatePickerDefaults.colors(
                                        selectedDayContainerColor = GreenPrimary,
                                        todayContentColor = BluePrimary,
                                        todayDateBorderColor = BluePrimary
                                )
                )
        }
}

@Composable
fun WeekdaySelectorRow(selectedWeekday: Int, onWeekdaySelected: (Int) -> Unit) {
        val daysOfWeek = listOf("D", "L", "M", "M", "J", "V", "S")
        val dayNames =
                listOf("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")

        Column {
                Text(
                        text = "Selecciona el día",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        for (i in 0 until 7) {
                                Surface(
                                        modifier =
                                                Modifier.size(40.dp).clickable {
                                                        onWeekdaySelected(i)
                                                },
                                        shape = androidx.compose.foundation.shape.CircleShape,
                                        color =
                                                if (selectedWeekday == i) GreenPrimary
                                                else BluePrimary.copy(alpha = 0.2f)
                                ) {
                                        Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier.fillMaxSize()
                                        ) {
                                                Text(
                                                        text = daysOfWeek[i],
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color =
                                                                if (selectedWeekday == i)
                                                                        Color.White
                                                                else
                                                                        MaterialTheme.colorScheme
                                                                                .onSurface
                                                )
                                        }
                                }
                        }
                }
                Text(
                        text = dayNames[selectedWeekday],
                        fontSize = 12.sp,
                        color = BluePrimary,
                        modifier = Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.SemiBold
                )
        }
}

@Composable
fun MonthdaySelector(selectedMonthday: Int, onMonthdaySelected: (Int) -> Unit) {
        Column {
                Text(
                        text = "Selecciona el día del mes",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 8.dp)
                )

                // Calendar grid 7 columns
                Column(modifier = Modifier.fillMaxWidth()) {
                        for (week in 0 until 5) {
                                Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                        for (dayOfWeek in 0 until 7) {
                                                val dayNumber = week * 7 + dayOfWeek + 1
                                                if (dayNumber <= 31) {
                                                        Surface(
                                                                modifier =
                                                                        Modifier.size(40.dp)
                                                                                .clickable {
                                                                                        onMonthdaySelected(
                                                                                                dayNumber
                                                                                        )
                                                                                },
                                                                shape =
                                                                        androidx.compose.foundation
                                                                                .shape.CircleShape,
                                                                color =
                                                                        if (selectedMonthday ==
                                                                                        dayNumber
                                                                        )
                                                                                GreenPrimary
                                                                        else
                                                                                BluePrimary.copy(
                                                                                        alpha = 0.2f
                                                                                )
                                                        ) {
                                                                Box(
                                                                        contentAlignment =
                                                                                Alignment.Center,
                                                                        modifier =
                                                                                Modifier.fillMaxSize()
                                                                ) {
                                                                        Text(
                                                                                text =
                                                                                        dayNumber
                                                                                                .toString(),
                                                                                fontSize = 12.sp,
                                                                                fontWeight =
                                                                                        FontWeight
                                                                                                .Bold,
                                                                                color =
                                                                                        if (selectedMonthday ==
                                                                                                        dayNumber
                                                                                        )
                                                                                                Color.White
                                                                                        else
                                                                                                MaterialTheme
                                                                                                        .colorScheme
                                                                                                        .onSurface
                                                                        )
                                                                }
                                                        }
                                                } else {
                                                        Spacer(modifier = Modifier.size(40.dp))
                                                }
                                        }
                                }

                                if (week < 4) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                }
                        }
                }

                Text(
                        text = "Día $selectedMonthday",
                        fontSize = 12.sp,
                        color = BluePrimary,
                        modifier = Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.SemiBold
                )
        }
}
