package com.example.opsc_part2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.opsc_part2.Dashboard
import com.example.opsc_part2.Statistics
import com.example.opsc_part2.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception

class Dashboard : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        bottomNav = findViewById(R.id.bottomNavView)

        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Menu_Stats -> {
                    loadFragment(Statistics())
                    true
                }
                R.id.Menu_Dashboard -> {
                    // Do nothing since you're already on the dashboard
                    true
                }
                else -> false
            }
        }

        // Set the initial fragment
       // loadFragment(Dashboard())
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.relParental, fragment)
            .commit()
    }
}