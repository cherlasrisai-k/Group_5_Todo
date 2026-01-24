package com.example.todo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.todo.data.Task
import com.example.todo.viewmodel.TaskViewModel
import java.text.DateFormat
import java.util.Date
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit

@Composable
fun ActiveTasksScreen(vm: TaskViewModel) {

    val tasks by vm.todayTasks.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var editTask by remember { mutableStateOf<Task?>(null) }

    LazyColumn {

        items(tasks) { task ->

            Card(Modifier.padding(8.dp)) {

                Row(
                    Modifier.fillMaxWidth().padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column {
                        Text(task.topic, fontWeight = FontWeight.Bold)
                        Text(task.heading)
                        Text(DateFormat.getDateTimeInstance().format(Date(task.dateTime)))
                    }

                    Column {
                        IconButton(onClick = { vm.completeTask(task) }) {
                            Icon(Icons.Default.Check, null)
                        }
                        IconButton(onClick = { vm.deleteTask(task) }) {
                            Icon(Icons.Default.Delete, null)
                        }
                        IconButton(onClick = {
                            editTask = task
                            showDialog = true
                        }) {
                            Icon(Icons.Default.Edit, null)
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddEditDialog(
            task = editTask,
            onDismiss = { showDialog = false },
            onSave = { t, h, d ->
                vm.updateTask(editTask!!.copy(topic = t, heading = h, dateTime = d))
                showDialog = false
            }
        )
    }
}
