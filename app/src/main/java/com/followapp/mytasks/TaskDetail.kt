package com.followapp.mytasks

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Locale

class TaskDetail : AppCompatActivity() {
    private lateinit var taskTitle: TextView
    private lateinit var taskDescription: TextView
    private lateinit var dueDate: TextView
    private lateinit var deleteButton: Button
    private lateinit var closeButton: Button

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_detail)

        taskTitle = findViewById(R.id.editTextTaskTitleDetail)
        taskDescription = findViewById(R.id.editTextTaskDescription)
        dueDate = findViewById(R.id.editTextDueDate)
        deleteButton = findViewById(R.id.buttonDeleteTask)
        closeButton = findViewById(R.id.buttonCloseDetail)

        // Get the task from SelectedTask
        if (SelectedTask.position != -1) {
            // Display task details
            taskTitle.text = SelectedTask.tasksList[SelectedTask.position].title
            taskDescription.text = SelectedTask.tasksList[SelectedTask.position].description
//            dueDate.text = dateFormat.format(task.dueDate!!)  // TODO esto puede dar null

            deleteButton.setOnClickListener {
                // Handle delete action
            }
        } else {
            // Handle new task creation
            deleteButton.visibility = View.GONE // Hide delete button for new task
        }

        closeButton.setOnClickListener {
            // Save the task
            val newTask = Task(
                taskTitle.text.toString(),
                false,
                description = taskDescription.text.toString(),
//                dateFormat.parse(dueDate.text.toString())
            )
            if(SelectedTask.position == -1) SelectedTask.tasksList.add(newTask)

//            if (SelectedTask.isNewTask) {
//                // Add the new task to the list
//                tasksList.add(newTask)
//                tasksAdapter.notifyDataSetChanged()
//            } else {
//                // Update the existing task
//                val index = tasksList.indexOf(SelectedTask.task)
//                if (index != -1) {
//                    tasksList[index] = newTask
//                    tasksAdapter.notifyDataSetChanged()
//                }

            // Close the detail view
            finish()
        }
    }
}
