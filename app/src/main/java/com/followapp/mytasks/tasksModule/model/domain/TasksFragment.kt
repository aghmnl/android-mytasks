//package com.followapp.mytasks.tasksModule.model.domain
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import com.followapp.mytasks.R
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.Observer
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.followapp.mytasks.detailModule.view.TaskDetail
//import com.followapp.mytasks.homeModule.view.TasksAdapter
//import com.google.android.material.floatingactionbutton.FloatingActionButton
//
//class TasksFragment : Fragment(R.layout.fragment_tasks) {
//    private val taskViewModel: TaskViewModel by viewModels()
//    private lateinit var tasksAdapter: TasksAdapter
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        tasksAdapter = TasksAdapter()
//        val tasksRecyclerView: RecyclerView = view.findViewById(R.id.recyclerViewTasks)
//        tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        tasksRecyclerView.adapter = tasksAdapter
//
//        taskViewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
//            tasksAdapter.submitList(tasks)
//        })
//
//        val addTaskButton: FloatingActionButton = view.findViewById(R.id.fabAddTask)
//        addTaskButton.setOnClickListener {
//            val intent = Intent(requireContext(), TaskDetail::class.java)
//            startActivity(intent)
//        }
//    }
//}}