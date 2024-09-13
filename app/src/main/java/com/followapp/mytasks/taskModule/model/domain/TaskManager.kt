package com.followapp.mytasks.taskModule.model.domain

import android.content.Context
import androidx.room.Room
import com.followapp.mytasks.common.dataAccess.room.AppDatabase
import com.followapp.mytasks.common.dataAccess.room.TaskDAO
import com.followapp.mytasks.common.entities.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object TaskManager {
    private lateinit var database: AppDatabase
    private val taskDAO: TaskDAO
        get() = database.taskDAO() // This is to avoid database being accessed (via taskDAO) before it’s been initialized

    var selectedTaskIndex: Int = -1
    var tasksList = mutableListOf<Task>()
    var onItemInserted: (() -> Unit)? = null
    var onItemChanged: (() -> Unit)? = null
    var onItemRemoved: (() -> Unit)? = null

    fun initialize(applicationContext: Context) {
        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "tasks-database").build()

        //  Blocking the main thread can lead to UI freezes and is generally not recommended for long-running operations.
        runBlocking {
            tasksList.addAll(taskDAO.getAll())
            onItemInserted?.invoke()
            if (tasksList.isEmpty()) addSomeTasks()
        }
    }

    private fun addSomeTasks() {
        addTask(Task("Shopping", false))
        addTask(Task("Gym", false))
        addTask(Task("Work", true))
    }

    fun addTask(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            val id = taskDAO.insert(task)
            task.id = id.toInt()
        }
        tasksList.add(task)
        onItemInserted?.invoke()
    }

    fun updateTask(task: Task) {
        tasksList[selectedTaskIndex] = task
        CoroutineScope(Dispatchers.IO).launch {
            taskDAO.update(task)
        }
        onItemChanged?.invoke()
    }

    fun deleteTask(task: Task) {
        tasksList.remove(task)
        CoroutineScope(Dispatchers.IO).launch { taskDAO.delete(task) }
        onItemRemoved?.invoke()
    }
}