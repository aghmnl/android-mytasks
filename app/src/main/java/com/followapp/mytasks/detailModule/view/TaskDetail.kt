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








    }
}
