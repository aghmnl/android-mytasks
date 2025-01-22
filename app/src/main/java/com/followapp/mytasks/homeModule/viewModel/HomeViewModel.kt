package com.followapp.mytasks.homeModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.homeModule.model.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private var _allTasks = MutableLiveData<List<Task>>()
    private var _unsortedTasks: List<Task> = emptyList()
    private var _sortingCriteria: String? = null
    val allTasks: LiveData<List<Task>> get() = _allTasks

    private val _isGrouped = MutableLiveData<Boolean>(false)
    val isGrouped: LiveData<Boolean> get() = _isGrouped

//    init {
//        Log.i("IMPORTANTE", "HomeViewModel: about to run getAllTasks() from init")
//        getAllTasks()
//    }

    fun toggleTaskDone(task: Task) {
        task.isDone = !task.isDone
        task.version++
        viewModelScope.launch {
            updateTask(task)
            Log.i("IMPORTANTE", "4. HomeViewModel: toggleTaskDone executed. Task: ${task.title} (isDone: ${task.isDone} - version: ${task.version})")
            delay(100) // Add a slight delay to ensure updates are synchronized
            _allTasks.value = _allTasks.value?.toMutableList() // Create a new list to trigger DiffUtil
            Log.i("IMPORTANTE", "HomeViewModel: toggleTaskDone after the delay")

        }
    }


    fun toggleGrouping() {
        viewModelScope.launch {
            _isGrouped.value = _isGrouped.value != true
            sortTasks(_sortingCriteria)
        }
    }

    private suspend fun updateTask(task: Task) {
        withContext(Dispatchers.IO) {
            Log.i("IMPORTANTE", "2. HomeViewModel: updateTask (before repository.updateTask). Task: ${task.title} (isDone: ${task.isDone} - version: ${task.version})")
            val result = repository.updateTask(task)
            if (result == 0) {
                Log.i("IMPORTANTE", "No se pudo modificar la tarea")
            } else {
                Log.i("IMPORTANTE", "3. HomeViewModel: updateTask (after repository.updateTask). Task: ${task.title} (isDone: ${task.isDone} - version: ${task.version})")
            }
            Log.i("IMPORTANTE", "HomeViewModel: about to run getAllTasks() from updateTask")
            getAllTasks()
        }
    }

    fun sortTasks(criteria: String? = null) {
//        Log.i("IMPORTANTE", "Calling sortTasks()")
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
            Log.i("IMPORTANTE", "HomeViewModel: getAllTasks. Tasks: $_unsortedTasks)")
            sortTasks()
        }
    }
}