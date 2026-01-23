package com.example.todoapp.data.local.database
import androidx.room.TypeConverter
import com.example.todoapp.utils.TaskStatus
class Converters {
    @TypeConverter
    fun fromStatus(status: TaskStatus): String {
        return status.name
    }
    @TypeConverter
    fun toStatus(status: String): TaskStatus {
        return TaskStatus.valueOf(status)
    }
}
