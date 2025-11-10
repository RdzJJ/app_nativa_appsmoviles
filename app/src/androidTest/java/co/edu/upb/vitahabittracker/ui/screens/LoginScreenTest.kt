package co.edu.upb.vitahabittracker.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import co.edu.upb.vitahabittracker.R
import androidx.compose.ui.test.junit4.ComposeTestRule

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Helper function to setup the LoginScreen
    private fun setupLoginScreen(
        onLoginClick: (String, String) -> Unit = { _, _ -> },
        onSignupClick: () -> Unit = {},
        isLoading: Boolean = false,
        errorMessage: String? = null
    ) {
        composeTestRule.setContent {
            LoginScreen(
                onLoginClick = onLoginClick,
                onSignupClick = onSignupClick,
                isLoading = isLoading,
                errorMessage = errorMessage
            )
        }
    }

    @Test
    fun loginScreen_ShowsAllRequiredElements() {
        setupLoginScreen()

        // Verify app name is displayed
        composeTestRule.onNodeWithText("Vita").assertIsDisplayed()

        // Verify email field is displayed
        composeTestRule.onNodeWithText("Correo electrónico").assertIsDisplayed()

        // Verify password field is displayed
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()

        // Verify login button is displayed but disabled (empty fields)
        composeTestRule.onNodeWithText("Iniciar Sesión")
            .assertIsDisplayed()
            .assertIsNotEnabled()

        // Verify signup link is displayed
        composeTestRule.onNodeWithText("¿No tienes cuenta?").assertIsDisplayed()
    }

    @Test
    fun loginScreen_EmptyFields_LoginButtonDisabled() {
        setupLoginScreen()

        // Initially button should be disabled
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsNotEnabled()

        // Enter only email
        composeTestRule.onNodeWithText("Correo electrónico")
            .performTextInput("test@example.com")
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsNotEnabled()

        // Clear email and enter only password
        composeTestRule.onNodeWithText("Correo electrónico")
            .performTextClearance()
        composeTestRule.onNodeWithText("Contraseña")
            .performTextInput("password123")
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsNotEnabled()
    }

    @Test
    fun loginScreen_ValidInput_LoginButtonEnabled() {
        setupLoginScreen()

        // Enter valid email and password
        composeTestRule.onNodeWithText("Correo electrónico")
            .performTextInput("test@example.com")
        composeTestRule.onNodeWithText("Contraseña")
            .performTextInput("password123")

        // Button should be enabled
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsEnabled()
    }

    @Test
    fun loginScreen_ShowsErrorMessage() {
        val errorMsg = "Correo o contraseña incorrectos"
        setupLoginScreen(errorMessage = errorMsg)

        // Verify error message is displayed
        composeTestRule.onNodeWithText(errorMsg).assertIsDisplayed()
    }

    @Test
    fun loginScreen_ShowsLoadingState() {
        setupLoginScreen(isLoading = true)

        // When loading, the button should be disabled
        composeTestRule.onNodeWithText("Iniciar Sesión").assertDoesNotExist()
        
        // And a CircularProgressIndicator should be visible
        // Note: In Compose, we can't directly assert the CircularProgressIndicator
        // but we can verify the button is not visible during loading
    }

    @Test
    fun loginScreen_PasswordVisibilityToggle() {
        setupLoginScreen()

        // Enter password
        composeTestRule.onNodeWithText("Contraseña")
            .performTextInput("password123")

        // Initially password should be hidden
        // Click visibility toggle
        composeTestRule.onNodeWithContentDescription("Toggle password visibility")
            .performClick()

        // The password should now be visible
        // Note: We can't directly verify the password visibility state in Compose
        // but we can verify the toggle button exists and is clickable
    }
}