package com.example.todo.ui


import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.todo.viewmodel.TaskViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
fun HistoryScreen(vm: TaskViewModel) {

    val tasks by vm.completedTasks.collectAsState()

    LazyColumn {
        items(tasks) {
            ListItem(
                headlineContent = { Text(it.topic) },
                supportingContent = { Text(it.heading) }
            )
        }
    }
}
