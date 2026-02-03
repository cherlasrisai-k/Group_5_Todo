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

    private fun getPasswordValidationErrors(password: String): List<String> {
        val errors = mutableListOf<String>()
        if (password.length < 8) {
            errors.add("be at least 8 characters long")
        }
        if (!password.any { it.isUpperCase() }) {
            errors.add("contain an uppercase letter")
        }
        if (!password.any { it.isLowerCase() }) {
            errors.add("contain a lowercase letter")
        }
        if (!password.any { it.isDigit() }) {
            errors.add("contain a number")
        }
        if (password.all { it.isLetterOrDigit() }) {
            errors.add("contain a special character")
        }
        return errors
    }

    fun login(mobile: String, password: String) = viewModelScope.launch {
        val result = userDao.login(mobile, password)
        if (result == null) {
            loginError = "Invalid mobile number or password."
        } else {
            user = result.copy(isLoggedIn = true)
            userDao.updateUser(user!!)
            loginError = null
            _loginRegister.value = LoginRegisterUIStates()
        }
    }

    fun register(name: String, mobile: String, password: String) = viewModelScope.launch {
        val newUser = User(mobile, name, password, isLoggedIn = true)
        userDao.register(newUser)
        user = userDao.getUserByMobile(mobile)
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

    fun validateAndLogin(mobile: String, password: String) {
        loginError = null
        _loginRegister.value = _loginRegister.value.copy(passwordError = null)
        val passwordErrors = getPasswordValidationErrors(password)

        when {
            mobile.isBlank() -> loginError = "Enter mobile number"
            mobile.length != 10 -> loginError = "Enter valid 10 digit number"
            password.isBlank() -> _loginRegister.value = _loginRegister.value.copy(passwordError = "Enter password")
            passwordErrors.isNotEmpty() -> {
                _loginRegister.value = _loginRegister.value.copy(passwordError = "Password must " + passwordErrors.joinToString(", "))
            }
            else -> login(mobile, password)
        }
    }


    fun validateAndRegister(
        name: String,
        mobile: String,
        password: String,
        confirmPassword: String
    ) {
        registerError = null
        val passwordErrors = getPasswordValidationErrors(password)

        when {
            name.isBlank() -> registerError = "Name cannot be empty"
            mobile.length != 10 -> registerError = "Enter a valid 10-digit mobile number"
            password.isBlank() -> registerError = "Enter password"
            passwordErrors.isNotEmpty() -> {
                registerError = "Password must " + passwordErrors.joinToString(", ")
            }
            confirmPassword.isBlank() -> registerError = "Confirm password"
            password != confirmPassword -> registerError = "Passwords do not match"
            else -> {
                viewModelScope.launch {
                    val existingUser = userDao.getUserByMobile(mobile)
                    if (existingUser != null) {
                        registerError = "User already exists. Please login."
                    } else {
                        register(name, mobile, password)
                    }
                }
            }
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

    fun onPasswordChange(value: String) {
        _loginRegister.value = _loginRegister.value.copy(password = value, passwordError = null)
    }

    fun onConfirmPasswordChange(value: String) {
        _loginRegister.value = _loginRegister.value.copy(confirmPassword = value)
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
