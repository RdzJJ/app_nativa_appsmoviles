package com.habitsapp.data.model

enum class HabitFrequency {
    DAILY,
    WEEKLY,
    MONTHLY,
    CUSTOM;

    val displayName: String
        get() = when (this) {
            DAILY -> "Diario"
            WEEKLY -> "Semanal"
            MONTHLY -> "Mensual"
            CUSTOM -> "Personalizado"
        }
}