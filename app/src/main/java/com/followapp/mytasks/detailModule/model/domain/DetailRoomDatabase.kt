package com.followapp.mytasks.detailModule.model.domain

import com.followapp.mytasks.TaskApplication
import com.followapp.mytasks.common.dataAccess.room.TaskDAO
import com.followapp.mytasks.common.entities.Task

class DetailRoomDatabase {
    private val dao: TaskDAO by lazy { TaskApplication.database.taskDAO() }

    fun getTaskById(id: Long) = dao.getTaskById(id)

    fun addTask(task: Task) = dao.addTask(task)

    fun updateTask(task: Task) = dao.updateTask(task)

    fun deleteTask(id: Long) = dao.deleteTaskById(id)
}