package com.followapp.mytasks

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TaskDetail : AppCompatActivity() {
    private lateinit var taskTitle: TextView
    private lateinit var taskDescription: TextView
    private lateinit var dueDate: TextView
    private lateinit var deleteButton: Button
    private lateinit var closeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_detail)

        taskTitle = findViewById(R.id.textViewTaskTitleDetail)
        taskDescription = findViewById(R.id.textViewTaskDescription)
        dueDate = findViewById(R.id.textViewDueDate)
        deleteButton = findViewById(R.id.buttonDeleteTask)
        closeButton = findViewById(R.id.buttonCloseDetail)

        // Get the task from SelectedTask
        val task = SelectedTask.task

        if (task != null) {
            // Display task details
            taskTitle.text = task.title
            taskDescription.text = task.description
            dueDate.text = task.dueDate.toString()

            deleteButton.setOnClickListener {
                // Handle delete action
            }
        } else {
            // Handle new task creation
            deleteButton.visibility = View.GONE // Hide delete button for new task
        }

        closeButton.setOnClickListener {
            // Close the detail view
            finish()
        }
    }
}
