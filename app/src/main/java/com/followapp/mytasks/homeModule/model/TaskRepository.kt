package com.followapp.mytasks.homeModule.model

import androidx.lifecycle.LiveData
import com.followapp.mytasks.common.dataAccess.room.TaskDAO
import com.followapp.mytasks.common.entities.Task

class TaskRepository(private val taskDAO: TaskDAO) {
    val allTasks: LiveData<List<Task>> = taskDAO.getAllTasks()

    suspend fun insert(task: Task) {
        taskDAO.insert(task)
    }

    suspend fun update(task: Task) {
        taskDAO.update(task)
    }

    suspend fun delete(task: Task) {
        taskDAO.delete(task)
    }
}