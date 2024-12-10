package com.followapp.mytasks.detailModule.view

import android.os.Bundle
import android.util.Log
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
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private var _taskId: Long = -1L
    private lateinit var detailViewModel: DetailViewModel

    private val calendar = Calendar.getInstance()
    private lateinit var materialDatePicker: MaterialDatePicker<Long>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setArguments()
        setupObservers()
        setupButtons()
        setupListeners()


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


    }

    private fun setArguments() {
        arguments?.let {
            _taskId = it.getLong("taskId", -1L)
            getTaskById()
        }
    }

    private fun setupObservers() {
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

    private fun setupButtons() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
        materialDatePicker = datePicker.build()
        materialDatePicker.addOnPositiveButtonClickListener {
            calendar.timeInMillis = it
//            task?.dueDate = calendar.time
            binding.buttonShowDatePicker.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
        }

        // Show or hide delete button based on whether taskId is valid
        binding.buttonDeleteTask.visibility = if (_taskId != -1L) View.VISIBLE else View.GONE
    }

    private fun setupListeners() {
        with(binding) {
            buttonSaveTask.setOnClickListener {
                val task = Task(
                    editTextTaskTitleDetail.text.toString(),
                    description = editTextTaskDescription.text.toString(),
                    dueDate = calendar.time
                )

                lifecycleScope.launch {
                    if (_taskId == -1L) {
                        detailViewModel.addTask(task)
                    } else {
                        task.id = _taskId
                        Log.i("IMPORTANTE TaskId: ", task.id.toString())
                        detailViewModel.updateTask(task)
                    }
                    closeDetail()
                }
            }

            buttonCloseDetail.setOnClickListener {
                closeDetail()
            }

            buttonShowDatePicker.setOnClickListener {
                materialDatePicker.show(parentFragmentManager, "DATE_PICKER")
            }

            buttonDeleteTask.setOnClickListener {
                if (_taskId != -1L) {
                    lifecycleScope.launch {
                        detailViewModel.deleteTask(_taskId)
                        closeDetail()
                    }
                }
            }
        }
    }

    private fun getTaskById() {
        if (_taskId != -1L) detailViewModel.getTaskById(_taskId)
        Log.i("IMPORTANTE taskId x/arg: ", _taskId.toString())
    }

    private fun closeDetail() {
        parentFragmentManager.popBackStack()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViewModel() {
        detailViewModel = ViewModelProvider(this, DetailViewModelFactory(DetailRepository(DetailRoomDatabase())))[DetailViewModel::class.java]
    }
}
