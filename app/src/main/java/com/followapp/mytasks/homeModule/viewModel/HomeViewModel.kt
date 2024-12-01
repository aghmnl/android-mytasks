package com.followapp.mytasks.homeModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.homeModule.model.HomeRepository
import com.followapp.mytasks.tasksModule.model.domain.TaskManager.onItemChanged
import com.followapp.mytasks.tasksModule.model.domain.TaskManager.onItemInserted
import com.followapp.mytasks.tasksModule.model.domain.TaskManager.onItemRemoved
import com.followapp.mytasks.tasksModule.model.domain.TaskManager.selectedTaskIndex
import com.followapp.mytasks.tasksModule.model.domain.TaskManager.tasksList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private val _allTasks = MutableLiveData<List<Task>>()
    val allTasks: LiveData<List<Task>> = _allTasks

    init {
        getAllTasks()
    }

    fun getAllTasks() {
        repository.allTasks
    }

    fun addTask(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insert(task)
            tasksList.add(task)
            onItemInserted?.invoke()
        }
    }

    fun updateTask(task: Task) {
        tasksList[selectedTaskIndex] = task
        CoroutineScope(Dispatchers.IO).launch {
            repository.update(task)
            onItemChanged?.invoke()
        }
    }

    fun deleteTask(task: Task) {
        tasksList.remove(task)
        CoroutineScope(Dispatchers.IO).launch {
            repository.delete(task)
            onItemRemoved?.invoke()
        }
    }


}


