package com.example.todo.ui

data class HomeUiState(
    val topic: String = "",
    val heading: String = "",
    val dateTime: Long = System.currentTimeMillis(),
    val topicError: String = "",
    val headingError: String = "",
    val timeError: String = ""
)
