package com.example.todo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
fun RegisterScreen(vm: AuthViewModel, onSuccess: () -> Unit) {

    var name by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    LaunchedEffect(vm.user) { if (vm.user != null) onSuccess() }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(name, { name = it }, label = { Text("Name") })
        OutlinedTextField(mobile, { mobile = it }, label = { Text("Mobile") }
        ,leadingIcon = { Text("+91 ") })
        Button(onClick = { when {
            name.isBlank() -> error = "Enter name"
            mobile.length != 10 -> error = "Enter valid mobile number"
            else -> vm.register(name, mobile)
        }
        }) { Text("Register") }
        if (error.isNotEmpty()) Text(error, color = MaterialTheme.colorScheme.error)
    }
}
