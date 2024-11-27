package com.followapp.mytasks.common.dataAccess.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.followapp.mytasks.common.entities.Task

@Dao
interface TaskDAO {
    @Query("SELECT * FROM TasksTable")
    fun getAllTasks(): LiveData<List<Task>>

//    @Query("SELECT * FROM TasksTable WHERE id == :id")
//    suspend fun getTaskById(id: Double): Task

    @Insert
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}
