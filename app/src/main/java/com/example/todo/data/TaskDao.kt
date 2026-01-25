package com.example.todo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    suspend fun insert(task: Task)
    @Update
    suspend fun update(task: Task)
    @Delete
    suspend fun delete(task: Task)

    // Only TODAY active tasks, sorted
//    @Query("""
//        SELECT * FROM tasks
//        WHERE isCompleted = 0
//        AND date(dateTime/1000,'unixepoch','localtime') = date('now','localtime')
//        ORDER BY dateTime ASC
//    """)
//    fun todayTasks(): Flow<List<Task>>

    @Query("""
    SELECT * FROM tasks
    WHERE userMobile = :mobile
    AND isCompleted = 0
    AND date(dateTime/1000,'unixepoch','localtime') = date('now','localtime')
    ORDER BY dateTime ASC
""")
    fun todayTasks(mobile: String): Flow<List<Task>>

    @Query("SELECT * FROM tasks\n" +
            "        WHERE userMobile = :mobile\n" +
            "        AND isCompleted = 1\n" +
            "        ORDER BY dateTime DESC")
    fun completedTasks(mobile: String): Flow<List<Task>>

    // For reminder
    @Query("""
        SELECT COUNT(*) FROM tasks
        WHERE isCompleted = 0
        AND date(dateTime/1000,'unixepoch','localtime') = date('now','localtime')
    """)
    suspend fun countTodayTasks(): Int



//    @Query("DELETE FROM tasks WHERE isCompleted = 1")
//    suspend fun deleteHistory():Flow<List<Task>>



}
