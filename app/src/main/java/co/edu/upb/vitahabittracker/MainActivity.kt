package co.edu.upb.vitahabittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.edu.upb.vitahabittracker.data.models.Habit
import co.edu.upb.vitahabittracker.data.models.HabitEntry
import co.edu.upb.vitahabittracker.data.models.User
import co.edu.upb.vitahabittracker.data.repository.AuthRepository
import co.edu.upb.vitahabittracker.data.repository.FirestoreHabitEntryRepository
import co.edu.upb.vitahabittracker.data.repository.FirestoreHabitRepository
import co.edu.upb.vitahabittracker.ui.screens.AddHabitDialog
import co.edu.upb.vitahabittracker.ui.screens.HomeScreen
import co.edu.upb.vitahabittracker.ui.screens.LoginScreen
import co.edu.upb.vitahabittracker.ui.screens.ProfileScreen
import co.edu.upb.vitahabittracker.ui.screens.SignupScreen
import co.edu.upb.vitahabittracker.ui.screens.StatisticsScreen
import co.edu.upb.vitahabittracker.ui.theme.BluePrimary
import co.edu.upb.vitahabittracker.ui.theme.GreenPrimary
import co.edu.upb.vitahabittracker.ui.theme.VitaHabitosTheme
import co.edu.upb.vitahabittracker.utils.NotificationScheduler
import co.edu.upb.vitahabittracker.utils.SessionManager
import java.time.LocalDate
import java.time.LocalTime
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                enableEdgeToEdge()
                setContent { VitaHabitosTheme { VitaHabitosApp() } }
        }
}

