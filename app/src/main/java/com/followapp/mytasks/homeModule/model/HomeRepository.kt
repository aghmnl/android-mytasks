package com.followapp.mytasks.homeModule.model

import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.homeModule.model.domain.HomeRoomDatabase

class HomeRepository(private val db: HomeRoomDatabase) {
    val allTasks: List<Task> = db.getAllTasks()

    fun addTask(task: Task): Long {
        return db.addTask(task)
    }

    fun updateTask(task: Task): Int {
        return db.updateTask(task)
    }

    fun deleteTask(task: Task): Int {
        return db.deleteTask(task)
    }
}