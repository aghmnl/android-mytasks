package com.followapp.mytasks.detailModule.model

import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.detailModule.model.domain.DetailRoomDatabase

class DetailRepository(private val db: DetailRoomDatabase) {

    fun getTaskById(id: Long) = db.getTaskById(id)

    fun addTask(task: Task) = db.addTask(task)

    fun updateTask(task: Task) = db.updateTask(task)

    fun deleteTask(id: Long) = db.deleteTask(id)

}