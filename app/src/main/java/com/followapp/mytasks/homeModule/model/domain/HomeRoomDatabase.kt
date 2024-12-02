package com.followapp.mytasks.homeModule.model.domain

import com.followapp.mytasks.TaskApplication
import com.followapp.mytasks.common.dataAccess.room.TaskDAO
import com.followapp.mytasks.common.entities.Task

class HomeRoomDatabase {
    private val dao: TaskDAO by lazy { TaskApplication.database.taskDAO() }

    fun getAllTasks() = dao.getAllTasks()

    fun addTask(task: Task) = dao.addTask(task)

    fun updateTask(task: Task) = dao.updateTask(task)

    fun deleteTask(task: Task) = dao.deleteTask(task)
}