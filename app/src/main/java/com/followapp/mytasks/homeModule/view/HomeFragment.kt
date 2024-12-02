package com.followapp.mytasks.homeModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.followapp.mytasks.R
import com.followapp.mytasks.homeModule.model.HomeRepository
import com.followapp.mytasks.homeModule.model.domain.HomeRoomDatabase
import com.followapp.mytasks.homeModule.viewModel.HomeViewModel
import com.followapp.mytasks.homeModule.viewModel.HomeViewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.Observer
import com.followapp.mytasks.detailModule.view.TaskDetail
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Intent
import com.followapp.mytasks.common.entities.Task


class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var tasksAdapter: TaskListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProvider(this, HomeViewModelFactory(HomeRepository(HomeRoomDatabase())))[HomeViewModel::class.java]
        tasksAdapter = TaskListAdapter { task -> openTaskDetail(task) }

        val tasksRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewTasks)
        tasksRecyclerView.layoutManager = LinearLayoutManager(context)
        tasksRecyclerView.adapter = tasksAdapter

        homeViewModel.allTasks.observe(viewLifecycleOwner, Observer { tasks ->
            tasks?.let { tasksAdapter.submitList(it) }
        })

        view.findViewById<FloatingActionButton>(R.id.fabAddTask).setOnClickListener {
            openTaskDetail(null)
        }
    }

    private fun openTaskDetail(task: Task?) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(task)
        findNavController().navigate(action)
    }
}