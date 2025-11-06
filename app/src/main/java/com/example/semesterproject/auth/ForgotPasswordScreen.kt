package com.example.semesterproject.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.semesterproject.navigation.Screen
import com.example.semesterproject.ui.theme.SemesterProjectTheme

@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            // Icon
            Icon(
                imageVector = Icons.Default.LockReset, // A fitting icon
                contentDescription = "Forgot Password",
                tint = MaterialTheme.colorScheme.primary, // Pink
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Forgot Your Password?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No problem! Enter the email address you used to sign up, and we'll send you a link to reset it.",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("you@example.com") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon")
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Send Reset Link Button
            Button(
                onClick = {
                    // TODO: Phase 2 - Call backend API to send reset email
                    // For now, just go back to the Login screen
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary // Pink
                )
            ) {
                Text(text = "Send Reset Link", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // "Back to Log In" Link
            TextButton(
                onClick = {
                    // This just goes back to the previous screen (which is Login)
                    navController.popBackStack(Screen.Login.route, inclusive = false)
                }
            ) {
                Text(text = "Back to Log In", color = MaterialTheme.colorScheme.secondary) // Purple
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    SemesterProjectTheme {
        ForgotPasswordScreen(navController = rememberNavController())
    }
}