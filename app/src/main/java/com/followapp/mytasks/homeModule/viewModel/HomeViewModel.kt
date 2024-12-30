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

    private var _allTasks = MutableLiveData<List<Task>>()
    private var _unsortedTasks: List<Task> = emptyList()
    private var _sortingCriteria: String? = null
    val allTasks: LiveData<List<Task>> get() = _allTasks

    private val _isGrouped = MutableLiveData<Boolean>(false)
    val isGrouped: LiveData<Boolean> get() = _isGrouped

    init {
        getAllTasks()
    }

    fun toggleTaskDone(task: Task) {
        task.isDone = !task.isDone
        updateTask(task)
    }

    fun toggleGrouping() {
        viewModelScope.launch {
            _isGrouped.value = _isGrouped.value != true
            sortTasks()
        }

        Log.i("IMPORTANTE", "Grouping: ${_isGrouped.value}")
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = repository.updateTask(task)
                if (result == 0) {
                    Log.i("IMPORTANTE", "No se pudo modificar la tarea")
                }
            }
        }
    }

    fun sortTasks(criteria: String? = null) {
        _sortingCriteria = criteria
        if (_isGrouped.value == true) {
            _allTasks.value = when (criteria) {
                "az" -> _unsortedTasks.sortedBy { it.title }
                "za" -> _unsortedTasks.sortedByDescending { it.title }
                else -> _unsortedTasks
            }.sortedBy { it.isDone }
        } else {
            _allTasks.value = when (criteria) {
                "az" -> _unsortedTasks.sortedBy { it.title }
                "za" -> _unsortedTasks.sortedByDescending { it.title }
                else -> _unsortedTasks
            }
        }
    }

    fun getAllTasks() {
        viewModelScope.launch {
            _unsortedTasks = withContext(Dispatchers.IO) {
                repository.getAllTasks()
            }
            sortTasks()
        }
    }
}