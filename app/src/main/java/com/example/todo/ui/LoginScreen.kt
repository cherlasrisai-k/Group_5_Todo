package com.example.todo.ui

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todo.R
import com.example.todo.navigation.Routes
import com.example.todo.viewmodel.AuthViewModel


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun LoginScreen(
    vm: AuthViewModel,
    navController: NavController,
    activity: ComponentActivity
) {

    val state by vm.LoginRegister.collectAsState()

    val error = vm.loginError


    val scrollState = rememberScrollState()
    LaunchedEffect(vm.user) {
        if (vm.user != null) {
            navController.navigate(Routes.MAIN.route) {
                popUpTo(Routes.LOGIN.route) { inclusive = true }
            }

        }
    }


    val windowSizeClass = calculateWindowSizeClass(activity)

    val fontSize = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Expanded -> 24.sp
        WindowWidthSizeClass.Medium -> 20.sp
        else -> 16.sp
    }



    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f),
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                modifier  =  Modifier.fillMaxWidth(),
                painter = painterResource(R.drawable.updatedtodo),
                contentDescription = "TODOImage",
                        contentScale = androidx.compose.ui.layout.ContentScale.FillWidth
            )
        }


        Box(
            modifier = Modifier
                .padding(16.dp)
                .weight(2f),
                contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.LoginScreen_Title),
                    style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = stringResource(R.string.LoginScreen_Heading),
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = fontSize),
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .weight(4f)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                Text(stringResource(R.string.LoginScreen_LoginText), style = MaterialTheme.typography.labelMedium)

                OutlinedTextField(
                    value = state.mobile,
                    onValueChange = {
                        if (it.length <= 10 && it.all { ch -> ch.isDigit() }) {
                            vm.onMobileChange(it)
                        }
                    },
                    label = { Text(stringResource(R.string.TextFields_MobileNumber), modifier = Modifier.alpha(0.5f)) },
                    leadingIcon = { Text("+91")},
                     isError =!vm.loginError.isNullOrEmpty(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    colors= OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        focusedLabelColor =Color.Black,
                        unfocusedBorderColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedTextColor = Color.Black,
                        errorBorderColor = MaterialTheme.colorScheme.error
                    )
                )




                TextButton( onClick = {
                        vm.validateAndLogin(state.mobile)
                }) {
                    Text(stringResource(R.string.Buttons_Login),color=Color.Black, fontWeight = FontWeight.SemiBold)
                }

                if (!vm.loginError.isNullOrEmpty())
                    Text(vm.loginError!!, color = MaterialTheme.colorScheme.primary )

                TextButton(onClick = {
                    vm.clearLoginState()
                    navController.navigate(Routes.REGISTER.route)

                }) {
                    Text(stringResource(R.string.LoginScreen_Registration), color = Color.Black,fontWeight = FontWeight.Bold)
                }
            }
        }


       Box(modifier = Modifier
           .fillMaxSize()
           .weight(1f),
           contentAlignment = Alignment.BottomCenter){
           Row(verticalAlignment = Alignment.CenterVertically){
               Text(text = stringResource(R.string.LoginScreen_Terms))
           }
       }


    }
        }

