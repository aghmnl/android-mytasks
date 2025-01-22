package com.followapp.mytasks.homeModule.view

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.followapp.mytasks.common.entities.Task

class TaskDiff : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        val areItemsSame = oldItem.id == newItem.id
//        if(!areItemsSame)
            Log.i("IMPORTANTE", "TaskDiff - areItemsTheSame: ${oldItem.title} (isDone: ${oldItem.isDone}, version: ${oldItem.version}) vs ${newItem.title} (isDone: ${newItem.isDone}, version: ${newItem.version}) = $areItemsSame")
        return areItemsSame
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        val areContentsSame = oldItem.title == newItem.title &&
                oldItem.isDone == newItem.isDone &&
                oldItem.version == newItem.version
//        if(!areContentsSame)
            Log.i("IMPORTANTE", "TaskDiff - areContentsTheSame: ${oldItem.title} (isDone: ${oldItem.isDone}, version: ${oldItem.version}) vs ${newItem.title} (isDone: ${newItem.isDone}, version: ${newItem.version}) = $areContentsSame")
        return areContentsSame
    }
}


