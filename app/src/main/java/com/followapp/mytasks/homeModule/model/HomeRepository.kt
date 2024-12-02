package com.followapp.mytasks.homeModule.model

import androidx.lifecycle.LiveData
import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.homeModule.model.domain.HomeRoomDatabase

class HomeRepository(private val db: HomeRoomDatabase) {
    val allTasks: List<Task> = db.getAllTasks()

    suspend fun addTask(task: Task): Long {
        return db.addTask(task)
    }

    suspend fun updateTask(task: Task): Int {
        return db.updateTask(task)
    }

    suspend fun deleteTask(task: Task): Int {
        return db.deleteTask(task)
    }
}