@Composable
fun VitaHabitosApp() {
        val context = androidx.compose.ui.platform.LocalContext.current
        val sessionManager = remember { SessionManager(context) }
        val notificationScheduler = remember { NotificationScheduler(context) }

        var isLoggedIn by remember { mutableStateOf(sessionManager.isLoggedIn()) }
        var isSignupMode by remember { mutableStateOf(false) }
        var currentUser by remember { mutableStateOf<User?>(sessionManager.getUser()) }
        var currentScreen by remember { mutableStateOf("home") }
        var isLoading by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        val authRepository = remember { AuthRepository() }
        val coroutineScope = rememberCoroutineScope()

        // Firestore repositories - initialized when user logs in
        var habitRepository by remember { mutableStateOf<FirestoreHabitRepository?>(null) }
        var habitEntryRepository by remember {
                mutableStateOf<FirestoreHabitEntryRepository?>(null)
        }

        // Sample habits - empty by default
        var habits by remember { mutableStateOf<List<Habit>>(emptyList()) }
        var habitEntries by remember { mutableStateOf<List<HabitEntry>>(emptyList()) }
        var showAddHabitDialog by remember { mutableStateOf(false) }
        var editingHabit by remember { mutableStateOf<Habit?>(null) }
        var showEditProfileDialog by remember { mutableStateOf(false) }
        var completedHabitsToday by remember { mutableStateOf<Set<Int>>(emptySet()) }
        var showEditHabitDialog by remember { mutableStateOf(false) }

        // Initialize repositories and load data when user logs in
        LaunchedEffect(currentUser?.id) {
                currentUser?.let { user ->
                        habitRepository = FirestoreHabitRepository(user.id)
                        habitEntryRepository = FirestoreHabitEntryRepository(user.id)

                        // Launch separate coroutines for each flow
                        launch {
                                habitRepository?.getHabitsFlow()?.collect { fetchedHabits ->
                                        habits = fetchedHabits
                                }
                        }

                        launch {
                                habitEntryRepository?.getHabitEntriesFlow()?.collect {
                                        fetchedEntries ->
                                        habitEntries = fetchedEntries
                                        // Update completed habits for today
                                        val today = LocalDate.now()
                                        completedHabitsToday =
                                                fetchedEntries
                                                        .filter { it.completedDate == today }
                                                        .map { it.habitId }
                                                        .toSet()
                                }
                        }
                }
        }

        when {
                !isLoggedIn -> {
                        if (isSignupMode) {
                                SignupScreen(
                                        onSignupClick = { name, email, password ->
                                                coroutineScope.launch {
                                                        isLoading = true
                                                        errorMessage = null
                                                        try {
                                                                val result =
                                                                        authRepository.signup(
                                                                                email,
                                                                                password,
                                                                                name
                                                                        )
                                                                result
                                                                        .onSuccess { user ->
                                                                                currentUser = user
                                                                                isLoggedIn = true
                                                                                isSignupMode = false
                                                                                isLoading = false
                                                                                sessionManager
                                                                                        .saveSession(
                                                                                                user
                                                                                        )
                                                                        }
                                                                        .onFailure { error ->
                                                                                errorMessage =
                                                                                        error.message
                                                                                                ?: "Error en el registro"
                                                                                isLoading = false
                                                                        }
                                                        } catch (e: Exception) {
                                                                errorMessage =
                                                                        e.message
                                                                                ?: "Error desconocido"
                                                                isLoading = false
                                                        }
                                                }
                                        },
                                        onBackClick = { isSignupMode = false },
                                        isLoading = isLoading,
                                        errorMessage = errorMessage
                                )
                        } else {
                                LoginScreen(
                                        onLoginClick = { email, password ->
                                                coroutineScope.launch {
                                                        isLoading = true
                                                        errorMessage = null
                                                        try {
                                                                val result =
                                                                        authRepository.login(
                                                                                email,
                                                                                password
                                                                        )
                                                                result
                                                                        .onSuccess { user ->
                                                                                currentUser = user
                                                                                isLoggedIn = true
                                                                                isLoading = false
                                                                                sessionManager
                                                                                        .saveSession(
                                                                                                user
                                                                                        )
                                                                        }
                                                                        .onFailure { error ->
                                                                                errorMessage =
                                                                                        error.message
                                                                                                ?: "Error en el login"
                                                                                isLoading = false
                                                                        }
                                                        } catch (e: Exception) {
                                                                errorMessage =
                                                                        e.message
                                                                                ?: "Error desconocido"
                                                                isLoading = false
                                                        }
                                                }
                                        },
                                        onSignupClick = { isSignupMode = true },
                                        isLoading = isLoading,
                                        errorMessage = errorMessage
                                )
                        }
                }
                else -> {
                        MainAppScreen(
                                currentScreen = currentScreen,
                                onScreenChange = { currentScreen = it },
                                user = currentUser,
                                habits = habits,
                                habitEntries = habitEntries,
                                onAddHabit = { showAddHabitDialog = true },
                                editingHabit = editingHabit,
                                showEditHabitDialog = showEditHabitDialog,
                                onDismissEditHabit = {
                                        showEditHabitDialog = false
                                        editingHabit = null
                                },
                                onDeleteHabit = { habit ->
                                        coroutineScope.launch {
                                                // Delete from Firestore
                                                habitRepository?.deleteHabit(habit.id)
                                                habitEntryRepository?.deleteEntriesForHabit(
                                                        habit.id
                                                )
                                                // Cancel reminders for this habit
                                                notificationScheduler.cancelHabitReminder(habit.id)
                                        }
                                },
                                onEditHabit = { habit ->
                                        editingHabit = habit
                                        showEditHabitDialog = true
                                },
                                onCompleteHabit = { habit ->
                                        coroutineScope.launch {
                                                val today = LocalDate.now()
                                                val existingEntry =
                                                        habitEntries.find {
                                                                it.habitId == habit.id &&
                                                                        it.completedDate == today
                                                        }

                                                if (existingEntry != null) {
                                                        // Remove entry from Firestore
                                                        habitEntryRepository?.deleteHabitEntry(
                                                                existingEntry.id
                                                        )
                                                } else {
                                                        // Add new entry to Firestore
                                                        val newEntry =
                                                                HabitEntry(
                                                                        id =
                                                                                System.currentTimeMillis()
                                                                                        .toInt(),
                                                                        habitId = habit.id,
                                                                        completedDate = today,
                                                                        completedTime =
                                                                                LocalTime.now()
                                                                                        .toString()
                                                                )
                                                        habitEntryRepository?.addHabitEntry(
                                                                newEntry
                                                        )
                                                }
                                        }
                                },
                                onLogout = {
                                        sessionManager.clearSession()
                                        isLoggedIn = false
                                        currentUser = null
                                        habits = emptyList()
                                        habitEntries = emptyList()
                                        completedHabitsToday = emptySet()
                                        currentScreen = "home"
                                },
                                onEditProfile = { showEditProfileDialog = true },
                                showAddHabitDialog = showAddHabitDialog,
                                onDismissAddHabit = { showAddHabitDialog = false },
                                onSaveHabit = {
                                        name,
                                        description,
                                        frequency,
                                        reminderTime,
                                        finishDate ->
                                        coroutineScope.launch {
                                                if (showEditHabitDialog && editingHabit != null) {
                                                        // Update existing habit in Firestore
                                                        val updatedHabit =
                                                                editingHabit!!.copy(
                                                                        name = name,
                                                                        description = description,
                                                                        frequency = frequency,
                                                                        reminderTime = reminderTime,
                                                                        finishDate = finishDate
                                                                )
                                                        habitRepository?.updateHabit(updatedHabit)
                                                        showEditHabitDialog = false
                                                        editingHabit = null

                                                        // Reschedule notification
                                                        notificationScheduler.cancelHabitReminder(
                                                                updatedHabit.id
                                                        )
                                                        if (reminderTime != null) {
                                                                notificationScheduler
                                                                        .scheduleHabitReminder(
                                                                                updatedHabit
                                                                        )
                                                        }
                                                } else {
                                                        // Create new habit in Firestore
                                                        val newHabit =
                                                                Habit(
                                                                        id =
                                                                                System.currentTimeMillis()
                                                                                        .toInt(),
                                                                        name = name,
                                                                        description = description,
                                                                        frequency = frequency,
                                                                        reminderTime = reminderTime,
                                                                        finishDate = finishDate
                                                                )
                                                        habitRepository?.addHabit(newHabit)
                                                        showAddHabitDialog = false

                                                        // Schedule notification if reminder is set
                                                        if (reminderTime != null) {
                                                                notificationScheduler
                                                                        .scheduleHabitReminder(
                                                                                newHabit
                                                                        )
                                                        }
                                                }
                                        }
                                },
                                completedHabitsToday = completedHabitsToday
                        )
                }
        }
}

