package com.followapp.mytasks.homeModule.model

import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.homeModule.model.domain.HomeRoomDatabase

class HomeRepository(private val db: HomeRoomDatabase) {
    suspend fun getAllTasks(): List<Task> {
        return db.getAllTasks()
    }
}