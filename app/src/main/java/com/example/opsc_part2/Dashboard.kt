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

    lateinit var bottomNav : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        /*bottomNav.background = null  //Removing the background shadow
        bottomNav.menu.getItem(2).isEnabled = false*/

       /* loadFragment(Statistics())*/

        bottomNav = findViewById(R.id.bottomNavView) as BottomNavigationView

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.Stats -> {
                    loadFragment(Statistics())
                    true
                }R.id.Stats -> {
                startActivity(Intent(this, Dashboard::class.java))
                true
            }
                else -> {false}
            }
        }
    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.relParental,fragment)
        transaction.commit()
    }
}