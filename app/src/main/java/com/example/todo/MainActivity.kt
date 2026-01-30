package com.example.todo

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.todo.data.AppDatabase
import com.example.todo.navigation.AppNavGraph
import com.example.todo.navigation.Routes
import com.example.todo.session.SessionManager
import com.example.todo.ui.LoginScreen
import com.example.todo.ui.RegisterScreen
import com.example.todo.ui.theme.TodoTheme
import com.example.todo.viewmodel.AuthViewModel
import com.example.todo.viewmodel.TaskViewModel
import com.example.todo.worker.ReminderWorker
import java.util.concurrent.TimeUnit
import com.example.todo.ui.theme.TodoTheme

class MainActivity : ComponentActivity() {

    private lateinit var authVM: AuthViewModel
    private lateinit var taskVM: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.get(applicationContext)
        authVM = AuthViewModel(db.userDao(),applicationContext)
        taskVM = TaskViewModel(db.taskDao(), applicationContext)

        setContent {
            TodoTheme {
                var startRoute by rememberSaveable { mutableStateOf<String?>(null) }

                LaunchedEffect(Unit) {
                    try {
                        val savedUser = SessionManager.getUser(applicationContext)

                        startRoute = if (savedUser != null) {
                            authVM.autoLogin(savedUser)
                            taskVM.setUser(savedUser)
                            Routes.MAIN.route
                        } else {
                            Routes.LOGIN.route
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        startRoute = Routes.LOGIN.route
                    }
                }


                    val navController = rememberNavController()

                    AppNavGraph(
                        navController = navController,
                        authVM = authVM,
                        taskVM = taskVM,
                        startDestination = Routes.LOGIN.route,
                        activity = this
                    )
                }
            }
        }

            }






