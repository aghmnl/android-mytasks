package com.followapp.mytasks.common.utils

import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.homeModule.view.TaskListAdapter

interface OnTaskClickListener {
    fun onTaskClick(task: Task)
    fun onTaskCheckBoxClick(task: Task, position: Int, adapter: TaskListAdapter)
}