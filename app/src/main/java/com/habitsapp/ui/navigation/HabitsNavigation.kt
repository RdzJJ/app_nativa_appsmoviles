package com.habitsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.habitsapp.ui.screens.home.HomeScreen
import com.habitsapp.ui.screens.addhabit.AddHabitScreen
import com.habitsapp.ui.screens.edithabit.EditHabitScreen
import com.habitsapp.ui.screens.auth.LoginScreen

@Composable
fun HabitsNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Habits.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Habits.route) {
            HomeScreen(
                navController = navController,
                onNavigateToAddHabit = {
                    navController.navigate(Screen.AddHabit.route)
                },
                onNavigateToEditHabit = { habitId ->
                    navController.navigate(Screen.EditHabit.createRoute(habitId))
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Habits.route) { inclusive = true }
                    }
                }
            )
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

