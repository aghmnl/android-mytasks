package com.followapp.mytasks.detailModule.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.followapp.mytasks.R
import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.detailModule.model.DetailRepository
import com.followapp.mytasks.detailModule.model.domain.DetailRoomDatabase
import com.followapp.mytasks.detailModule.viewModel.DetailViewModel
import com.followapp.mytasks.detailModule.viewModel.DetailViewModelFactory
import com.followapp.mytasks.tasksModule.model.domain.TaskManager
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment : Fragment() {
    private lateinit var taskViewModel: DetailViewModel
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel = ViewModelProvider(this, DetailViewModelFactory(DetailRepository(DetailRoomDatabase())))[DetailViewModel::class.java]
        editTextTaskTitleDetail = view.findViewById(R.id.editTextTaskTitleDetail)
        editTextTaskDescription = view.findViewById(R.id.editTextTaskDescription)
        buttonShowDatePicker = view.findViewById(R.id.buttonShowDatePicker)
        saveButton = view.findViewById(R.id.buttonSaveTask)
        deleteButton = view.findViewById(R.id.buttonDeleteTask)
        closeButton = view.findViewById(R.id.buttonCloseDetail)

        if (selectedTaskIndex > -1) {
            task = TaskManager.tasksList[selectedTaskIndex]
            editTextTaskTitleDetail.setText(task?.title)
            editTextTaskDescription.setText(task?.description ?: "")
            task?.dueDate?.let {
                calendar.time = it
                buttonShowDatePicker.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
            }
            deleteButton.setOnClickListener {
                Log.v("DebugAgus", "selectedTaskIndex = $selectedTaskIndex")
                task?.let { taskViewModel.deleteTask(it) }
                findNavController().navigateUp()
            }
        } else {
            deleteButton.visibility = View.GONE
        }

        buttonShowDatePicker.setOnClickListener {
            materialDatePicker.show(parentFragmentManager, "DATE_PICKER")
        }

        val datePicker = MaterialDatePicker.Builder.datePicker()
        materialDatePicker = datePicker.build()
        materialDatePicker.addOnPositiveButtonClickListener {
            calendar.timeInMillis = it
            task?.dueDate = calendar.time
            buttonShowDatePicker.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
        }

        saveButton.setOnClickListener {
            if (selectedTaskIndex > -1) {
                val newTask = Task(
                    editTextTaskTitleDetail.text.toString(),
                    false,
                    id = task?.id ?: 0,
                    description = editTextTaskDescription.text.toString(),
                    dueDate = task?.dueDate
                )
                taskViewModel.updateTask(newTask)
            } else {
                val newTask = Task(
                    editTextTaskTitleDetail.text.toString(),
                    false,
                    description = editTextTaskDescription.text.toString(),
                    dueDate = task?.dueDate
                )
                taskViewModel.addTask(newTask)
            }
            findNavController().navigateUp()
        }

        closeButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
