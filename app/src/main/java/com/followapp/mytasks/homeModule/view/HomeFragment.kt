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

//        val tasksRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewTasks)
//        tasksRecyclerView.layoutManager = LinearLayoutManager(context)
//        tasksRecyclerView.adapter = tasksAdapter

        homeViewModel.allTasks.observe(viewLifecycleOwner, Observer { tasks ->
            tasks?.let { tasksAdapter.submitList(it) }
        })

        view.findViewById<FloatingActionButton>(R.id.fabAddTask).setOnClickListener {
//            openTaskDetail(null)
        }
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
        homeViewModel.allTasks.observe(viewLifecycleOwner) { getTasks() }
    }


//    private fun openTaskDetail(task: Task?) {
//        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(task)
//        findNavController().navigate(action)
//    }

    override fun onClick(task: Task) {
        val fragmentManager = childFragmentManager
        val fragment = DetailFragment()
        val args = Bundle()
        args.putLong("id", task.id)
        fragment.arguments = args
        fragment.view
//        fragment.show(fragmentManager, DetailFragment::class.java.simpleName)
//        fragment.setOnUpdateListener {
//            getTasks()
//        }
    }

    private fun getTasks() {
        homeViewModel.getAllTasks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}