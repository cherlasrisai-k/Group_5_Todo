package com.example.todo

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
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
import com.example.todo.data.Screens
import com.example.todo.ui.LoginScreen
import com.example.todo.ui.RegisterScreen
import com.example.todo.ui.theme.TodoTheme
import com.example.todo.viewmodel.AuthViewModel
import com.example.todo.viewmodel.TaskViewModel
import com.example.todo.worker.ReminderWorker
import java.util.concurrent.TimeUnit
import com.example.todo.ui.theme.TodoTheme

class MainActivity : ComponentActivity() {

//    private val authVM: AuthViewModel by viewModels()
//    private val taskVM: TaskViewModel by viewModels()

    private lateinit var authVM: AuthViewModel
    private lateinit var taskVM: TaskViewModel





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.get(this)

        val authVM = AuthViewModel(db.userDao())
        val taskVM = TaskViewModel(db.taskDao())
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101
            )
        }

        // 🔔 First reminder immediately
        WorkManager.getInstance(this).enqueue(
            OneTimeWorkRequestBuilder<ReminderWorker>().build()
        )

        //  Hourly reminder
        val hourly =
            PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.HOURS).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "hourly_reminder",
            ExistingPeriodicWorkPolicy.UPDATE,
            hourly
        )

        setContent {
            TodoTheme{

                val nav = rememberNavController()

                NavHost(nav, startDestination = Screens.LoginScreen.route) {

                    composable(Screens.LoginScreen.route) {
                        LoginScreen(
                            authVM,
                            onLogin = {
                                authVM.user?.let { taskVM.setUser(it.mobile) }
                                nav.navigate(Screens.HomeScreen.route)
                            },
                            onRegister = { nav.navigate(Screens.RegisterScreen.route) }
                        )


                    }

                    composable(Screens.RegisterScreen.route) {
                        RegisterScreen(authVM) {
                            authVM.user?.let {
                                taskVM.setUser(it.mobile)
                            }

                            nav.navigate(Screens.HomeScreen.route)
                        }
                    }

                    composable(Screens.HomeScreen.route) {
                        MainScaffold(taskVM, authVM) {
                            nav.navigate(Screens.LoginScreen.route) {
                                popUpTo(Screens.LoginScreen.route) { inclusive = true }
                            }
                        }

                    }
                }
            }
        }
    }

}




