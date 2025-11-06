package com.example.semesterproject.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semesterproject.data.model.LoginRequest
import com.example.semesterproject.data.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// This data class holds all the "state" for the login screen
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val loginError: String? = null,
    val loginSuccess: Boolean = false
)

class LoginViewModel : ViewModel() {

    // Private mutable state
    private val _uiState = MutableStateFlow(LoginUiState())
    // Public read-only state
    val uiState: StateFlow<LoginUiState> = _uiState

    // Called when the email field changes
    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email, loginError = null)
    }

    // Called when the password field changes
    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(password = password, loginError = null)
    }

    // Called when the "Login" button is clicked
    fun loginUser() {
        // Start loading
        _uiState.value = _uiState.value.copy(isLoading = true, loginError = null)

        // Launch a coroutine in the ViewModel's scope
        viewModelScope.launch {
            try {
                val request = LoginRequest(
                    email = _uiState.value.email,
                    password = _uiState.value.password
                )

                // Make the network call
                val response = RetrofitInstance.api.login(request)

                if (response.isSuccessful && response.body() != null) {
                    // Success!
                    // TODO: Save the response.body().token to SharedPreferences
                    _uiState.value = _uiState.value.copy(isLoading = false, loginSuccess = true)
                } else {
                    // Server returned an error (e.g., "Invalid credentials")
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    _uiState.value = _uiState.value.copy(isLoading = false, loginError = errorMsg)
                }
            } catch (e: Exception) {
                // Network error (e.g., no internet, server down)
                _uiState.value = _uiState.value.copy(isLoading = false, loginError = e.message ?: "Network error")
            }
        }
    }
}