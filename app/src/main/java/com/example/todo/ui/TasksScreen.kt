package com.example.todo.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.example.todo.ui.theme.SuccessGreen
import kotlinx.coroutines.launch


@Composable
fun ActiveTasksScreen(vm: TaskViewModel) {

    val tasks: List<Task> by vm.todayTasks.collectAsState(initial = emptyList())

    val uiState by vm.activeUiState.collectAsState()

    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val showButton by remember {
        derivedStateOf { scrollState.firstVisibleItemIndex > 0 }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    // Collect one-time events (SharedFlow)
    LaunchedEffect(Unit) {
        vm.events.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(state = scrollState) {
            items(tasks) { task:Task ->

                Card(
                    Modifier.padding(5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column(Modifier.weight(1f)) {
                            Text(task.topic, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Text(task.heading)
                            Text(DateFormat.getDateTimeInstance().format(Date(task.dateTime)))
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            IconButton(onClick = { vm.completeTask(task) }) {
                                Icon(Icons.Default.Check, contentDescription = "Complete",tint = SuccessGreen)
                            }

                            IconButton(onClick = { vm.deleteTask(task) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete",tint = MaterialTheme.colorScheme.error)
                            }

                            IconButton(onClick = { vm.startEdit(task) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit",tint = MaterialTheme.colorScheme.secondary)
                            }
                        }
                    }
                }
            }
        }

        // Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )

        // Scroll to top FAB
        AnimatedVisibility(
            visible = showButton,
            enter = fadeIn() + expandIn(),
            exit = fadeOut() + shrinkOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            FloatingActionButton(
                onClick = { scope.launch { scrollState.animateScrollToItem(0) } }
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Top")
            }
        }
    }

    // Dialog opened purely from ViewModel state
    if (uiState.showDialog) {
        AddEditDialog(vm)
    }
}

