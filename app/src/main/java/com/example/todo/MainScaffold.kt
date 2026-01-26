package com.example.todo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.todo.ui.ActiveTasksScreen
import com.example.todo.ui.BottomBar
import com.example.todo.ui.BottomNavItem
import com.example.todo.ui.HistoryScreen
import com.example.todo.ui.HomeScreen
import com.example.todo.viewmodel.AuthViewModel
import com.example.todo.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    taskVM: TaskViewModel,
    authVM: AuthViewModel,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Active,
        BottomNavItem.History
    )



    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior();




    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route


    val screenTitle = items
        .firstOrNull { it.route == currentRoute }
        ?.title ?: ""

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            TopAppBar(
                title={
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center) {
                        Box(
                            modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person, contentDescription = "Person Logo",
                                tint=MaterialTheme.colorScheme.onSecondary
                            )
                        }
                        Text(text="Welcome $")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        authVM.logout()
                        onLogout()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint= MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                scrollBehavior = scrollBehavior,

            )
        },

        bottomBar = {
            AnimatedVisibility(
                visible = scrollBehavior.state.collapsedFraction < 0.5f,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                BottomBar(navController)
            }
        }

    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    taskVM,
                    onLogout = {
                        authVM.logout()
                        onLogout()
                    }
                )
            }

            composable(BottomNavItem.Active.route) {
                ActiveTasksScreen(taskVM)
            }

            composable(BottomNavItem.History.route) {
                HistoryScreen(taskVM)
            }
        }
    }
}
