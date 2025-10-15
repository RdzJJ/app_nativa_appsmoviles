package com.habitsapp.data.model

enum class HabitFrequency(
    val displayName: String,
    val intervalHours: Long
) {
    HOURLY("Cada hora", 1),
    DAILY("Diario", 24),
    WEEKLY("Semanal", 168),  // 7 * 24
    MONTHLY("Mensual", 720), // 30 * 24 (aprox.)
    CUSTOM("Personalizado", 0)
}
