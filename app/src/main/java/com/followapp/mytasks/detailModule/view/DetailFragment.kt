package com.followapp.mytasks.detailModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.followapp.mytasks.R
import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.common.utils.toUTCLocalDateTime
import com.followapp.mytasks.databinding.FragmentDetailBinding
import com.followapp.mytasks.detailModule.viewModel.DetailViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private var _taskId: Long = -1L
    private val detailViewModel: DetailViewModel by inject()

    private val calendar = Calendar.getInstance()
    private lateinit var materialDatePicker: MaterialDatePicker<Long>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setArguments()
        setupObservers()
        setupUIComponents()
        setupListeners()
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
                with(binding) {
                    editTextTaskTitleDetail.setText(it.title)
                    editTextTaskDescription.setText(it.description)
                    checkBoxDone.isChecked = it.isDone
                    if (it.dueDate != null) {
                        buttonShowDatePicker.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it.dueDate ?: Date())
                        calendar.time = it.dueDate ?: Date()
                    } else {
                        buttonShowDatePicker.text = getString(R.string.select_date)
                    }
                }
            }
        }
    }

    private fun setupUIComponents() {
        materialDatePicker = MaterialDatePicker.Builder.datePicker().build()
        materialDatePicker.addOnPositiveButtonClickListener { selection ->
            selection?.let {
                calendar.timeInMillis = it.toUTCLocalDateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                binding.buttonShowDatePicker.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
            }
        }

        // Show or hide delete button based on whether taskId is valid
        binding.buttonDeleteTask.visibility = if (_taskId != -1L) View.VISIBLE else View.GONE
    }

    private fun setupListeners() {
        with(binding) {
            buttonSaveTask.setOnClickListener {
                val title = editTextTaskTitleDetail.text.toString()
                if (title.isBlank()) {
                    Toast.makeText(requireContext(), getString(R.string.enter_a_title), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener  // To exit the listener
                }

                val task = Task(
                    title,
                    description = editTextTaskDescription.text.toString(),
                    isDone = checkBoxDone.isChecked,
                    dueDate = if (buttonShowDatePicker.text != getString(R.string.select_date)) calendar.time else null
                )

                lifecycleScope.launch {
                    if (_taskId == -1L) {
                        detailViewModel.addTask(task)
                    } else {
                        task.id = _taskId
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
    }

    private fun closeDetail() {
        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
