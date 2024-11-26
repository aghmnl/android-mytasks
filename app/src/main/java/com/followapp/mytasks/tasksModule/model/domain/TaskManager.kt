package com.followapp.mytasks.tasksModule.model.domain

import android.content.Context
import androidx.room.Room
import com.followapp.mytasks.common.dataAccess.room.AppDatabase
import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.homeModule.model.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object TaskManager {
    private lateinit var repository: TaskRepository
    var selectedTaskIndex: Int = -1
    var tasksList = mutableListOf<Task>()
    var onItemInserted: (() -> Unit)? = null
    var onItemChanged: (() -> Unit)? = null
    var onItemRemoved: (() -> Unit)? = null

    fun initialize(applicationContext: Context) {
        val database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "tasks-database").build()
        repository = TaskRepository(database.taskDAO())
        runBlocking {
            tasksList.addAll(repository.allTasks.value ?: emptyList())
            onItemInserted?.invoke()
        }
    }

    fun addTask(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insert(task)
            tasksList.add(task)
            onItemInserted?.invoke()
        }
    }

    fun updateTask(task: Task) {
        tasksList[selectedTaskIndex] = task
        CoroutineScope(Dispatchers.IO).launch {
            repository.update(task)
            onItemChanged?.invoke()
        }
    }

    fun deleteTask(task: Task) {
        tasksList.remove(task)
        CoroutineScope(Dispatchers.IO).launch {
            repository.delete(task)
            onItemRemoved?.invoke()
        }
    }
}