package com.example.semesterproject.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.semesterproject.navigation.Screen
import com.example.semesterproject.ui.theme.SemesterProjectTheme
// Import the ViewModel and the state
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.semesterproject.auth.viewmodel.LoginViewModel
import com.example.semesterproject.ui.theme.ErrorRed

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel() // This line injects the ViewModel
) {
    // Get the UI state from the ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // State for password visibility (this is purely UI, so it's ok to keep it here)
    var passwordVisibility by remember { mutableStateOf(false) }

    // This effect runs when uiState.loginSuccess changes to true
    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            // Navigate to the dashboard (which you'll create later)
            // For now, let's just pop back to Welcome
            navController.navigate(Screen.Welcome.route) { // TODO: Change to Dashboard
                popUpTo(Screen.Welcome.route) { inclusive = true }
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Logo
            Icon(
                imageVector = Icons.Default.Savings,
                contentDescription = "SmartSave Logo",
                tint = MaterialTheme.colorScheme.primary, // Pink
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "SmartSave",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Welcome Back", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(32.dp))

            // Email Field - Now powered by the ViewModel
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                isError = uiState.loginError != null // Show error state
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field - Now powered by the ViewModel
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    val image = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(imageVector = image, contentDescription = "Toggle password visibility")
                    }
                },
                isError = uiState.loginError != null // Show error state
            )

            // Show error message from the ViewModel
            uiState.loginError?.let { error ->
                Text(
                    text = error,
                    color = ErrorRed,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Forgot Password?
            TextButton(
                onClick = { navController.navigate(Screen.ForgotPassword.route) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Forgot Password?", color = MaterialTheme.colorScheme.secondary) // Purple
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = { viewModel.loginUser() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary // Pink
                ),
                enabled = !uiState.isLoading // Disable button when loading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(text = "Login", fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // "Don't have an account?" Link
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Don't have an account?")
                TextButton(
                    onClick = {
                        navController.navigate(Screen.SignUp.route) {
                            popUpTo(Screen.Welcome.route)
                        }
                    }
                ) {
                    Text(text = "Sign Up", color = MaterialTheme.colorScheme.secondary) // Purple
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SemesterProjectTheme {
        LoginScreen(navController = rememberNavController())
    }
}