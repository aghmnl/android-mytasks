package com.followapp.mytasks.homeModule.view

import androidx.recyclerview.widget.DiffUtil
import com.followapp.mytasks.common.entities.Task

class TaskDiff : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}