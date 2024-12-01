package com.followapp.mytasks.homeModule.model.domain

import com.followapp.mytasks.TaskApplication
import com.followapp.mytasks.common.dataAccess.room.TaskDAO
import com.followapp.mytasks.common.entities.Task

class HomeRoomDatabase {
    private val dao: TaskDAO by lazy { TaskApplication.database.taskDAO() }

    fun getAllTasks() = dao.getAllTasks()

    suspend fun insert(task: Task) = dao.insert(task)

    suspend fun update(task: Task) = dao.update(task)

    suspend fun delete(task: Task) = dao.delete(task)
}