package co.edu.upb.vitahabittracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                        reminderTime: String?) -> Unit
) {
    var habitName by remember { mutableStateOf("") }
    var habitDescription by remember { mutableStateOf("") }
    var selectedFrequency by remember { mutableStateOf(HabitFrequency.DAILY) }
    var enableReminder by remember { mutableStateOf(false) }
    var selectedHour by remember { mutableStateOf(9) }
    var selectedMinute by remember { mutableStateOf(0) }
    var showTimePicker by remember { mutableStateOf(false) }

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
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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
                            text = stringResource(R.string.add_habit),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(
                                Icons.Filled.Close,
                                contentDescription = stringResource(R.string.cancel),
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
                                        unfocusedBorderColor = BluePrimary.copy(alpha = 0.3f)
                                )
                )

                // Habit Description Field
                OutlinedTextField(
                        value = habitDescription,
                        onValueChange = { habitDescription = it },
                        label = { Text(stringResource(R.string.habit_description)) },
                        modifier =
                                Modifier.fillMaxWidth()
                                        .padding(bottom = 24.dp)
                                        .heightIn(min = 80.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors =
                                OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = BluePrimary,
                                        unfocusedBorderColor = BluePrimary.copy(alpha = 0.3f)
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
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                    selected = selectedFrequency == frequency,
                                    onClick = { selectedFrequency = frequency },
                                    colors =
                                            RadioButtonDefaults.colors(
                                                    selectedColor = GreenPrimary,
                                                    unselectedColor = BluePrimary.copy(alpha = 0.5f)
                                            )
                            )
                            Text(
                                    text =
                                            when (frequency) {
                                                HabitFrequency.DAILY ->
                                                        stringResource(R.string.daily)
                                                HabitFrequency.WEEKLY -> "Semanal"
                                                HabitFrequency.MONTHLY -> "Mensual"
                                            },
                                    modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

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
                                            checkedTrackColor = GreenPrimary.copy(alpha = 0.5f)
                                    )
                    )
                }

                if (enableReminder) {
                    OutlinedButton(
                            onClick = { showTimePicker = true },
                            modifier =
                                    Modifier.fillMaxWidth().padding(bottom = 24.dp).height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors =
                                    ButtonDefaults.outlinedButtonColors(
                                            containerColor = BluePrimary.copy(alpha = 0.05f)
                                    )
                    ) {
                        Icon(
                                imageVector = androidx.compose.material.icons.Icons.Filled.Schedule,
                                contentDescription = null,
                                tint = BluePrimary,
                                modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                                text = String.format("%02d:%02d", selectedHour, selectedMinute),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = BluePrimary
                        )
                    }
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
                            border = androidx.compose.foundation.BorderStroke(1.dp, BluePrimary)
                    ) { Text(stringResource(R.string.cancel)) }

                    Button(
                            onClick = {
                                if (habitName.isNotBlank()) {
                                    val reminderTime =
                                            if (enableReminder) {
                                                String.format(
                                                        "%02d:%02d",
                                                        selectedHour,
                                                        selectedMinute
                                                )
                                            } else null
                                    onSave(
                                            habitName,
                                            habitDescription,
                                            selectedFrequency,
                                            reminderTime
                                    )
                                }
                            },
                            modifier = Modifier.weight(1f).height(48.dp),
                            enabled = habitName.isNotBlank(),
                            colors =
                                    ButtonDefaults.buttonColors(
                                            containerColor = GreenPrimary,
                                            disabledContainerColor = GreenPrimary.copy(alpha = 0.5f)
                                    ),
                            shape = RoundedCornerShape(12.dp)
                    ) { Text(stringResource(R.string.save)) }
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
                    is24Hour = true
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
                TextButton(onClick = { onConfirm(timePickerState.hour, timePickerState.minute) }) {
                    Text("Confirmar", color = GreenPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
            shape = RoundedCornerShape(20.dp)
    )
}
