package com.example.todo.ui

import androidx.activity.ComponentActivity
import androidx.compose.animation.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo.data.Task
import com.example.todo.viewmodel.TaskViewModel
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*
import java.util.Date

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun TasksScreen(vm: TaskViewModel, activity: ComponentActivity) {

    val tasks: List<Task> by vm.todayTasks.collectAsState(initial = emptyList())
    val uiState by vm.activeUiState.collectAsState()


    val windowSizeClass = calculateWindowSizeClass(activity)
    val columns = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 1
        WindowWidthSizeClass.Medium -> 2
        WindowWidthSizeClass.Expanded -> 3
        else -> 1
    }

    val listState = rememberLazyListState()
    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    val showButton by remember(columns) {
        derivedStateOf {
            if (columns == 1) listState.firstVisibleItemIndex > 0
            else gridState.firstVisibleItemIndex > 0
        }
    }

    LaunchedEffect(Unit) {
        vm.events.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (columns == 1) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(tasks) { task ->
                    TaskCard(task = task, vm = vm)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                state = gridState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(tasks) { task ->
                    TaskCard(task = task, vm = vm)
                }
            }
        }

        // Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
        ) { snackbarData ->
            Snackbar(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = snackbarData.visuals.message,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }


        AnimatedVisibility(
            visible = showButton,
            enter = fadeIn() + expandIn(),
            exit = fadeOut() + shrinkOut(),
            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp)
        ) {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape= CircleShape,
                modifier = Modifier.size(56.dp),
                onClick = {
                    scope.launch {
                        if (columns == 1) listState.animateScrollToItem(0)
                        else gridState.animateScrollToItem(0)
                    }
                }
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Top")
            }
        }
    }

    if (uiState.showDialog) {
        AddEditDialog(vm)
    }
}

@Composable
fun TaskCard(task: Task, vm: TaskViewModel) {
    Card(
        Modifier.padding(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text(task.topic, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium)
                Text(task.heading)
                val isTaskPending=task.dateTime<System.currentTimeMillis()

                val timeColor = if (isTaskPending)
                    Color.Red
                else
                    MaterialTheme.colorScheme.onSurface

                Text(
                    text = DateFormat.getDateTimeInstance().format(Date(task.dateTime)),
                    color = timeColor,
                    style = MaterialTheme.typography.bodySmall
                )
                if(isTaskPending){
                    Text(text = "(Pending Task)",color=timeColor)
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Action Buttons
                TaskActionButton(Icons.Default.Check, Color.Green) { vm.completeTask(task) }
                TaskActionButton(Icons.Default.Delete, MaterialTheme.colorScheme.error) { vm.deleteTask(task) }
                TaskActionButton(Icons.Default.Edit, MaterialTheme.colorScheme.secondary) { vm.startEdit(task) }
            }
        }
    }
}

@Composable
fun TaskActionButton(icon: ImageVector, tint: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
        }
    }
}