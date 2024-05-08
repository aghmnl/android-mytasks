package com.followapp.mytasks

object TaskManager {
    var selectedTaskIndex: Int = -1
    var tasksList = mutableListOf<Task>()
    var onItemInserted: (() -> Unit)? = null
    var onItemChanged: (() -> Unit)? = null
    var onItemRemoved: (() -> Unit)? = null

//    fun getSelectedTask(): Task? {
//        return if (selectedTaskIndex != -1) tasksList[selectedTaskIndex] else null
//    }

    fun addTask(task: Task) {
        tasksList.add(task)
        onItemInserted?.invoke()
    }

    fun updateTask(task: Task) {
        tasksList[selectedTaskIndex] = task
        onItemChanged?.invoke()
    }

    fun deleteTask() {
        tasksList.removeAt(selectedTaskIndex)
        onItemRemoved?.invoke()
    }
}