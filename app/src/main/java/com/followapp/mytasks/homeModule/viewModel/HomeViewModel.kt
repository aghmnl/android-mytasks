package com.followapp.mytasks.homeModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.homeModule.model.HomeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private val _allTasks = MutableLiveData<List<Task>>()
    val allTasks: LiveData<List<Task>> = _allTasks

    init {
        getAllTasks()
    }

    fun setTasks(value: List<Task>) {  // protected es necesario para que las clases hijas también tengan acceso
        _allTasks.postValue(value)
    }

    fun getAllTasks() {
        viewModelScope.launch {
            val result = repository.allTasks
            setTasks(result)
        }
    }

    fun addTask(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = repository.addTask(task)
            if (result == -1L) {
                Log.i("IMPORTANTE", "No se pudo agregar la tarea")
            }
        }
    }

    fun updateTask(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = repository.updateTask(task)
            if (result == 0) {
                Log.i("IMPORTANTE", "No se pudo modificar la tarea")
            }
        }
    }

    fun deleteTask(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = repository.deleteTask(task)
            if (result == 0) {
                Log.i("IMPORTANTE", "No se pudo borrar la tarea")
            }
        }
    }


}


