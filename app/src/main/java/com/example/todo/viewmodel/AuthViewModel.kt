package com.example.todo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.TaskDao
import com.example.todo.data.User
import com.example.todo.data.UserDao
import com.example.todo.states.LoginRegisterUIStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userDao: UserDao,
    private val taskDao: TaskDao
) : ViewModel() {

    private val _loginRegister = MutableStateFlow(LoginRegisterUIStates())
    val loginRegister = _loginRegister.asStateFlow()

    var user by mutableStateOf<User?>(null)
        private set

    var loginError by mutableStateOf<String?>(null)
        private set

    var registerError by mutableStateOf<String?>(null)
        private set

    fun login(mobile: String) = viewModelScope.launch {
        val result = userDao.login(mobile)
        if (result == null) {
            loginError = "User does not exist. Please register."
        } else {
            user = result.copy(isLoggedIn = true)
            userDao.updateUser(user!!)
            loginError = null
            _loginRegister.value = LoginRegisterUIStates()
        }
    }

    fun register(name: String, mobile: String) = viewModelScope.launch {
        registerError = null
        val existingUser = userDao.login(mobile)

        if (existingUser == null) {
            // New user → insert
            val newUser = User(mobile, name, isLoggedIn = true)
            userDao.register(newUser)

            // Fetch again after insert
            user = userDao.login(mobile)

        } else {
            // Already exists
            user = null
            registerError = "User already exists. Please login."
        }
    }


    fun logout() = viewModelScope.launch {
        userDao.logout()
        user = null
        loginError = null
        registerError = null
    }

    suspend fun deleteAccount() = withContext(Dispatchers.IO) {
        user?.let {
            taskDao.deleteTasksByUser(it.mobile)
            userDao.deleteUser(it)
            user = null
        }
    }

    fun validateAndLogin(mobile: String) {
        when {
            mobile.isBlank() -> loginError = "Enter mobile number"
            mobile.length != 10 -> loginError = "Enter valid 10 digit number"
            else -> login(mobile)
        }
    }


    fun validateAndRegister(name: String, mobile: String) {
        registerError = null

        when {
            name.isBlank() -> registerError = "Name cannot be empty"
            mobile.length != 10 -> registerError = "Enter a valid 10-digit mobile number"
            else -> register(name, mobile)
        }
    }

    fun onMobileChange(value: String) {
        val digitsOnly = value.filter { it.isDigit() }
        if (digitsOnly.length <= 10) {
            _loginRegister.value = _loginRegister.value.copy(mobile = digitsOnly)
        }
    }

    fun onNameChange(value: String) {
        _loginRegister.value = _loginRegister.value.copy(name = value)
    }

    fun clearLoginState() {
        _loginRegister.value = LoginRegisterUIStates()
        loginError = null
    }

    fun clearRegisterState() {
        _loginRegister.value = LoginRegisterUIStates()
        registerError = null
    }

    suspend fun getLoggedInUser(): User? {
        return userDao.getLoggedInUser()
    }
    fun autoLogin(loggedInUser: User) {
        user = loggedInUser
    }



}
