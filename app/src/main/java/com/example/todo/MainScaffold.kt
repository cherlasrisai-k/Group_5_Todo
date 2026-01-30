package com.example.todo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.todo.navigation.MainNavGraph
import com.example.todo.navigation.Routes
import com.example.todo.ui.BottomBar
import com.example.todo.viewmodel.AuthViewModel
import com.example.todo.viewmodel.TaskViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    taskVM: TaskViewModel, authVM: AuthViewModel, appNavController: NavController
) {
    val bottomNavController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val currentScreen = bottomNavController.currentBackStackEntryAsState().value?.destination?.route



    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                scrolledContainerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            ),

                title = {
                    if (currentScreen == Routes.HOME.route) {
                        Text(
                            text = "Welcome ${authVM.user?.name}",
                            modifier = Modifier.padding(vertical = 8.dp),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        IconButton(onClick = {
                            bottomNavController.popBackStack()
                        }, modifier = Modifier.size(30.dp)) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "NavigateBack")
                        }
                    }
                }, actions = {
                    IconButton(onClick = {
                        authVM.logout()

                        appNavController.navigate(Routes.LOGIN.route) {
                            popUpTo(0)
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout")
                    }
                }, scrollBehavior = scrollBehavior
            )
        },

        bottomBar = {
            BottomBar(bottomNavController)
        }

    ) { padding ->
        MainNavGraph(
            navController = bottomNavController,
            taskVM = taskVM,
            modifier = Modifier.padding(padding)
        )
    }
}

