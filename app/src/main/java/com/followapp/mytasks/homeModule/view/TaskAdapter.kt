package com.followapp.mytasks.homeModule.view

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.followapp.mytasks.R
import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.tasksModule.model.domain.TaskManager
import com.followapp.mytasks.detailModule.view.TaskDetail

class TasksAdapter : ListAdapter<Task, TasksAdapter.TaskViewHolder>(TaskDiffCallback()) {

    class TaskViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val taskTitle: TextView = view.findViewById(R.id.textViewTaskTitle)

        fun bind(task: Task) {
            taskTitle.text = task.title
            view.setOnClickListener {
                TaskManager.selectedTaskIndex = adapterPosition
                val intent = Intent(view.context, TaskDetail::class.java)
                if (!(view.context as Activity).isFinishing) {
                    view.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.task_row, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }
}

