package com.example.todoapp

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.todoapp.data.local.database.AppDatabase
import com.example.todoapp.data.local.entity.TaskEntity
import com.example.todoapp.data.repository.TaskRepository
import com.example.todoapp.ui.theme.TodoAppTheme
import com.example.todoapp.utils.TaskStatus
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

//        val db = AppDatabase.getDatabase(this)
//        val repository = TaskRepository(db.taskDao())
//
//        lifecycleScope.launch {
//            val testTask = TaskEntity(
//                userId = 1,
//                topic = "Unit 1 Basics",
//                heading = "Learn Kotlin",
//                description = "Room + MVVM Test",
//                dueDateTime = System.currentTimeMillis(),
//                status = TaskStatus.ACTIVE
//            )
//            repository.addTask(testTask)
//            repository.markTaskCompleted(1)
//            repository.getActiveTasks(1).collect {
//                Log.d("TASK_TEST", "Active Tasks: $it")
//            }
//
//            repository.getCompletedTasks(1).collect {
//                Log.d("TASK_TEST", "Completed Tasks: $it")
//            }
//
//        }

        setContent {
            TodoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    Greeting(
                        name = "To-Do App",
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoAppTheme {
        Greeting("Android")
    }
}

