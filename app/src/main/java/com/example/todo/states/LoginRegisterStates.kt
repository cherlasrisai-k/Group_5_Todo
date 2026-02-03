package com.example.todo.states

data class LoginRegisterUIStates(
    val name: String = "",
    val mobile: String = "",
    val otp: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordError: String? = null
)