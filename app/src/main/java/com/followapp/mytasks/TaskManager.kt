package com.followapp.mytasks

import android.content.Context
import androidx.room.Room
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
        addTask(Task("Hacer las compras", false))
        addTask(Task("Ir al gym", false))
        addTask(Task("Programar", true))
    }

    fun addTask(task: Task) {
        tasksList.add(task)
        CoroutineScope(Dispatchers.IO).launch {
            taskDAO.insert(task)
        }
        onItemInserted?.invoke()
    }

    fun updateTask(task: Task) {
        tasksList[selectedTaskIndex] = task
        CoroutineScope(Dispatchers.IO).launch {
            taskDAO.update(task)
        }
        onItemChanged?.invoke()
    }

    fun deleteTask(id: Int) {
        tasksList.removeAt(selectedTaskIndex)
        CoroutineScope(Dispatchers.IO).launch {
            taskDAO.delete(id)
        }
        onItemRemoved?.invoke()
    }
}