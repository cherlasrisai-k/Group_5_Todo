package com.example.todo.ui

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todo.R
import com.example.todo.navigation.Routes
import com.example.todo.states.LoginRegisterUIStates
import com.example.todo.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(vm: AuthViewModel = hiltViewModel(), navController: NavController) {

    val state by vm.loginRegister.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(vm.user) {
        if (vm.user != null) {
            navController.navigate(
                Routes.MAIN.withUser(vm.user!!.mobile)
            ) {

                popUpTo(Routes.LOGIN.route) { inclusive = true }
            }
        }
    }

    RegisterScreenContent(
        state = state,
        registerError = vm.registerError,
        onNameChange = vm::onNameChange,
        onMobileChange = vm::onMobileChange,
        onPasswordChange = vm::onPasswordChange,
        onConfirmPasswordChange = vm::onConfirmPasswordChange,
        onRegister = {
            vm.validateAndRegister(
                state.name,
                state.mobile,
                state.password,
                state.confirmPassword
            )
        },
        onBack = {
            vm.clearLoginState()
            vm.clearRegisterState()
            navController.popBackStack()
        },
        passwordVisible = passwordVisible,
        onPasswordVisibilityChange = { passwordVisible = !passwordVisible },
        confirmPasswordVisible = confirmPasswordVisible,
        onConfirmPasswordVisibilityChange = { confirmPasswordVisible = !confirmPasswordVisible }
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun RegisterScreenContent(
    state: LoginRegisterUIStates,
    registerError: String?,
    onNameChange: (String) -> Unit,
    onMobileChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegister: () -> Unit,
    onBack: () -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: () -> Unit,
    confirmPasswordVisible: Boolean,
    onConfirmPasswordVisibilityChange: () -> Unit
) {
    BackHandler { onBack() }

    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val windowSizeClass = if (context is Activity) {
        calculateWindowSizeClass(activity = context)
    } else {
        null
    }

    val dynamicFontSize = when (windowSizeClass?.widthSizeClass) {
        WindowWidthSizeClass.Expanded -> 24.sp
        WindowWidthSizeClass.Medium -> 20.sp
        else -> 16.sp
    }
    val cardWidthMultiplier = when (windowSizeClass?.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 1f
        WindowWidthSizeClass.Medium -> 0.7f
        WindowWidthSizeClass.Expanded -> 0.5f
        else -> 1f
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            stringResource(R.string.registerScreen_Title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            stringResource(R.string.registerScreen_Heading),
            style = MaterialTheme.typography.headlineSmall,
            fontSize = dynamicFontSize
        )

        Spacer(modifier = Modifier.height(16.dp))


        Card(
            modifier = Modifier
                .fillMaxWidth(cardWidthMultiplier)
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
                Text(
                    stringResource(R.string.registerScreen_CardTitle),
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                val isFieldError = !registerError.isNullOrEmpty()
                val textFieldColors = if (isSystemInDarkTheme()) Color.White else Color.Black

                OutlinedTextField(
                    value = state.name,
                    onValueChange = onNameChange,
                    isError = isFieldError,
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") },
                    label = {
                        Text(
                            stringResource(R.string.TextFields_Name),
                            modifier = Modifier.alpha(0.5f)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = TextStyle(color = textFieldColors),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = textFieldColors,
                        focusedBorderColor = textFieldColors,
                        unfocusedBorderColor = textFieldColors,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        focusedLabelColor = textFieldColors,
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.mobile,
                    onValueChange = onMobileChange,
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Mobile") },
                    label = {
                        Text(
                            stringResource(R.string.TextFields_MobileNumber),
                            modifier = Modifier.alpha(0.5f)
                        )
                    },
                    isError = isFieldError,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = TextStyle(color = textFieldColors),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = textFieldColors,
                        focusedBorderColor = textFieldColors,
                        unfocusedBorderColor = textFieldColors,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        focusedLabelColor = textFieldColors,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.password,
                    onValueChange = onPasswordChange,
                    label = {
                        Text(
                            stringResource(R.string.TextFields_Password),
                            modifier = Modifier.alpha(0.5f)
                        )
                    },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        IconButton(onClick = onPasswordVisibilityChange){
                            Icon(imageVector  = image, "")
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = isFieldError,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = TextStyle(color = textFieldColors),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = textFieldColors,
                        focusedBorderColor = textFieldColors,
                        unfocusedBorderColor = textFieldColors,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        focusedLabelColor = textFieldColors,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = {
                        Text(
                            stringResource(R.string.TextFields_ConfirmPassword),
                            modifier = Modifier.alpha(0.5f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "Confirm Password"
                        )
                    },
                    trailingIcon = {
                        val image = if (confirmPasswordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        IconButton(onClick = onConfirmPasswordVisibilityChange){
                            Icon(imageVector  = image, "")
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = isFieldError,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = TextStyle(color = textFieldColors),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = textFieldColors,
                        focusedBorderColor = textFieldColors,
                        unfocusedBorderColor = textFieldColors,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        focusedLabelColor = textFieldColors,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    keyboardActions = KeyboardActions(onDone = { onRegister() })
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isFieldError) {
                    Text(
                        text = registerError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Button(
                    onClick = onRegister,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(stringResource(R.string.Buttons_Register))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreenContent(
        state = LoginRegisterUIStates(),
        registerError = null,
        onNameChange = {},
        onMobileChange = {},
        onPasswordChange = {},
        onConfirmPasswordChange = {},
        onRegister = {},
        onBack = {},
        passwordVisible = false,
        onPasswordVisibilityChange = {},
        confirmPasswordVisible = false,
        onConfirmPasswordVisibilityChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenErrorPreview() {
    RegisterScreenContent(
        state = LoginRegisterUIStates(name = "John Doe", mobile = "123"),
        registerError = "Enter a valid 10-digit mobile number",
        onNameChange = {},
        onMobileChange = {},
        onPasswordChange = {},
        onConfirmPasswordChange = {},
        onRegister = {},
        onBack = {},
        passwordVisible = false,
        onPasswordVisibilityChange = {},
        confirmPasswordVisible = false,
        onConfirmPasswordVisibilityChange = {}
    )
}
