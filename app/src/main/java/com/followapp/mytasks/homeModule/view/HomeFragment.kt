package com.followapp.mytasks.homeModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
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

class HomeFragment : Fragment(), OnTaskClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var tasksAdapter: TaskListAdapter
    private var toggleMenuItem: MenuItem? = null

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
        setupListeners()
        setupMenu()
    }

    private fun setupViewModel() {
        homeViewModel = ViewModelProvider(this, HomeViewModelFactory(HomeRepository(HomeRoomDatabase())))[HomeViewModel::class.java]
        homeViewModel.getAllTasks()
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
            }
            toggleEmptyState(tasks.isEmpty())
        })

        homeViewModel.isGrouped.observe(viewLifecycleOwner, Observer { isGrouped ->
            toggleMenuItem?.icon = if (isGrouped) {
                ResourcesCompat.getDrawable(resources, R.drawable.ic_toggle_on, null)
            } else {
                ResourcesCompat.getDrawable(resources, R.drawable.ic_toggle_off, null)
            }
            requireActivity().invalidateOptionsMenu()  // to update the options menu dynamically. requireActivity() is needed when working with fragments
        })
    }

    private fun setupListeners() {
        binding.fabAddTask.setOnClickListener {
            parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, DetailFragment())
            .addToBackStack(null)
            .commit()
        }
    }

    // If there are no tasks, show an image and a text as the empty state. Otherwise, show the tasks.
    private fun toggleEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.recyclerViewTasks.visibility = View.GONE
            binding.emptyStateImage.visibility = View.VISIBLE
            binding.emptyStateMessage.visibility = View.VISIBLE
        } else {
            binding.recyclerViewTasks.visibility = View.VISIBLE
            binding.emptyStateImage.visibility = View.GONE
            binding.emptyStateMessage.visibility = View.GONE
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
                toggleMenuItem = menu.findItem(R.id.action_toggle_group)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_toggle_group -> {
                        homeViewModel.toggleGrouping()
                        true
                    }

                    R.id.action_sort_title_az -> {
                        homeViewModel.sortTasks("az")
                        true
                    }

                    R.id.action_sort_title_za -> {
                        homeViewModel.sortTasks("za")
                        true
                    }

                    else -> false
                }
            }

            override fun onPrepareMenu(menu: Menu) {
                toggleMenuItem?.icon = if (homeViewModel.isGrouped.value == true) {
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_toggle_on, null)
                } else {
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_toggle_off, null)
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    // To be implemented for the OnTaskClickListener
    override fun onTaskClick(task: Task) {
        val detailFragment = DetailFragment()
        val args = Bundle()
        args.putLong("taskId", task.id)
        detailFragment.arguments = args
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_view, detailFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    // To be implemented for the OnTaskClickListener
    override fun onTaskCheckBoxClick(task: Task) {
        homeViewModel.toggleTaskDone(task, tasksAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}