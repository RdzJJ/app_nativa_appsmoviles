package com.habitsapp.ui.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.habitsapp.R
import com.habitsapp.ui.theme.*
import com.habitsapp.ui.viewmodel.AuthViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    
    if (currentUser != null) {
        if (isEditMode) {
            EditProfileScreen(
                user = currentUser!!,
                onSave = { updatedUser ->
                    viewModel.updateProfile(updatedUser)
                    isEditMode = false
                },
                onCancel = { isEditMode = false },
                viewModel = viewModel
            )
        } else {
            ProfileViewScreen(
                user = currentUser!!,
                onEditClick = { isEditMode = true },
                onLogoutClick = { showLogoutDialog = true },
                isLoading = isLoading
            )
        }
    }
    
    if (showLogoutDialog) {
        LogoutConfirmDialog(
            onConfirm = {
                viewModel.logout()
                onLogout()
            },
            onDismiss = { showLogoutDialog = false }
        )
    }
}

@Composable
private fun ProfileViewScreen(
    user: com.habitsapp.data.model.User,
    onEditClick: () -> Unit,
    onLogoutClick: () -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(VitaDarkPurple, VitaLight),
                    startY = 0f,
                    endY = 1000f
                )
            )
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.profile_title),
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = VitaWhite
                )
            )
        }
        
        // Profile Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Surface(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                color = VitaGreen,
                shape = CircleShape
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = user.fullName.take(1).uppercase(),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = VitaDarkPurple,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // User Info
            Text(
                text = user.fullName,
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = VitaWhite
                )
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = VitaWhite.copy(alpha = 0.7f)
                )
            )
            
            if (user.phone.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = user.phone,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = VitaWhite.copy(alpha = 0.7f)
                    )
                )
            }
        }
        
        // Stats Cards
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                icon = Icons.Default.CheckCircle,
                title = stringResource(R.string.total_habits_created),
                value = user.totalHabitsCreated.toString(),
                backgroundColor = VitaGreen
            )
            
            StatCard(
                icon = Icons.Default.Whatshot,
                title = stringResource(R.string.best_streak),
                value = "${user.bestStreak} días",
                backgroundColor = StreakGold
            )
            
            StatCard(
                icon = Icons.Default.DateRange,
                title = stringResource(R.string.joined_date),
                value = formatDate(user.createdAt),
                backgroundColor = VitaGrey
            )
        }
        
        // Bio Section
        if (user.bio.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = VitaWhite.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.bio),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = VitaGreen
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = user.bio,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = VitaWhite
                        )
                    )
                }
            }
        }
        
        // Action Buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onEditClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VitaGreen,
                    contentColor = VitaDarkPurple
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.edit_profile))
            }
            
            OutlinedButton(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Error
                ),
                border = BorderStroke(1.dp, Error),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.logout))
            }
        }
    }
}

@Composable
private fun StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    backgroundColor: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor.copy(alpha = 0.15f)
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, backgroundColor.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = VitaWhite.copy(alpha = 0.7f)
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = VitaWhite,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = backgroundColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    modifier = Modifier.size(24.dp),
                    tint = backgroundColor
                )
            }
        }
    }
}

@Composable
private fun EditProfileScreen(
    user: com.habitsapp.data.model.User,
    onSave: (com.habitsapp.data.model.User) -> Unit,
    onCancel: () -> Unit,
    viewModel: AuthViewModel
) {
    var fullName by remember { mutableStateOf(user.fullName) }
    var phone by remember { mutableStateOf(user.phone) }
    var bio by remember { mutableStateOf(user.bio) }
    
    val isLoading by viewModel.isLoading.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(VitaDarkPurple, VitaLight),
                    startY = 0f,
                    endY = 1000f
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.edit_profile),
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = VitaWhite
                )
            )
            IconButton(onClick = onCancel) {
                Icon(Icons.Default.Close, contentDescription = null, tint = VitaWhite)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Full Name
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text(stringResource(R.string.full_name)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VitaGreen,
                unfocusedBorderColor = VitaGreen.copy(alpha = 0.5f),
                focusedLabelColor = VitaGreen
            ),
            singleLine = true
        )
        
        // Phone
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text(stringResource(R.string.phone)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VitaGreen,
                unfocusedBorderColor = VitaGreen.copy(alpha = 0.5f),
                focusedLabelColor = VitaGreen
            ),
            singleLine = true
        )
        
        // Bio
        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text(stringResource(R.string.bio)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VitaGreen,
                unfocusedBorderColor = VitaGreen.copy(alpha = 0.5f),
                focusedLabelColor = VitaGreen
            ),
            maxLines = 4
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Save Button
        Button(
            onClick = {
                onSave(user.copy(
                    fullName = fullName,
                    phone = phone,
                    bio = bio
                ))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = VitaGreen,
                contentColor = VitaDarkPurple
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading
        ) {
            Text(stringResource(R.string.save_changes))
        }
        
        // Cancel Button
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = VitaWhite
            ),
            border = BorderStroke(1.dp, VitaWhite.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(stringResource(R.string.cancel))
        }
    }
}

@Composable
private fun LogoutConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.logout))
        },
        text = {
            Text(stringResource(R.string.logout_confirm))
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Error
                )
            ) {
                Text(stringResource(R.string.yes))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(stringResource(R.string.no))
            }
        }
    )
}

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("dd/MM/yyyy", Locale("es", "ES"))
    return format.format(date)
}
