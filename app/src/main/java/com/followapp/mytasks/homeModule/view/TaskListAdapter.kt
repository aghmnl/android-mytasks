package com.followapp.mytasks.homeModule.view

import TaskItem
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView
import com.followapp.mytasks.common.entities.Task

class TaskListAdapter(
    private val tasks: List<Task>,
    private val onItemClick: (Task) -> Unit,
    private val onCheckedChange: (Task, Boolean) -> Unit
) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val composeView = ComposeView(parent.context)
        return TaskViewHolder(composeView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task, onItemClick, onCheckedChange)
    }

    override fun getItemCount(): Int = tasks.size

    class TaskViewHolder(private val composeView: ComposeView) : RecyclerView.ViewHolder(composeView) {
        fun bind(task: Task, onItemClick: (Task) -> Unit, onCheckedChange: (Task, Boolean) -> Unit) {
            composeView.setContent {
                TaskItem(
                    taskTitle = task.title,
                    isChecked = task.isDone,
                    onCheckedChange = { isChecked -> onCheckedChange(task, isChecked) },
                    onItemClick = { onItemClick(task) }
                )
            }
        }
    }
}

//package com.followapp.mytasks.homeModule.view
//
//import android.graphics.Paint
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.followapp.mytasks.R
//import com.followapp.mytasks.common.entities.Task
//import com.followapp.mytasks.common.utils.OnTaskClickListener
//import com.followapp.mytasks.databinding.ItemTaskBinding
//
//class TaskListAdapter : ListAdapter<Task, RecyclerView.ViewHolder>(TaskDiff()) {
//
//    lateinit var listener: OnTaskClickListener
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false))
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val task = getItem(position)
//        (holder as ViewHolder).bindItem(task)
//    }
//
//    fun setOnClickListener(listener: OnTaskClickListener) {
//        this.listener = listener
//    }
//
//    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val binding = ItemTaskBinding.bind(view)
//
//        fun bindItem(task: Task) {
//            setupUIComponents(task)
//            setupListeners(task)
//        }
//
//        private fun setupUIComponents(task: Task) {
//            with(binding) {
//                tvTaskTitle.text = task.title
//                cbTaskDone.isChecked = task.isDone
//                tvTaskTitle.paintFlags = if (task.isDone) {
//                    binding.tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//                } else {
//                    binding.tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
//                }
//            }
//        }
//
//        private fun setupListeners(task: Task) {
//            with(binding) {
//                root.setOnClickListener {
//                    listener.onTaskClick(task)
//                }
//                cbTaskDone.setOnClickListener {
//                    listener.onTaskCheckBoxClick(task)
//                }
//            }
//        }
//    }
//}