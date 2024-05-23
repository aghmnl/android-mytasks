package com.followapp.mytasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TasksActivity : AppCompatActivity() {
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var addTaskButton: FloatingActionButton
    private lateinit var advertView: AdView


    private val tasksAdapter = TasksAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

       initBanner()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // This is to avoid dark mode

        TaskManager.initialize(this)

        tasksRecyclerView = findViewById(R.id.recyclerViewTasks)
        addTaskButton = findViewById(R.id.fabAddTask)

        tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        tasksRecyclerView.adapter = tasksAdapter

        addTaskButton.setOnClickListener {
            TaskManager.selectedTaskIndex = -1

            // Handle click event to add new task
            val intent = Intent(this, TaskDetail::class.java)
            startActivity(intent)
        }

        TaskManager.onItemInserted = { tasksAdapter.notifyItemInserted(TaskManager.tasksList.size) }
        TaskManager.onItemChanged = { tasksAdapter.notifyItemRangeChanged(TaskManager.selectedTaskIndex, TaskManager.tasksList.size) }
        TaskManager.onItemRemoved = { tasksAdapter.notifyItemRemoved(TaskManager.selectedTaskIndex) }

    }

    // To load the ads banner
    private fun initBanner() {
        MobileAds.initialize(this) {}
        advertView = AdView(this)
        advertView.setAdSize(AdSize.BANNER)
        advertView.adUnitId = "ca-app-pub-3940256099942544/9214589741"  // Banner test AdMob ID
//        advertView.adUnitId = "ca-app-pub-5163472824682213~4420222935"  // FollowApp MyTasks AdMob ID

        val lyAdsBanner = findViewById<FrameLayout>(R.id.ad_view_container)
        lyAdsBanner.addView(advertView)

        val adRequest = AdRequest.Builder().build()
        advertView.loadAd(adRequest)

    }
}