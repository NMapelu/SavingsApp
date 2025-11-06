package com.example.semesterproject.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.semesterproject.auth.WelcomeScreen
import com.example.semesterproject.auth.LoginScreen
import com.example.semesterproject.auth.SignUpScreen
import com.example.semesterproject.auth.ForgotPasswordScreen

// 1. Define all your screen "routes" as objects
sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object ForgotPassword : Screen("forgot_password")
    // Add other screens here later, e.g., object Dashboard : Screen("dashboard")
}

// 2. Create the App's Navigation Host
@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route // The first screen users see
    ) {
        // Welcome Screen
        composable(Screen.Welcome.route) {
            WelcomeScreen(navController = navController)
        }

        // Login Screen
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        // Sign Up Screen
        composable(Screen.SignUp.route) {
            SignUpScreen(navController = navController)
        }

        // Forgot Password Screen
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController)
        }

        // TODO: Add composable(Screen.Dashboard.route) { ... }
        // after authentication is successful
    }
}