package com.example.todoapp.data.local.dao
import androidx.room.*
import com.example.todoapp.data.local.entity.TaskEntity
import com.example.todoapp.utils.TaskStatus
import kotlinx.coroutines.flow.Flow
// this is the dao layer in which create , update , and delete queries are defined
@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)
    @Update
    suspend fun updateTask(task: TaskEntity)
    @Delete
    suspend fun deleteTask(task: TaskEntity)
    //check status of active tasks
    @Query("""
        SELECT * FROM tasks 
        WHERE userId = :userId AND status = :status
        ORDER BY dueDateTime ASC
    """)
    fun getActiveTasks(
        userId: Int,
        status: TaskStatus = TaskStatus.ACTIVE
    ): Flow<List<TaskEntity>>
    @Query("""
        SELECT * FROM tasks 
        WHERE userId = :userId AND status = :status
        ORDER BY createdAt DESC
    """)
    fun getCompletedTasks(
        userId: Int,
        status: TaskStatus = TaskStatus.COMPLETED
    ): Flow<List<TaskEntity>>

    @Query("""
        UPDATE tasks 
        SET status = :status 
        WHERE id = :taskId
    """)
    suspend fun updateTaskStatus(
        taskId: Int,
        status: TaskStatus
    )
}
