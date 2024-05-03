package com.followapp.mytasks

//import android.content.Context
//import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import java.time.LocalTime
//import java.util.Date

//import com.google.android.material.floatingactionbutton.FloatingActionButton
//import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
//import com.google.gson.Gson

class TasksActivity : AppCompatActivity() {
    private lateinit var tasksRecyclerView: RecyclerView
//    private lateinit var addTaskButton: FloatingActionButton

//    private lateinit var sharedPreferences: SharedPreferences
//    private val gson = Gson()

    private val task1 = Task("Hacer las compras",false)
    private val task2 = Task("Ir al gym", false)
    private val task3 = Task("Programar", true)
    private val taskList = listOf(task1, task2, task3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        tasksRecyclerView = findViewById(R.id.recyclerViewTasks)
//        addTaskButton = findViewById(R.id.fabAddTask)

        tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        tasksRecyclerView.adapter = TasksAdapter(taskList)

//        addTaskButton.setOnClickListener {
//            // Handle click event to add new task
//        }

//        sharedPreferences = getSharedPreferences("tasks", Context.MODE_PRIVATE)
    }

//    // Add a task
//    private fun addTask(task: Task) {
//        val tasks = getTasks().toMutableList()
//        tasks.add(task)
//        saveTasks(tasks)
//    }
//
//    // Get all tasks
//    private fun getTasks(): List<Task> {
//        val json = sharedPreferences.getString("tasks", null)
//        val type = object : TypeToken<List<Task>>() {}.type
//        return gson.fromJson(json, type) ?: emptyList()
//    }
//
//    // Delete a task
//    private fun deleteTask(task: Task) {
//        val tasks = getTasks().toMutableList()
//        tasks.remove(task)
//        saveTasks(tasks)
//    }
//
//    // Mark a task as done
//    private fun markTaskAsDone(task: Task) {
//        val tasks = getTasks().toMutableList()
//        val index = tasks.indexOf(task)
//        if (index != -1) {
//            tasks[index].isDone = true
//            saveTasks(tasks)
//        }
//    }
//
//    // Edit a task
//    private fun editTask(task: Task) {
//        val tasks = getTasks().toMutableList()
//        val index = tasks.indexOf(task)
//        if (index != -1) {
//            tasks[index] = task
//            saveTasks(tasks)
//        }
//    }
//
//    private fun saveTasks(tasks: List<Task>) {
//        val editor = sharedPreferences.edit()
//        val json = gson.toJson(tasks)
//        editor.putString("tasks", json)
//        editor.apply()
//    }
}