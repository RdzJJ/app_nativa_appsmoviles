package com.habitsapp.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object Habits : Screen("habits")
    object Statistics : Screen("statistics")
    object AddHabit : Screen("add_habit")
    object EditHabit : Screen(
        route = "edit_habit/{habitId}",
        arguments = listOf(
            navArgument("habitId") {
                type = NavType.LongType
            }
        )
    ) {
        fun createRoute(habitId: Long) = "edit_habit/$habitId"
    }
}

