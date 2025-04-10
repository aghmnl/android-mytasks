package com.followapp.mytasks.mainModule.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.followapp.mytasks.R
import com.followapp.mytasks.adModule.view.AdFragment
import com.followapp.mytasks.homeModule.view.HomeFragment
import com.followapp.mytasks.loginModule.view.LoginFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolBar()
        launchHome()
        loadAdFragment()
//        loadLoginFragment()
    }

    private fun launchHome() {
        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragment_container_view, homeFragment)
                .commit()
        }
    }

    private fun loadAdFragment() {
        val adFragment = AdFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.ad_fragment_container, adFragment)
            .commit()
    }

    private fun loadLoginFragment() {
        val loginFragment = LoginFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, loginFragment)
            .commit()
    }

    private fun initToolBar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
    }
}