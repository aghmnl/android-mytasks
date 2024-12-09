package com.followapp.mytasks.detailModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.databinding.FragmentDetailBinding
import com.followapp.mytasks.detailModule.model.DetailRepository
import com.followapp.mytasks.detailModule.model.domain.DetailRoomDatabase
import com.followapp.mytasks.detailModule.viewModel.DetailViewModel
import com.followapp.mytasks.detailModule.viewModel.DetailViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private var _taskId: Long = -1L
    private lateinit var detailViewModel: DetailViewModel


//    private var task: Task? = null
//    private lateinit var _taskId: Task

    private val calendar = Calendar.getInstance()
//    private lateinit var materialDatePicker: MaterialDatePicker<Long>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
//        setupButtons()
        setArguments()
        setupListeners()


//            task = detailViewModel.

        // Populate UI with task details
//            binding.editTextTaskTitleDetail.setText(taskTitle)
//            binding.editTextTaskDescription.setText(taskDescription)
//            if (taskDueDate != -1L) {
//                val date = Date(taskDueDate)
//                binding.buttonShowDatePicker.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
//            }
        // Show or hide delete button based on whether taskId is valid
//            binding.buttonDeleteTask.visibility = if (_taskId != -1L) View.VISIBLE else View.GONE


//        if (selectedTaskIndex > -1) {
////            task = TaskManager.tasksList[selectedTaskIndex]
//            editTextTaskTitleDetail.setText(task?.title)
//            editTextTaskDescription.setText(task?.description ?: "")
//            task?.dueDate?.let {
//                calendar.time = it
//                buttonShowDatePicker.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
//            }
//            deleteButton.setOnClickListener {
//                Log.v("DebugAgus", "selectedTaskIndex = $selectedTaskIndex")
//                task?.let { taskViewModel.deleteTask(it) }
//                findNavController().navigateUp()
//            }
//        } else {
//            deleteButton.visibility = View.GONE
//        }

//    buttonShowDatePicker.setOnClickListener
//    {
//        materialDatePicker.show(parentFragmentManager, "DATE_PICKER")
//    }

//    val datePicker = MaterialDatePicker.Builder.datePicker()
//    materialDatePicker = datePicker.build()
//    materialDatePicker.addOnPositiveButtonClickListener
//    {
//        calendar.timeInMillis = it
////            task?.dueDate = calendar.time
//        buttonShowDatePicker.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
//    }

//        saveButton.setOnClickListener {
//            if (selectedTaskIndex > -1) {
//                val newTask = Task(
//                    editTextTaskTitleDetail.text.toString(),
//                    false,
//                    id = task?.id ?: 0.0,
//                    description = editTextTaskDescription.text.toString(),
//                    dueDate = task?.dueDate
//                )
//                taskViewModel.updateTask(newTask)
//            } else {
//                val newTask = Task(
//                    editTextTaskTitleDetail.text.toString(),
//                    false,
//                    description = editTextTaskDescription.text.toString(),
//                    dueDate = task?.dueDate
//                )
//                taskViewModel.addTask(newTask)
//            }
//            findNavController().navigateUp()
//        }

//        closeButton.setOnClickListener {
//            parentFragmentManager.popBackStack()
//        }
    }

//    private fun setupButtons() {
//        editTextTaskTitleDetail = view.findViewById(R.id.editTextTaskTitleDetail)
//        editTextTaskDescription = view.findViewById(R.id.editTextTaskDescription)
//        buttonShowDatePicker = view.findViewById(R.id.buttonShowDatePicker)
//        saveButton = view.findViewById(R.id.buttonSaveTask)
//        deleteButton = view.findViewById(R.id.buttonDeleteTask)
//        closeButton = view.findViewById(R.id.buttonCloseDetail)
//    }

    private fun setArguments() {
        arguments?.let {
            _taskId = it.getLong("taskId", -1L)
            getTaskById()
        }

        detailViewModel.task.observe(viewLifecycleOwner) { task ->
            task?.let {
                binding.editTextTaskTitleDetail.setText(it.title)
                binding.editTextTaskDescription.setText(it.description)
                task.dueDate?.let {
                    calendar.time = it
                    binding.buttonShowDatePicker.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
                }
                binding.buttonDeleteTask.visibility = View.VISIBLE
            } ?: run {
                binding.buttonDeleteTask.visibility = View.GONE
            }
        }
    }

    private fun setupListeners() {
        with(binding) {
            buttonSaveTask.setOnClickListener {
                val newTask = Task(
                    editTextTaskTitleDetail.text.toString(),
                    description = editTextTaskDescription.text.toString(),
                    dueDate = calendar.time
                )

                lifecycleScope.launch {
                    detailViewModel.addTask(newTask)
                    parentFragmentManager.popBackStack()
                }

            }
            buttonCloseDetail.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun getTaskById() {
        if (_taskId != -1L) detailViewModel.getTaskById(_taskId)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViewModel() {
        detailViewModel = ViewModelProvider(this, DetailViewModelFactory(DetailRepository(DetailRoomDatabase())))[DetailViewModel::class.java]
    }
}
