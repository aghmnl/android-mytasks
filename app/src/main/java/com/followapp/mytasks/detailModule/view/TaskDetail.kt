package com.followapp.mytasks.detailModule.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.followapp.mytasks.R
import com.followapp.mytasks.tasksModule.model.domain.TaskManager
import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.homeModule.viewModel.HomeViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TaskDetail : AppCompatActivity() {
    private lateinit var taskViewModel: HomeViewModel
    private var task: Task? = null

    private lateinit var editTextTaskTitleDetail: EditText
    private lateinit var editTextTaskDescription: EditText

    private lateinit var buttonShowDatePicker: Button
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var closeButton: Button
    private val selectedTaskIndex = TaskManager.selectedTaskIndex
    private val calendar = Calendar.getInstance()
    private lateinit var materialDatePicker: MaterialDatePicker<Long>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_detail)

        taskViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        editTextTaskTitleDetail = findViewById(R.id.editTextTaskTitleDetail)
        editTextTaskDescription = findViewById(R.id.editTextTaskDescription)
        buttonShowDatePicker = findViewById(R.id.buttonShowDatePicker)
        saveButton = findViewById(R.id.buttonSaveTask)
        deleteButton = findViewById(R.id.buttonDeleteTask)
        closeButton = findViewById(R.id.buttonCloseDetail)

        // Get the task from SelectedTask
        if (selectedTaskIndex > -1) {
            task = TaskManager.tasksList[selectedTaskIndex]


            // Set the Title and description if not null the latter
            editTextTaskTitleDetail.setText(task?.title)
            editTextTaskDescription.setText(task?.description ?: "")

            // Set the DatePicker to the task's due date if not null
            task?.dueDate?.let {
                calendar.time = it
                buttonShowDatePicker.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
            }

            deleteButton.setOnClickListener {
//                Log.v("DebugAgus", "selectedTaskIndex = $selectedTaskIndex")
//                Log.v("DebugAgus", "TaskManager.tasksList.size = " + TaskManager.tasksList.size)
                Log.v("DebugAgus", "selectedTaskIndex = $selectedTaskIndex")  // Cuando queda sólo una tarea y el selectedTaskIndex es 1, no la puede borrar

                task?.let { TaskManager.deleteTask(it) }
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
            task?.dueDate = calendar.time
            buttonShowDatePicker.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
        }

        saveButton.setOnClickListener {
            // Create a new task or update an existing one
            if (selectedTaskIndex > -1) {
                val newTask = Task(
                    editTextTaskTitleDetail.text.toString(),
                    false,
                    id = task?.id ?: 0,
                    description = editTextTaskDescription.text.toString(),
                    dueDate = task?.dueDate
                )
                TaskManager.updateTask(newTask)

            } else {
                val newTask = Task(
                    editTextTaskTitleDetail.text.toString(),
                    false,
                    description = editTextTaskDescription.text.toString(),
                    dueDate = task?.dueDate
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
