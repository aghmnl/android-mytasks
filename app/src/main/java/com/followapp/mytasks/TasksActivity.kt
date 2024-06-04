package com.followapp.mytasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class TasksActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var addTaskButton: FloatingActionButton
    private lateinit var advertView: AdView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView


    private val tasksAdapter = TasksAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

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

        initToolBar()
        initBanner()

    }

    private fun initToolBar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.bar_title, R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()
        initNavigationView()
    }

    private fun initNavigationView() {
        navigationView = findViewById(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener(this)
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_login -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        // Para cerrar el menú luego de elegir la opción
        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }
}