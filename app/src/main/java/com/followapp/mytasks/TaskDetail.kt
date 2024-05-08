package com.followapp.mytasks

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class TaskDetail : AppCompatActivity() {
    private lateinit var editTextTaskTitleDetail: EditText
    private lateinit var editTextTaskDescription: EditText
    private lateinit var datePickerDueDate: DatePicker
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var closeButton: Button
    private val selectedTaskIndex = TaskManager.selectedTaskIndex
    private val task = if (selectedTaskIndex != -1) TaskManager.tasksList[selectedTaskIndex] else Task("", false)
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_detail)

        editTextTaskTitleDetail = findViewById(R.id.editTextTaskTitleDetail)
        editTextTaskDescription = findViewById(R.id.editTextTaskDescription)
        datePickerDueDate = findViewById(R.id.datePickerDueDate)
        saveButton = findViewById(R.id.buttonSaveTask)
        deleteButton = findViewById(R.id.buttonDeleteTask)
        closeButton = findViewById(R.id.buttonCloseDetail)

        // Get the task from SelectedTask
        if (selectedTaskIndex > -1) {

            // Set the Title and description if not null the latter
            editTextTaskTitleDetail.setText(task.title)
            if (task.description != null) editTextTaskDescription.setText(task.description)

            // Set the DatePicker to the task's due date if not null
            if (task.dueDate != null) {
                calendar.time = task.dueDate!!
                datePickerDueDate.updateDate(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
            }

            deleteButton.setOnClickListener {
                TaskManager.deleteTask()
                finish()


            }
        } else {
            // Handle new task creation
            deleteButton.visibility = View.GONE // Hide delete button for new task
        }

        // When the date is changed, it updates task.dueDate with the new date
        datePickerDueDate.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)) { _, year, month, day ->
            calendar.set(year, month, day)
            task.dueDate = calendar.time
        }

        saveButton.setOnClickListener {
            // Adds a new task
            val newTask = Task(
                editTextTaskTitleDetail.text.toString(),
                false,
                description = editTextTaskDescription.text.toString(),
                dueDate = task.dueDate
            )

            if (selectedTaskIndex > -1) {
                TaskManager.updateTask(newTask)
            }

            if (selectedTaskIndex == -1) {
                TaskManager.addTask(newTask)
            }

            // Close the detail view
            finish()
        }

        closeButton.setOnClickListener {
            // Close the detail view
            finish()
        }
    }
}
