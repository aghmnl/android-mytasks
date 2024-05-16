package com.followapp.mytasks

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TaskDetail : AppCompatActivity() {
    private lateinit var editTextTaskTitleDetail: EditText
    private lateinit var editTextTaskDescription: EditText

    private lateinit var buttonShowDatePicker: Button
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var closeButton: Button
    private val selectedTaskIndex = TaskManager.selectedTaskIndex
    private val task =
        if (selectedTaskIndex != -1 && selectedTaskIndex < TaskManager.tasksList.size) TaskManager.tasksList[selectedTaskIndex] else Task("", false)
    private val calendar = Calendar.getInstance()
    private lateinit var materialDatePicker: MaterialDatePicker<Long>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_detail)

        editTextTaskTitleDetail = findViewById(R.id.editTextTaskTitleDetail)
        editTextTaskDescription = findViewById(R.id.editTextTaskDescription)
        buttonShowDatePicker = findViewById(R.id.buttonShowDatePicker)
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
                buttonShowDatePicker.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
            }

            deleteButton.setOnClickListener {
                TaskManager.deleteTask(task)
                finish()
            }
        } else {
            // Handle new task creation
            deleteButton.visibility = View.GONE // Hide delete button for new task
        }

        // This button opens the Docked Date Picker
        buttonShowDatePicker.setOnClickListener {
            materialDatePicker.show(supportFragmentManager, "DATE_PICKER")
        }

        // Initialize the MaterialDatePicker
        val datePicker = MaterialDatePicker.Builder.datePicker()
        materialDatePicker = datePicker.build()




        // When the date is changed, it updates task.dueDate with the new date
        materialDatePicker.addOnPositiveButtonClickListener {
            calendar.timeInMillis = it
            task.dueDate = calendar.time
            buttonShowDatePicker.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
        }

        saveButton.setOnClickListener {
            // Create a new task or update an existing one
            if (selectedTaskIndex > -1) {
                val newTask = Task(
                    editTextTaskTitleDetail.text.toString(),
                    false,
                    id = task.id,
                    description = editTextTaskDescription.text.toString(),
                    dueDate = task.dueDate
                )
                TaskManager.updateTask(newTask)

            } else {
                val newTask = Task(
                    editTextTaskTitleDetail.text.toString(),
                    false,
                    description = editTextTaskDescription.text.toString(),
                    dueDate = task.dueDate
                )
                TaskManager.addTask(newTask)
            }
            finish()
        }

        closeButton.setOnClickListener {
            // Close the detail view
            finish()
        }
    }
}
