package com.example.todoapp.data.repository
import com.example.todoapp.data.local.dao.TaskDao
import com.example.todoapp.data.local.entity.TaskEntity
import com.example.todoapp.utils.TaskStatus
import kotlinx.coroutines.flow.Flow
class TaskRepository(
    private val taskDao: TaskDao
) {
    fun getActiveTasks(userId: Int): Flow<List<TaskEntity>> =
        taskDao.getActiveTasks(userId)
    fun getCompletedTasks(userId: Int): Flow<List<TaskEntity>> =
        taskDao.getCompletedTasks(userId)
    suspend fun addTask(task: TaskEntity) =
        taskDao.insertTask(task)

    suspend fun updateTask(task: TaskEntity) =
        taskDao.updateTask(task)
    suspend fun deleteTask(task: TaskEntity) =
        taskDao.deleteTask(task)
    suspend fun markTaskCompleted(taskId: Int) =
        taskDao.updateTaskStatus(taskId, TaskStatus.COMPLETED)
}
