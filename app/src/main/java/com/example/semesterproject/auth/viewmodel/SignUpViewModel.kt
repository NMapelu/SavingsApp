package com.example.semesterproject.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semesterproject.data.model.SignUpRequest
import com.example.semesterproject.data.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// State for the Sign Up screen
data class SignUpUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val signUpError: String? = null,
    val passwordsMatch: Boolean = true,
    val signUpSuccess: Boolean = false
)

class SignUpViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState

    // --- Event Handlers for UI ---
    fun onFullNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(fullName = name, signUpError = null)
    }

    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email, signUpError = null)
    }

    fun onPasswordChanged(password: String) {
        val passwordsMatch = password == _uiState.value.confirmPassword
        _uiState.value = _uiState.value.copy(password = password, passwordsMatch = passwordsMatch, signUpError = null)
    }

    fun onConfirmPasswordChanged(confirm: String) {
        val passwordsMatch = _uiState.value.password == confirm
        _uiState.value = _uiState.value.copy(confirmPassword = confirm, passwordsMatch = passwordsMatch, signUpError = null)
    }

    // --- Business Logic ---
    fun signUpUser() {
        // First, check if passwords match
        if (!_uiState.value.passwordsMatch) {
            _uiState.value = _uiState.value.copy(signUpError = "Passwords do not match")
            return
        }

        // Check for empty fields (you can add more validation)
        if (_uiState.value.email.isBlank() || _uiState.value.password.isBlank() || _uiState.value.fullName.isBlank()) {
            _uiState.value = _uiState.value.copy(signUpError = "All fields are required")
            return
        }

        // Start loading
        _uiState.value = _uiState.value.copy(isLoading = true, signUpError = null)

        viewModelScope.launch {
            try {
                val request = SignUpRequest(
                    fullName = _uiState.value.fullName,
                    email = _uiState.value.email,
                    password = _uiState.value.password
                )

                val response = RetrofitInstance.api.signUp(request)

                if (response.isSuccessful) {
                    // Success!
                    _uiState.value = _uiState.value.copy(isLoading = false, signUpSuccess = true)
                } else {
                    // Server error (e.g., "Email already in use")
                    val errorMsg = response.errorBody()?.string() ?: "Sign-up failed"
                    _uiState.value = _uiState.value.copy(isLoading = false, signUpError = errorMsg)
                }
            } catch (e: Exception) {
                // Network error
                _uiState.value = _uiState.value.copy(isLoading = false, signUpError = e.message ?: "Network error")
            }
        }
    }
}