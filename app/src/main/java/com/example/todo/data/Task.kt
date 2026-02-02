package com.example.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userMobile: String,
    val topic: String,
    val heading: String,
    val dateTime: Long,
    val isCompleted: Boolean = false,
    val completionTime: Long = 0
)
