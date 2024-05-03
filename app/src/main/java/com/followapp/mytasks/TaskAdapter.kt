package com.followapp.mytasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TasksAdapter(private val tasks: List<Task>) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    // This class is required as result of the onCreateViewHolder
    class TaskViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    // This function is required by the RecyclerView.Adapter
    override fun getItemCount(): Int {
        return tasks.size
    }

    // This function is required by the RecyclerView.Adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.task_row, parent, false)
        return TaskViewHolder(view)
    }

    // This function is required by the RecyclerView.Adapter
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val taskTitle: TextView = holder.view.findViewById(R.id.textViewTaskTitle)
        println(tasks)
        val task = tasks[position]
        taskTitle.text = task.title
//        holder.itemView.setOnClickListener {
//            // Handle click event to show task details
//        }
    }
}