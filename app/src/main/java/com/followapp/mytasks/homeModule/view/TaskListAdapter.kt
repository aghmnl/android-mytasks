package com.followapp.mytasks.homeModule.view

import android.graphics.Paint
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
        (holder as ViewHolder).bindItem(task, position)
    }

    fun setOnClickListener(listener: OnTaskClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTaskBinding.bind(view)

        fun bindItem(task: Task, position: Int) {
            setupUIComponents(task)
            setupListeners(task, position)
        }

        private fun setupUIComponents(task: Task) {
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

        private fun setupListeners(task: Task, position: Int) {
            with(binding) {
                root.setOnClickListener { listener.onTaskClick(task) }
                cbTaskDone.setOnClickListener {
                    listener.onTaskCheckBoxClick(task, position, this@TaskListAdapter)
                }
            }
        }

    }
}