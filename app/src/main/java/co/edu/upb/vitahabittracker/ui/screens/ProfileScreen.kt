package co.edu.upb.vitahabittracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import co.edu.upb.vitahabittracker.R
import co.edu.upb.vitahabittracker.data.models.User
import co.edu.upb.vitahabittracker.ui.theme.BluePrimary
import co.edu.upb.vitahabittracker.ui.theme.GreenPrimary

@Composable
fun ProfileScreen(user: User? = null, onLogoutClick: () -> Unit) {
        val displayUser = user ?: User(name = "Usuario", email = "usuario@email.com")
        var showEditDialog by remember { mutableStateOf(false) }

        if (showEditDialog) {
                EditProfileDialog(
                        user = displayUser,
                        onDismiss = { showEditDialog = false },
                        onSave = {
                                // Profile update logic (bio removed)
                                showEditDialog = false
                        }
                )
        }

        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .background(color = MaterialTheme.colorScheme.background)
                                .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
                // Header with gradient
                Box(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .height(200.dp)
                                        .background(
                                                brush =
                                                        Brush.verticalGradient(
                                                                colors =
                                                                        listOf(
                                                                                GreenPrimary,
                                                                                BluePrimary
                                                                        )
                                                        ),
                                                shape = RoundedCornerShape(20.dp)
                                        )
                                        .padding(20.dp),
                        contentAlignment = Alignment.Center
                ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                // Avatar
                                Surface(
                                        modifier = Modifier.size(80.dp).padding(bottom = 16.dp),
                                        shape = CircleShape,
                                        color =
                                                MaterialTheme.colorScheme.onPrimary.copy(
                                                        alpha = 0.2f
                                                )
                                ) {
                                        Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier.fillMaxSize()
                                        ) {
                                                Icon(
                                                        Icons.Filled.Person,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(40.dp),
                                                        tint = MaterialTheme.colorScheme.onPrimary
                                                )
                                        }
                                }

                                Text(
                                        text = displayUser.name,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                )
                        }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Statistics Card
                Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors =
                                CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                )
                ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                        "Tus Logros",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                )

                                Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                        StatItem(label = "Hábitos", value = "0")
                                        StatItem(label = "Racha", value = "0")
                                        StatItem(label = "Completados", value = "0")
                                }
                        }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Logout Button - In scrollable content
                Button(
                        onClick = onLogoutClick,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors =
                                ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                ),
                        shape = RoundedCornerShape(12.dp)
                ) {
                        Icon(
                                Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(stringResource(R.string.logout), fontWeight = FontWeight.Bold)
                }

                // Space for navigation bar
                Spacer(modifier = Modifier.height(100.dp))
        }
}

@Composable
fun EditProfileDialog(user: User, onDismiss: () -> Unit, onSave: (name: String) -> Unit) {
        var editName by remember { mutableStateOf(user.name) }

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
                        colors =
                                CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                )
                ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                                Text(
                                        text = "Editar Perfil",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(bottom = 24.dp)
                                )

                                OutlinedTextField(
                                        value = editName,
                                        onValueChange = { editName = it },
                                        label = { Text(stringResource(R.string.user_name)) },
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors =
                                                OutlinedTextFieldDefaults.colors(
                                                        focusedBorderColor = BluePrimary,
                                                        unfocusedBorderColor =
                                                                BluePrimary.copy(alpha = 0.3f)
                                                )
                                )

                                Row(
                                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                        OutlinedButton(
                                                onClick = onDismiss,
                                                modifier = Modifier.weight(1f).height(48.dp),
                                                shape = RoundedCornerShape(12.dp)
                                        ) { Text(stringResource(R.string.cancel)) }

                                        Button(
                                                onClick = { onSave(editName) },
                                                modifier = Modifier.weight(1f).height(48.dp),
                                                colors =
                                                        ButtonDefaults.buttonColors(
                                                                containerColor = GreenPrimary
                                                        ),
                                                shape = RoundedCornerShape(12.dp)
                                        ) { Text(stringResource(R.string.save)) }
                                }
                        }
                }
        }
}

@Composable
fun StatItem(label: String, value: String) {
        Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
        ) {
                Text(
                        text = value,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                )
                Text(
                        text = label,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                )
        }
}
