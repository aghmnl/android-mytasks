package com.followapp.mytasks.tasksModule.model.domain

import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.homeModule.model.HomeRepository

import kotlinx.coroutines.runBlocking

object TaskManager {


    private lateinit var repository: HomeRepository
    var selectedTaskIndex: Int = -1
    var tasksList = mutableListOf<Task>()
    var onItemInserted: (() -> Unit)? = null
    var onItemChanged: (() -> Unit)? = null
    var onItemRemoved: (() -> Unit)? = null

    fun initialize() {

        runBlocking {
            tasksList.addAll(repository.allTasks.value ?: emptyList())
            onItemInserted?.invoke()
        }
    }


}