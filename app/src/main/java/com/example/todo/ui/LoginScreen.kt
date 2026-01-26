package com.example.todo.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo.R
import com.example.todo.viewmodel.AuthViewModel


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun LoginScreen(
    vm: AuthViewModel,
    onLogin: () -> Unit,
    onRegister: () -> Unit
) {

    var mobile by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    LaunchedEffect(vm.user) {
        if (vm.user != null) onLogin()
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    // Dynamically calculate font size
    val fontSize = when {
        screenWidth > 400 -> 24.sp   // large screen/tablet
        screenWidth > 320 -> 20.sp   // normal phone
        else -> 16.sp                 // small phone
    }



    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f),
            contentAlignment = Alignment.TopStart
        ) {
            Image(
                painter = painterResource(R.drawable.updatedtodo),
                contentDescription = "TODOImage"
            )
        }


        Column(
            modifier = Modifier
                .padding(16.dp)
                .weight(2f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome Back \uD83D\uDC4B",
                style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold
            )
            Text(
                text = "Let’s plan something great today",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = fontSize),
                overflow = TextOverflow.Ellipsis,
            )
        }

        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .weight(4f)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                Text("Log in or sign up", style = MaterialTheme.typography.labelMedium)

                OutlinedTextField(
                    value = mobile,
                    onValueChange = {
                        if (it.length <= 10 && it.all { ch -> ch.isDigit() }) {
                            mobile = it
                        }
                    },
                    label = { Text("Mobile Number") },
                    leadingIcon = { Text("+91 ") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    colors= OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )




                TextButton( onClick = {
                    error = ""
                    when {
                        mobile.isBlank() -> error = "Enter mobile number"
                        mobile.length != 10 -> error = "Enter valid 10 digit number"
                        else -> vm.login("$mobile")
                    }
                }) {
                    Text("Login")
                }

                if (error.isNotEmpty())
                    Text(error, color = MaterialTheme.colorScheme.error)

                TextButton(onClick = onRegister) {
                    Text("New user? Register")
                }
            }
        }


       Box(modifier = Modifier
           .fillMaxSize()
           .weight(1f),
           contentAlignment = Alignment.BottomCenter){
           Row() {
               Text(text = " All rights Reserved")
           }
       }


    }
        }

