package com.example.todo.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.todo.ui.HistoryScreen
import com.example.todo.ui.HomeScreen
import com.example.todo.ui.TasksScreen
import com.example.todo.viewmodel.TaskViewModel

@Composable
fun MainNavGraph(
    navController: NavHostController,
    taskVM: TaskViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.screen.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Home.screen.route) {
            HomeScreen(taskVM)
        }

        composable(
            BottomNavItem.Active.screen.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "todo://active_tasks"
                }
            )
        ) {
            TasksScreen(taskVM)
        }

        composable(BottomNavItem.History.screen.route) {
            HistoryScreen(taskVM)
        }
    }
}
