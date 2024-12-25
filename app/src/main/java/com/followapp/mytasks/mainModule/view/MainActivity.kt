package com.followapp.mytasks.mainModule.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.followapp.mytasks.R
import com.followapp.mytasks.adModule.view.AdFragment
import com.followapp.mytasks.homeModule.view.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
//    private lateinit var navigationView: NavigationView
//
//    private lateinit var credentialManager: CredentialManager
//    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolBar()
        launchHome()
        loadAdFragment()
    }

    fun launchHome() {
        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().apply {
            add(R.id.container_main, homeFragment)
                .commit()
        }
    }

    private fun loadAdFragment() {
        val adFragment = AdFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.ad_fragment_container, adFragment)
            .commit()
    }

    private fun initToolBar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

//        drawerLayout = findViewById(R.id.drawer_layout)
//
//        val toggle = ActionBarDrawerToggle(
//            this, drawerLayout, toolbar, R.string.bar_title, R.string.navigation_drawer_close
//        )
//
//        drawerLayout.addDrawerListener(toggle)
//
//        toggle.syncState()
//        initNavigationView()
    }

//    private fun initNavigationView() {
//        navigationView = findViewById(R.id.nav_view)
//        navigationView.setNavigationItemSelectedListener(this)
//    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.nav_login -> {
//                val intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//            }
//
//            R.id.nav_logout -> {
//                logout()
//            }
//        }
//
//        drawerLayout.closeDrawer(GravityCompat.START)
//        return true
//    }

//    private fun logout() {
//        lifecycleScope.launch {
//            try {
//                credentialManager = CredentialManager.create(this@MainActivity)
//                auth = FirebaseAuth.getInstance()
//
//                credentialManager.clearCredentialState(ClearCredentialStateRequest())
//                auth.signOut()
//                println("LOGOUT")
//
//            } catch (e: GetCredentialException) {
//                println("THERE WAS AN ERROR LOGOUT")
//            }
//        }
//    }
}
