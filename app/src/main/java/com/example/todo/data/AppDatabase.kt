package com.example.todo.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [User::class, Task::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao
}
