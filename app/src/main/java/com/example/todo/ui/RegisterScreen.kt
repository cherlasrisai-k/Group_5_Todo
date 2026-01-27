package com.example.todo.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import com.example.todo.R
import com.example.todo.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(vm: AuthViewModel, onSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    LaunchedEffect(vm.user) {
        if (vm.user != null) onSuccess()
    }

    val configurations=LocalConfiguration.current

    val screenWidth=configurations.screenWidthDp

    val fontSize = when {
        screenWidth > 400 -> 24.sp
        screenWidth > 320 -> 20.sp
        else -> 16.sp
    }
    val dynamicFontSize = (screenWidth / 18).sp

    val  context= LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(getString(context, R.string.registerScreen_Title), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(text=getString(context,R.string.registerScreen_Heading),style=MaterialTheme.typography.headlineSmall ,fontSize=dynamicFontSize)

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
                Text(text=getString(context,R.string.registerScreen_CardTitle), style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(16.dp))


                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        error = ""
                    },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") },
                    label = { Text(text=getString(context,R.string.TextFields_Name)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors= OutlinedTextFieldDefaults.colors(
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = mobile,
                    onValueChange = {
                        mobile = it
                        error = ""
                    },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Mobile") },
                    label = { Text(text=getString(context,R.string.TextFields_MobileNumber)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors= OutlinedTextFieldDefaults.colors(
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (error.isNotEmpty()) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Button(
                    onClick = {
                        when {
                            name.isBlank() -> error = context.getString(R.string.RegisterScreen_NameValidation)
                            mobile.length != 10 -> error = context.getString(R.string.RegisterScreen_MobileLengthValidation)
                            else -> vm.register(name, mobile)
                        }

                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text=getString(context,R.string.Buttons_Register))
                }
            }
        }
    }
}