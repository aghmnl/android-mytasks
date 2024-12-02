package com.followapp.mytasks.homeModule.model.domain

import com.followapp.mytasks.TaskApplication
import com.followapp.mytasks.common.dataAccess.room.TaskDAO

class HomeRoomDatabase {
    private val dao: TaskDAO by lazy { TaskApplication.database.taskDAO() }

    suspend fun getAllTasks() = dao.getAllTasks()

}