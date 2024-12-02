package com.followapp.mytasks.detailModule.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.detailModule.model.DetailRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: DetailRepository) : ViewModel() {
    fun addTask(task: Task) {
        viewModelScope.launch {
            val result = repository.addTask(task)
            if (result == -1L) {
                Log.i("IMPORTANTE", "No se pudo agregar la tarea")
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            val result = repository.updateTask(task)
            if (result == 0) {
                Log.i("IMPORTANTE", "No se pudo modificar la tarea")
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            val result = repository.deleteTask(task)
            if (result == 0) {
                Log.i("IMPORTANTE", "No se pudo borrar la tarea")
            }
        }
    }
}