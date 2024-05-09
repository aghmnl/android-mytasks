package com.followapp.mytasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TasksActivity : AppCompatActivity() {
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var addTaskButton: FloatingActionButton

    private val tasksAdapter = TasksAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        TaskManager.initialize(this)


//        addSomeTasks()

        tasksRecyclerView = findViewById(R.id.recyclerViewTasks)
        addTaskButton = findViewById(R.id.fabAddTask)

        tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        tasksRecyclerView.adapter = tasksAdapter

        addTaskButton.setOnClickListener {
            TaskManager.selectedTaskIndex = -1

            // Handle click event to add new task
            val intent = Intent(this, TaskDetail::class.java)
            startActivity(intent)
        }

        TaskManager.onItemInserted = { tasksAdapter.notifyItemInserted(TaskManager.tasksList.size) }
        TaskManager.onItemChanged = { tasksAdapter.notifyItemRangeChanged(TaskManager.selectedTaskIndex, TaskManager.tasksList.size) }
        TaskManager.onItemRemoved = { tasksAdapter.notifyItemRemoved(TaskManager.selectedTaskIndex) }

    }




//    // Get all tasks
//    private fun getTasks(): List<Task> {
//        val json = sharedPreferences.getString("tasks", null)
//        val type = object : TypeToken<List<Task>>() {}.type
//        return gson.fromJson(json, type) ?: emptyList()
//    }

//    private fun saveTasks(tasks: List<Task>) {
//        val editor = sharedPreferences.edit()
//        val json = gson.toJson(tasks)
//        editor.putString("tasks", json)
//        editor.apply()
//    }
}