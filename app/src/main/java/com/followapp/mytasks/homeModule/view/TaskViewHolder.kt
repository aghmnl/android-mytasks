package com.followapp.mytasks.homeModule.view

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.followapp.mytasks.R
import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.detailModule.view.TaskDetail
import com.followapp.mytasks.tasksModule.model.domain.TaskManager

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