@Composable
fun MainAppScreen(
        currentScreen: String,
        onScreenChange: (String) -> Unit,
        user: User?,
        habits: List<Habit>,
        habitEntries: List<HabitEntry> = emptyList(),
        onAddHabit: () -> Unit,
        onDeleteHabit: (Habit) -> Unit,
        onEditHabit: (Habit) -> Unit,
        onCompleteHabit: (Habit) -> Unit,
        onLogout: () -> Unit,
        onEditProfile: () -> Unit,
        showAddHabitDialog: Boolean = false,
        onDismissAddHabit: () -> Unit = {},
        editingHabit: Habit? = null,
        showEditHabitDialog: Boolean = false,
        onDismissEditHabit: () -> Unit = {},
        onSaveHabit:
                (
                        String,
                        String,
                        co.edu.upb.vitahabittracker.data.models.HabitFrequency,
                        String?,
                        String?) -> Unit =
                { _, _, _, _, _ ->
                },
        completedHabitsToday: Set<Int> = emptySet()
) {
        Box(modifier = Modifier.fillMaxSize()) {
                Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                                Box(
                                        modifier =
                                                Modifier.fillMaxWidth()
                                                        .padding(
                                                                horizontal = 16.dp,
                                                                vertical = 8.dp
                                                        )
                                                        .navigationBarsPadding(),
                                        contentAlignment = Alignment.BottomCenter
                                ) {
                                        NavigationBar(
                                                modifier =
                                                        Modifier.fillMaxWidth()
                                                                .height(64.dp)
                                                                .background(
                                                                        brush =
                                                                                Brush.horizontalGradient(
                                                                                        colors =
                                                                                                listOf(
                                                                                                        GreenPrimary
                                                                                                                .copy(
                                                                                                                        alpha =
                                                                                                                                0.95f
                                                                                                                ),
                                                                                                        BluePrimary
                                                                                                                .copy(
                                                                                                                        alpha =
                                                                                                                                0.95f
                                                                                                                )
                                                                                                )
                                                                                ),
                                                                        shape =
                                                                                RoundedCornerShape(
                                                                                        28.dp
                                                                                )
                                                                ),
                                                containerColor = Color.Transparent,
                                                tonalElevation = 0.dp
                                        ) {
                                                NavigationBarItem(
                                                        icon = {
                                                                Icon(
                                                                        Icons.Filled.Home,
                                                                        contentDescription =
                                                                                stringResource(
                                                                                        R.string
                                                                                                .home_title
                                                                                )
                                                                )
                                                        },
                                                        label = {
                                                                Text(
                                                                        stringResource(
                                                                                R.string.home_title
                                                                        )
                                                                )
                                                        },
                                                        selected = currentScreen == "home",
                                                        onClick = { onScreenChange("home") },
                                                        colors =
                                                                NavigationBarItemDefaults.colors(
                                                                        selectedIconColor =
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .onPrimary,
                                                                        selectedTextColor =
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .onPrimary,
                                                                        unselectedIconColor =
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .onPrimary
                                                                                        .copy(
                                                                                                alpha =
                                                                                                        0.6f
                                                                                        ),
                                                                        unselectedTextColor =
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .onPrimary
                                                                                        .copy(
                                                                                                alpha =
                                                                                                        0.6f
                                                                                        ),
                                                                        indicatorColor =
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .onPrimary
                                                                                        .copy(
                                                                                                alpha =
                                                                                                        0.15f
                                                                                        )
                                                                )
                                                )
                                                NavigationBarItem(
                                                        icon = {
                                                                Icon(
                                                                        Icons.Filled.BarChart,
                                                                        contentDescription =
                                                                                stringResource(
                                                                                        R.string
                                                                                                .statistics_title
                                                                                )
                                                                )
                                                        },
                                                        label = {
                                                                Text(
                                                                        stringResource(
                                                                                R.string
                                                                                        .statistics_title
                                                                        )
                                                                )
                                                        },
                                                        selected = currentScreen == "statistics",
                                                        onClick = { onScreenChange("statistics") },
                                                        colors =
                                                                NavigationBarItemDefaults.colors(
                                                                        selectedIconColor =
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .onPrimary,
                                                                        selectedTextColor =
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .onPrimary,
                                                                        unselectedIconColor =
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .onPrimary
                                                                                        .copy(
                                                                                                alpha =
                                                                                                        0.6f
                                                                                        ),
                                                                        unselectedTextColor =
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .onPrimary
                                                                                        .copy(
                                                                                                alpha =
                                                                                                        0.6f
                                                                                        ),
                                                                        indicatorColor =
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .onPrimary
                                                                                        .copy(
                                                                                                alpha =
                                                                                                        0.15f
                                                                                        )
                                                                )
                                                )
                                                NavigationBarItem(
                                                        icon = {
                                                                Icon(
                                                                        Icons.Filled.Person,
                                                                        contentDescription =
                                                                                stringResource(
                                                                                        R.string
                                                                                                .profile_title
                                                                                )
                                                                )
                                                        },
                                                        label = {
                                                                Text(
                                                                        stringResource(
                                                                                R.string
                                                                                        .profile_title
                                                                        )
                                                                )
                                                        },
                                                        selected = currentScreen == "profile",
                                                        onClick = { onScreenChange("profile") },
                                                        colors =
                                                                NavigationBarItemDefaults.colors(
                                                                        selectedIconColor =
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .onPrimary,
                                                                        selectedTextColor =
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .onPrimary,
                                                                        unselectedIconColor =
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .onPrimary
                                                                                        .copy(
                                                                                                alpha =
                                                                                                        0.6f
                                                                                        ),
                                                                        unselectedTextColor =
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .onPrimary
                                                                                        .copy(
                                                                                                alpha =
                                                                                                        0.6f
                                                                                        ),
                                                                        indicatorColor =
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .onPrimary
                                                                                        .copy(
                                                                                                alpha =
                                                                                                        0.15f
                                                                                        )
                                                                )
                                                )
                                        }
                                }
                        }
                ) { innerPadding ->
                        Box(
                                modifier =
                                        Modifier.fillMaxSize()
                                                .padding(innerPadding)
                                                .consumeWindowInsets(innerPadding)
                        ) {
                                when (currentScreen) {
                                        "home" ->
                                                HomeScreen(
                                                        habits = habits,
                                                        onAddHabitClick = onAddHabit,
                                                        onHabitClick = { onEditHabit(it) },
                                                        onDeleteHabit = onDeleteHabit,
                                                        onCompleteHabit = onCompleteHabit,
                                                        completedHabitsToday = completedHabitsToday,
                                                        userName = user?.name ?: "Usuario"
                                                )
                                        "statistics" ->
                                                StatisticsScreen(
                                                        habits = habits,
                                                        habitEntries = habitEntries
                                                )
                                        "profile" ->
                                                ProfileScreen(user = user, onLogoutClick = onLogout)
                                }
                                if (showAddHabitDialog) {
                                        AddHabitDialog(
                                                onDismiss = onDismissAddHabit,
                                                onSave = onSaveHabit
                                        )
                                }
                        }
                }
        }
}
