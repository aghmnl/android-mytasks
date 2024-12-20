package com.followapp.mytasks.homeModule.model

import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.homeModule.model.domain.HomeRoomDatabase

class HomeRepository(private val db: HomeRoomDatabase) {

    fun getAllTasks() = db.getAllTasks()

    fun updateTask(task: Task) = db.updateTask(task)
}