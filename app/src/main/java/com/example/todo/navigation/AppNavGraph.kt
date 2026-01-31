package com.example.todo.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
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
    authViewModel: AuthViewModel,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.SPLASH.route) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            LaunchedEffect(Unit) {
                val loggedInUser = authViewModel.getLoggedInUser()
                if (loggedInUser != null) {
                    authViewModel.autoLogin(loggedInUser)
                    navController.navigate(Routes.MAIN.withUser(loggedInUser.mobile)) {
                        popUpTo(Routes.SPLASH.route) { inclusive = true }
                    }
                } else {
                    navController.navigate(Routes.LOGIN.route) {
                        popUpTo(Routes.SPLASH.route) { inclusive = true }
                    }
                }
            }
        }

        composable(Routes.LOGIN.route) {
            LoginScreen(vm = authViewModel, navController = navController)
        }

        composable(Routes.REGISTER.route) {
            RegisterScreen(vm = authViewModel, navController = navController)
        }

        composable(Routes.MAIN.route) {
            val taskVM: TaskViewModel = hiltViewModel()

            MainScaffold(
                taskVM = taskVM,
                authVM = authViewModel,
                appNavController = navController
            )
        }
    }
}
