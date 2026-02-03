package com.example.todo.ui

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale.Companion.FillWidth
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todo.R
import com.example.todo.navigation.Routes
import com.example.todo.viewmodel.AuthViewModel


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun LoginScreen(
    vm: AuthViewModel = hiltViewModel(),
    navController: NavController,
) {

    val state by vm.loginRegister.collectAsState()

    LaunchedEffect(vm.user) {
        if (vm.user != null) {
            navController.navigate(
                Routes.MAIN.withUser(vm.user!!.mobile)
            ) {
                popUpTo(Routes.LOGIN.route) { inclusive = true }
            }
        }
    }

    var passwordVisible by remember { mutableStateOf(false) }

    LoginScreenContent(
        mobile = state.mobile,
        password = state.password,
        loginError = vm.loginError,
        passwordError = state.passwordError,
        onMobileChange = vm::onMobileChange,
        onPasswordChange = vm::onPasswordChange,
        onLogin = { vm.validateAndLogin(state.mobile, state.password) },
        onGoToRegister = {
            vm.clearLoginState()
            navController.navigate(Routes.REGISTER.route)
        },
        passwordVisible = passwordVisible,
        onPasswordVisibilityChange = { passwordVisible = !passwordVisible }
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun LoginScreenContent(
    mobile: String,
    password: String,
    loginError: String?,
    passwordError: String?,
    onMobileChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onGoToRegister: () -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: () -> Unit
) {
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val windowSizeClass = if (context is Activity) {
        calculateWindowSizeClass(activity = context)
    } else {
        // Provide a default for previews
        null
    }

    val fontSize = when (windowSizeClass?.widthSizeClass) {
        WindowWidthSizeClass.Expanded -> 24.sp
        WindowWidthSizeClass.Medium -> 20.sp
        else -> 16.sp
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(R.drawable.updatedtodo),
                    contentDescription = "TODOImage",
                    contentScale = FillWidth
                )
            }


            Box(
                modifier = Modifier.padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.LoginScreen_Title),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = stringResource(R.string.LoginScreen_Heading),
                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = fontSize),
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Spacer(modifier = Modifier.padding(16.dp))

            Column(
                modifier = Modifier.padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                Text(
                    stringResource(R.string.LoginScreen_LoginText),
                    style = MaterialTheme.typography.labelMedium
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = mobile,
                    onValueChange = onMobileChange,
                    label = {
                        Text(
                            stringResource(R.string.TextFields_MobileNumber),
                            modifier = Modifier.alpha(0.5f)
                        )
                    },
                    leadingIcon = { Text(stringResource(R.string.LoginScreen_LeadingText)) },
                    isError = !loginError.isNullOrEmpty(),
                    textStyle = TextStyle(color = Color.Black),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color.Black,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        focusedLabelColor = Color.Black,
                    )
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
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
                    isError = !loginError.isNullOrEmpty() || !passwordError.isNullOrEmpty(),
                    textStyle = TextStyle(color = Color.Black),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color.Black,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        focusedLabelColor = Color.Black,
                    )
                )
                if (!passwordError.isNullOrEmpty()) {
                    Text(
                        text = passwordError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                TextButton(onClick = onLogin) {
                    Text(
                        stringResource(R.string.Buttons_Login),
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (!loginError.isNullOrEmpty()) Text(
                    loginError, color = MaterialTheme.colorScheme.primary
                )

                TextButton(onClick = onGoToRegister) {
                    Text(
                        stringResource(R.string.LoginScreen_Registration),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.LoginScreen_Terms))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreenContent(
        mobile = "",
        password = "",
        loginError = null,
        passwordError = null,
        onMobileChange = {},
        onPasswordChange = {},
        onLogin = {},
        onGoToRegister = {},
        passwordVisible = false,
        onPasswordVisibilityChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenErrorPreview() {
    LoginScreenContent(
        mobile = "12345",
        password = "",
        loginError = "User does not exist. Please register.",
        passwordError = "Password cannot be empty",
        onMobileChange = {},
        onPasswordChange = {},
        onLogin = {},
        onGoToRegister = {},
        passwordVisible = false,
        onPasswordVisibilityChange = {}
    )
}
