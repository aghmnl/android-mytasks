package com.followapp.mytasks.homeModule.model

import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.homeModule.model.domain.HomeRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeRepository(private val db: HomeRoomDatabase) {
    suspend fun getAllTasks(): List<Task> {
        return db.getAllTasks()
    }

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