package com.followapp.mytasks.homeModule.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.followapp.mytasks.common.dataAccess.room.AppDatabase
import com.followapp.mytasks.common.dataAccess.room.TaskDAO
import com.followapp.mytasks.common.entities.Task
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val taskDAO: TaskDAO = AppDatabase.getDatabase(application).taskDAO()
    val allTasks: LiveData<List<Task>> = taskDAO.getAll().asLiveData()

    fun insert(task: Task) = viewModelScope.launch {
        taskDAO.insert(task)
    }

    fun update(task: Task) = viewModelScope.launch {
        taskDAO.update(task)
    }

    fun delete(task: Task) = viewModelScope.launch {
        taskDAO.delete(task)
    }
}
