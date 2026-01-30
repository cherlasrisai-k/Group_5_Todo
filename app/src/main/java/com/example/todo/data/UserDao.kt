package com.example.todo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun register(user: User)

    @Query("SELECT * FROM users WHERE mobile = :mobile LIMIT 1")
    suspend fun login(mobile: String): User?

}
