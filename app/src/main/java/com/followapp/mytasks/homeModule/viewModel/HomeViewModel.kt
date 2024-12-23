package com.followapp.mytasks.homeModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.homeModule.model.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private val _allTasks = MutableLiveData<List<Task>>()
    val allTasks: LiveData<List<Task>> get() = _allTasks

    fun setTasks(value: List<Task>) = _allTasks.postValue(value)

    init {
        getAllTasks()
    }

    fun toggleTaskDone(task: Task) {
        task.isDone = !task.isDone
        updateTask(task)
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = repository.updateTask(task)
                if (result == 0) {
                    Log.i("IMPORTANTE", "No se pudo modificar la tarea")
                }
            }
        }
    }

    fun getAllTasks() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getAllTasks()
            }
            setTasks(result)
        }
    }
}