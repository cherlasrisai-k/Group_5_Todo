package com.example.todo.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todo.MainScaffold
import com.example.todo.ui.LoginScreen
import com.example.todo.ui.RegisterScreen
import com.example.todo.viewmodel.AuthViewModel
import com.example.todo.viewmodel.TaskViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    authVM: AuthViewModel,
    taskVM: TaskViewModel,
    startDestination: String,
    activity: ComponentActivity
) {
    var savedMobile by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(savedMobile) {
        savedMobile?.let { taskVM.setUser(it) }
    }

    NavHost(
        navController = navController, startDestination = startDestination
    ) {

        composable(Routes.LOGIN.route) {
            LoginScreen(
                vm = authVM,
                navController = navController,
                activity
            )
        }

        composable(Routes.REGISTER.route) {
            RegisterScreen(authVM,navController = navController,activity)
        }

        composable(Routes.MAIN.route) {
            LaunchedEffect(authVM.user) {
                authVM.user?.let {
                    taskVM.setUser(it.mobile)
                }
            }


            MainScaffold(
                taskVM = taskVM,
                authVM = authVM,
                appNavController = navController,
                activity
            )
        }
    }
}
