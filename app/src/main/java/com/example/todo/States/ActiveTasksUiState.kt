package com.example.todo.States

import com.example.todo.data.Task

data class ActiveTasksUiState(
    var showDialog: Boolean = false,
    var editingTask: Task? = null
)
