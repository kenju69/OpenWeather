package com.openweather.exam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openweather.exam.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: WeatherRepository) : ViewModel() {
    private val _authResult = MutableStateFlow<Boolean?>(null)
    val authResult: StateFlow<Boolean?> = _authResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authResult.value = repo.loginUser(username, password)
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            repo.registerUser(username, password)
            _authResult.value = true
        }
    }
}
