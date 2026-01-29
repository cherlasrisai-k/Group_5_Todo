package com.example.todo.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todo.navigation.Routes
import com.example.todo.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(vm: AuthViewModel, navController: NavController) {


    val state by vm.LoginRegister.collectAsState()

    val scrollState = rememberScrollState()

    LaunchedEffect(vm.user) {
        if (vm.user != null) {
            navController.navigate(Routes.MAIN.route) {
                popUpTo(Routes.REGISTER.route) { inclusive = true }
            }

        }
    }

    val configurations=LocalConfiguration.current

    val screenWidth=configurations.screenWidthDp

    val fontSize = when {
        screenWidth > 400 -> 24.sp
        screenWidth > 320 -> 20.sp
        else -> 16.sp
    }
    val dynamicFontSize = (screenWidth / 18).sp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Get Started", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Manage your tasks effortlessly",style=MaterialTheme.typography.headlineSmall ,fontSize=dynamicFontSize)

        Spacer(modifier = Modifier.height(16.dp))


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            elevation = CardDefaults.cardElevation(5.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Create Account", style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(16.dp))


                OutlinedTextField(
                    value = state.name,
                    onValueChange = {
                        vm.onNameChange(it)
                    },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors= OutlinedTextFieldDefaults.colors(
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.mobile,
                    onValueChange = {
                        if (it.length <= 10 && it.all { ch -> ch.isDigit() }) {
                            vm.onMobileChange(it)
                        }
                    },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Mobile") },
                    label = { Text("Mobile Number") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors= OutlinedTextFieldDefaults.colors(
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (!vm.RegisterError.isNullOrEmpty()) {
                    Text(
                        text = vm.RegisterError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Button(
                    onClick = {
                        vm.validateAndRegister(state.name,state.mobile)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Register")
                }
            }
        }
    }
}