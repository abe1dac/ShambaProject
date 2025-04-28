package com.arnold.myapplication.viewmodels

import androidx.lifecycle.ViewModel
import com.arnold.myapplication.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    suspend fun register(email: String, password: String, name: String) {
        _authState.value = AuthState.Loading
        try {
            val userId = userRepository.registerUser(email, password, name)
            _authState.value = AuthState.Success(userId)
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.message ?: "Registration failed")
        }
    }

    // Similar for login
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val userId: Long) : AuthState()
    data class Error(val message: String) : AuthState()
}