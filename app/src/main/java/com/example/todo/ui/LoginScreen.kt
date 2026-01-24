package com.example.todo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.todo.viewmodel.AuthViewModel

@Composable
fun LoginScreen(vm: AuthViewModel, onLogin: () -> Unit, onRegister: () -> Unit) {

    var mobile by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    LaunchedEffect(vm.user) { if (vm.user != null) onLogin() }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("To-Do Reminder", style = MaterialTheme.typography.headlineMedium)
        Text(text="Login", style = MaterialTheme.typography.headlineSmall)
        Text("Enter your mobile number to login")
        Text(text="Demo")
        OutlinedTextField(mobile, { mobile = it }, label = { Text("Mobile Number") })
        Button(onClick =
            { when {
                mobile.isBlank() -> error = "Enter mobile number"
                mobile.length != 10 -> error = "Enter valid 10 digit number"
                else -> vm.login(mobile)
            }
            }) { Text("Login") }
        if (error.isNotEmpty()) Text(error, color = MaterialTheme.colorScheme.error)
        TextButton(onClick = onRegister) { Text("New user? Register") }
    }
}
