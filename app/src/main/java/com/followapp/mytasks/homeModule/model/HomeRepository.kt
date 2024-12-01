package com.followapp.mytasks.homeModule.model

import androidx.lifecycle.LiveData
import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.homeModule.model.domain.HomeRoomDatabase

class HomeRepository(private val db: HomeRoomDatabase) {
    val allTasks: LiveData<List<Task>> = db.getAllTasks()

    suspend fun insert(task: Task) {
        db.insert(task)
    }

    suspend fun update(task: Task) {
        db.update(task)
    }

    suspend fun delete(task: Task) {
        db.delete(task)
    }
}