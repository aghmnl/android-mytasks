package com.followapp.mytasks.homeModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.followapp.mytasks.R
import com.followapp.mytasks.common.entities.Task
import com.followapp.mytasks.common.utils.OnTaskClickListener
import com.followapp.mytasks.databinding.FragmentHomeBinding
import com.followapp.mytasks.detailModule.view.DetailFragment
import com.followapp.mytasks.homeModule.model.HomeRepository
import com.followapp.mytasks.homeModule.model.domain.HomeRoomDatabase
import com.followapp.mytasks.homeModule.viewModel.HomeViewModel
import com.followapp.mytasks.homeModule.viewModel.HomeViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeFragment : Fragment(), OnTaskClickListener {

    private var _binding: FragmentHomeBinding? = null
    val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var tasksAdapter: TaskListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupAdapter()
        setupRecyclerView()
        setupObservers()
        setupButtons()
    }

    private fun setupViewModel() {
        homeViewModel = ViewModelProvider(this, HomeViewModelFactory(HomeRepository(HomeRoomDatabase())))[HomeViewModel::class.java]
    }

    private fun setupAdapter() {
        tasksAdapter = TaskListAdapter()
        tasksAdapter.setOnClickListener(this)
    }

    private fun setupRecyclerView() {
        binding.recyclerViewTasks.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = this@HomeFragment.tasksAdapter
        }
    }

    private fun setupObservers() {
        homeViewModel.allTasks.observe(viewLifecycleOwner, Observer { tasks ->
            tasks?.let {
                tasksAdapter.submitList(it)
                getTasks()  // I don't know if here is the best place to put it.
            }
        })
    }

    private fun setupButtons() {
        binding.fabAddTask.setOnClickListener {
            val detailFragment = DetailFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container_main, detailFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }


//    private fun openTaskDetail(task: Task?) {
//        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(task)
//        findNavController().navigate(action)
//    }

    override fun onTaskClick(task: Task) {
        val detailFragment = DetailFragment()
        val args = Bundle()
        args.putLong("taskId", task.id)
        detailFragment.arguments = args
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.container_main, detailFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun getTasks() {
        homeViewModel.getAllTasks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}