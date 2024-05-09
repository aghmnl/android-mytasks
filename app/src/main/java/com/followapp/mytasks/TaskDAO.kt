package com.followapp.mytasks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDAO {
    @Query("SELECT * FROM task")
    suspend fun getAll(): List<Task>

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun delete(id: Int)

//    @Delete
//    suspend fun delete(id: Int)
}