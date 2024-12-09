package com.followapp.mytasks.detailModule.model

import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.detailModule.model.domain.DetailRoomDatabase

class DetailRepository(private val db: DetailRoomDatabase) {

    suspend fun getTaskById(id: Long) = db.getTaskById(id)

    suspend fun addTask(task: Task) = db.addTask(task)

    suspend fun updateTask(task: Task) = db.updateTask(task)

    suspend fun deleteTask(id: Long) = db.deleteTask(id)

}