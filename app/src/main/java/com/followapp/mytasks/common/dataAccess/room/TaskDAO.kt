package com.followapp.mytasks.common.dataAccess.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.followapp.mytasks.common.entities.Task

@Dao
interface TaskDAO {
    @Query("SELECT * FROM TasksTable")
    suspend fun getAllTasks(): MutableList<Task>

//    @Query("SELECT * FROM TasksTable WHERE id == :id")
//    suspend fun getTaskById(id: Double): Task

    @Insert
    suspend fun addTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task): Int

    @Delete
    suspend fun deleteTask(task: Task): Int
}
