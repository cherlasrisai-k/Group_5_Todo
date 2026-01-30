package com.example.todo.states

import com.example.todo.data.Task

data class TasksUiState(
    var showDialog: Boolean = false,
    var editingTask: Task? = null
)
