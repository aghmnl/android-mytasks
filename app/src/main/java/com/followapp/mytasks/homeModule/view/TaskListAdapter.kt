package com.followapp.mytasks.homeModule.view


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.followapp.mytasks.R
import com.followapp.mytasks.common.entities.Task

class TaskListAdapter : ListAdapter<Task, TaskViewHolder>(TaskDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.task_row, parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

