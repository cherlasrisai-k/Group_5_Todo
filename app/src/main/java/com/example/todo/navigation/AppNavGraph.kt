package com.example.todo.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
    taskVM: TaskViewModel
) {
    var savedMobile by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(savedMobile) {
        savedMobile?.let { taskVM.setUser(it) }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN.route
    ) {

        composable(Routes.LOGIN.route) {
            LoginScreen(
                vm = authVM,
                navController = navController
            )
        }

        composable(Routes.REGISTER.route) {
            RegisterScreen(authVM,navController = navController)
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
                appNavController = navController
            )
        }
    }
}
