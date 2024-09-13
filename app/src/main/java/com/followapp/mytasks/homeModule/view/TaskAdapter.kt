package com.followapp.mytasks.homeModule.view

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.followapp.mytasks.R
import com.followapp.mytasks.taskModule.model.domain.TaskManager
import com.followapp.mytasks.detailModule.view.TaskDetail

class TasksAdapter : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    // This class is required as result of the onCreateViewHolder
    class TaskViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    // This function is required by the RecyclerView.Adapter
    override fun getItemCount(): Int {
        return TaskManager.tasksList.size
    }

    // This function is required by the RecyclerView.Adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.task_row, parent, false)
        return TaskViewHolder(view)
    }

    // This function is required by the RecyclerView.Adapter
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        // This line was previously inse the TaskViewHolder class as val
        val taskTitle: TextView = holder.view.findViewById(R.id.textViewTaskTitle)

        val task = TaskManager.tasksList[position]

        taskTitle.text = task.title
        holder.itemView.setOnClickListener {
            // Set the clicked task to SelectedTask.task
            TaskManager.selectedTaskIndex = position
            // Open TaskDetail activity with the details of the clicked task
            val intent = Intent(holder.itemView.context, TaskDetail::class.java)
            if (!(holder.itemView.context as Activity).isFinishing) {
                holder.itemView.context.startActivity(intent)
            }
        }
    }
}