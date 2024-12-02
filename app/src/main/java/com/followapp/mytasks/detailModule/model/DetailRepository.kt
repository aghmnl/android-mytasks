package com.followapp.mytasks.detailModule.model

import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.detailModule.model.domain.DetailRoomDatabase

class DetailRepository(private val db: DetailRoomDatabase) {

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