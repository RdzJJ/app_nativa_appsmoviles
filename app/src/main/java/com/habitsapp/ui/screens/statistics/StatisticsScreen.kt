package com.habitsapp.ui.screens.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.habitsapp.ui.components.StatCard
import com.habitsapp.ui.components.HabitsBottomNavigation
import com.habitsapp.ui.components.SimpleBarChart
import com.habitsapp.ui.viewmodel.StatisticsViewModel
import com.habitsapp.ui.viewmodel.StatisticsData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    // Usa el tipo correcto y valor inicial
    val statistics by viewModel.statistics.collectAsState(initial = StatisticsData())
    val isLoading by viewModel.isLoading.collectAsState(initial = false)

    var selectedTab by remember { mutableIntStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadísticas") }
            )
        },
        bottomBar = {
            HabitsBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Resumen General",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatCard(
                            title = "Total Hábitos",
                            value = statistics.totalHabits.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Completados Hoy",
                            value = statistics.completedToday.toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatCard(
                            title = "Total Completados",
                            value = statistics.totalCompletions.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Racha Más Larga",
                            value = "${statistics.longestStreak} días",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    StatCard(
                        title = "Tasa de Cumplimiento Promedio",
                        value = "${statistics.averageCompletionRate.toInt()}%",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Text(
                        text = "Progreso de Hábitos",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Gráficos de Progreso",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            SimpleBarChart(
                                data = listOf(5f, 7f, 6f, 8f, 9f, 7f, 6f),
                                labels = listOf("L", "M", "X", "J", "V", "S", "D"),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Progreso semanal (ejemplo)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
