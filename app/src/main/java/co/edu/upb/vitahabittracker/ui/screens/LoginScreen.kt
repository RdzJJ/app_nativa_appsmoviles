package co.edu.upb.vitahabittracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.upb.vitahabittracker.R
import co.edu.upb.vitahabittracker.ui.theme.GreenPrimary
import co.edu.upb.vitahabittracker.ui.theme.BluePrimary
import co.edu.upb.vitahabittracker.ui.theme.TealPrimary

@Composable
fun LoginScreen(
    onLoginClick: (email: String, password: String) -> Unit,
    onSignupClick: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        GreenPrimary,
                        BluePrimary
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo / App Name
            Text(
                text = "Vita",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.app_name),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                modifier = Modifier.padding(bottom = 40.dp)
            )

            // Welcome Message
            Text(
                text = stringResource(R.string.login_title),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.login_subtitle),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // White Card for Form
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(stringResource(R.string.email_hint)) },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Email,
                                contentDescription = null,
                                tint = BluePrimary
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = BluePrimary.copy(alpha = 0.3f)
                        )
                    )

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(stringResource(R.string.password_hint)) },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Lock,
                                contentDescription = null,
                                tint = BluePrimary
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = null,
                                    tint = BluePrimary
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = BluePrimary.copy(alpha = 0.3f)
                        )
                    )

                    // Error Message
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.errorContainer,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp)
                        )
                    }

                    // Login Button
                    Button(
                        onClick = { onLoginClick(email, password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(bottom = 12.dp),
                        enabled = !isLoading && email.isNotEmpty() && password.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GreenPrimary,
                            disabledContainerColor = GreenPrimary.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                stringResource(R.string.login_button),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Signup Link
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.dont_have_account),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        TextButton(onClick = onSignupClick) {
                            Text(
                                stringResource(R.string.signup_button),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = BluePrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
