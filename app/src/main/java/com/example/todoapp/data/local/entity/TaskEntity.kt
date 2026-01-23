package com.example.todoapp.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapp.utils.TaskStatus
// for defining what all will be present in database schema
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val topic: String,
    val heading: String,
    val description: String? = null,
    val dueDateTime: Long,
    val status: TaskStatus,
    val createdAt: Long = System.currentTimeMillis()
)
