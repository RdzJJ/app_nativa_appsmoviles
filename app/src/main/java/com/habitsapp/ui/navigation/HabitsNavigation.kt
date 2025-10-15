package com.habitsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.habitsapp.ui.screens.habits.HabitsScreen
import com.habitsapp.ui.screens.statistics.StatisticsScreen
import com.habitsapp.ui.screens.addhabit.AddHabitScreen
import com.habitsapp.ui.screens.edithabit.EditHabitScreen

@Composable
fun HabitsNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Habits.route
    ) {
        composable(Screen.Habits.route) {
            HabitsScreen(
                onNavigateToAddHabit = {
                    navController.navigate(Screen.AddHabit.route)
                },
                onNavigateToEditHabit = { habitId ->
                    navController.navigate(Screen.EditHabit.createRoute(habitId))
                }
            )
        }
        
        composable(Screen.Statistics.route) {
            StatisticsScreen()
        }
        
        composable(Screen.AddHabit.route) {
            AddHabitScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            route = Screen.EditHabit.route,
            arguments = Screen.EditHabit.arguments
        ) { backStackEntry ->
            val habitId = backStackEntry.arguments?.getLong("habitId") ?: 0L
            EditHabitScreen(
                habitId = habitId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

