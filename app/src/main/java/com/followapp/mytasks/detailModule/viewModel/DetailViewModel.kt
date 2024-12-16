package com.followapp.mytasks.detailModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.detailModule.model.DetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel(private val repository: DetailRepository) : ViewModel() {

    private val _task = MutableLiveData<Task?>()
    val task: LiveData<Task?> = _task

    fun setTask(value: Task?) = _task.postValue(value)

    fun getTaskById(id: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val task = repository.getTaskById(id)
                setTask(task)
                if (task == null) {
                    Log.i("IMPORTANTE", "No se pudo encontrar la tarea")
                }
            }
        }
    }


    fun addTask(task: Task) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = repository.addTask(task)
                if (result == -1L) {
                    Log.i("IMPORTANTE", "No se pudo agregar la tarea")
                }
            }
        }
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

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = repository.deleteTask(id)
                if (result == 0) {
                    Log.i("IMPORTANTE", "No se pudo borrar la tarea")
                }
            }
        }
    }
}