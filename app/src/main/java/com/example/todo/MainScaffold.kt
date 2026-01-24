package com.example.todo

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todo.ui.ActiveTasksScreen
import com.example.todo.ui.BottomBar
import com.example.todo.ui.HistoryScreen
import com.example.todo.ui.HomeScreen
import com.example.todo.viewmodel.AuthViewModel
import com.example.todo.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(taskVM: TaskViewModel,
                 authVM: AuthViewModel,
                 onLogout: () -> Unit) {

    val nav = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("To-Do Reminder") },
                actions = {
                    TextButton(onClick = {
                        authVM.logout()
                        onLogout()
                    }) {
                        Text("Logout")
                    }
                }
            )
        },

        bottomBar = { BottomBar(nav) }
    ) { pad ->

        NavHost(
            navController = nav,
            startDestination = "home",
            modifier = Modifier.padding(pad)
        ) {
            composable("home") {
                HomeScreen(taskVM)
            }

            composable("active") { ActiveTasksScreen(taskVM) }
            composable("history") { HistoryScreen(taskVM) }
        }
    }
}
