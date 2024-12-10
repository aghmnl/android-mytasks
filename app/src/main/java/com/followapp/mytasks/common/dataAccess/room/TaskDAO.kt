package com.followapp.mytasks.common.dataAccess.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.followapp.mytasks.common.entities.Task

@Dao
interface TaskDAO {
    @Query("SELECT * FROM TasksTable")
    fun getAllTasks(): MutableList<Task>

    @Query("SELECT * FROM TasksTable WHERE id == :id")
    fun getTaskById(id: Long): Task?

    @Insert
    fun addTask(task: Task): Long

    @Update
    fun updateTask(task: Task): Int

    @Query("DELETE FROM TasksTable WHERE id = :id")
    fun deleteTaskById(id: Long): Int

//    @Delete
//    suspend fun deleteTask(task: Task): Int

//    @Query("DELETE FROM TasksTable WHERE id = :taskId")
//    suspend fun deleteTaskById(taskId: Long)
}
