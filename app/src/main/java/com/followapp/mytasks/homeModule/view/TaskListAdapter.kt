package com.followapp.mytasks.homeModule.view


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
        (holder as ViewHolder).run {
            setListener(task)
            with(binding) { tvTaskTitle.text = task.title }
        }
    }

    fun setOnClickListener(listener: OnTaskClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTaskBinding.bind(view)

        fun setListener(task: Task) {
            binding.root.setOnClickListener {
                listener.onClick(task)
                true
            }
        }
    }
}

private fun TaskListAdapter.ViewHolder.onClick(task: Task) {}

