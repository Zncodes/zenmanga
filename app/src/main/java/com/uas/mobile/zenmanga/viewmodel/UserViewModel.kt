package com.uas.mobile.zenmanga.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.uas.mobile.zenmanga.dao.UserDao
import com.uas.mobile.zenmanga.dao.UserDatabase
import com.uas.mobile.zenmanga.dao.auth.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao: UserDao = UserDatabase.getDatabase(application).userDao()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun login(identifier: String, password: String) {
        viewModelScope.launch {
            if (identifier.isBlank() || password.isBlank()) {
                _error.value = "Username/Email and password cannot be empty"
                return@launch
            }
            val user = userDao.getUserByUsername(identifier) ?: userDao.getUserByEmail(identifier)
            if (user != null && user.password == password) {
                _currentUser.value = user
                _isLoggedIn.value = true
                _error.value = null
            } else {
                _error.value = "Invalid username/email or password"
            }
        }
    }

    fun register(user: User) {
        viewModelScope.launch {
            if (user.username.isBlank() || user.email.isBlank() || user.password.isBlank()) {
                _error.value = "All fields must be filled"
                return@launch
            }
            userDao.insertUser(user)
            _currentUser.value = user
            _error.value = null
        }
    }

    fun logout() {
        _currentUser.value = null
        _isLoggedIn.value = false
    }
}
