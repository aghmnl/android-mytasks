package com.followapp.mytasks.homeModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.homeModule.model.HomeRepository
import com.followapp.mytasks.homeModule.view.TaskListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private var _allTasks = MutableLiveData<List<Task>>()
    val allTasks: LiveData<List<Task>> get() = _allTasks

    private var _unsortedTasks: List<Task> = emptyList()
    private var _sortingCriteria: String? = null

    private val _isGrouped = MutableLiveData<Boolean>(false)
    val isGrouped: LiveData<Boolean> get() = _isGrouped

    fun toggleTaskDone(task: Task, adapter: TaskListAdapter) {
        task.isDone = !task.isDone
        viewModelScope.launch {
            updateTask(task)
            adapter.forceRedraw()
        }
    }

    fun toggleGrouping() {
        _isGrouped.value = _isGrouped.value != true
        sortTasks(_sortingCriteria)
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = repository.updateTask(task)
                if (result == 0) {
                    Log.i("IMPORTANT", "The task could not be modified")
                }
                getAllTasks()
            }
        }
    }

    fun sortTasks(criteria: String? = null) {
        _sortingCriteria = criteria
        val sortedTasks = when (criteria) {
            "az" -> _unsortedTasks.sortedBy { it.title }
            "za" -> _unsortedTasks.sortedByDescending { it.title }
            else -> _unsortedTasks
        }
        _allTasks.value = if (_isGrouped.value == true) {
            sortedTasks.sortedBy { it.isDone }
        } else {
            sortedTasks
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