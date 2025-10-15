package com.habitsapp.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.habitsapp.ui.components.BottomNavItem
import com.habitsapp.ui.components.HabitsBottomNavigation
import com.habitsapp.ui.screens.habits.HabitsScreen
import com.habitsapp.ui.screens.statistics.StatisticsScreen
import com.habitsapp.ui.screens.profile.ProfileScreen

@Composable
fun HomeScreen(
    navController: NavHostController,
    onNavigateToAddHabit: () -> Unit,
    onNavigateToEditHabit: (Long) -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    
    Scaffold(
        bottomBar = {
            HabitsBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                BottomNavItem.HABITS.ordinal -> {
                    HabitsScreen(
                        onNavigateToAddHabit = onNavigateToAddHabit,
                        onNavigateToEditHabit = onNavigateToEditHabit
                    )
                }
                BottomNavItem.STATISTICS.ordinal -> {
                    StatisticsScreen()
                }
                BottomNavItem.PROFILE.ordinal -> {
                    ProfileScreen(onLogout = onLogout)
                }
            }
        }
    }
}
