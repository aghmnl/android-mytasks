package com.followapp.mytasks.homeModule.view

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.followapp.mytasks.R
import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.common.utils.OnTaskClickListener
import com.followapp.mytasks.databinding.ItemTaskBinding

class TaskListAdapter : ListAdapter<Task, RecyclerView.ViewHolder>(TaskDiff()) {

    lateinit var listener: OnTaskClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val task = getItem(position)
        Log.i("IMPORTANTE", "5. TaskListAdapter: onBindViewHolder executed. Task: ${task.title} (isDone: ${task.isDone} - version: ${task.version})" )
        (holder as ViewHolder).bindItem(task)
    }


    fun setOnClickListener(listener: OnTaskClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTaskBinding.bind(view)

        fun bindItem(task: Task) {
            setupUIComponents(task)
            setupListeners(task)
        }

        private fun setupUIComponents(task: Task) {
            Log.i("IMPORTANTE", "6. TaskListAdapter: setupUIComponents executed. Task: ${task.title} (isDone: ${task.isDone} - version: ${task.version})" )
            with(binding) {
                tvTaskTitle.text = task.title
                cbTaskDone.isChecked = task.isDone
                tvTaskTitle.paintFlags = if (task.isDone) {
                    binding.tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    binding.tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
        }

        private fun setupListeners(task: Task) {
            with(binding) {
                root.setOnClickListener { listener.onTaskClick(task) }
                cbTaskDone.setOnClickListener {
                    Log.i("IMPORTANTE", "1. TaskListAdapter: setupListeners (checkBox clicked). Task: ${task.title} (isDone: ${task.isDone} - version: ${task.version})")
                    listener.onTaskCheckBoxClick(task)
                }
            }
        }

    }
}