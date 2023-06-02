package com.example.opsc_part2

import Classes.ActivityObject
import Classes.ToolBox
import android.content.Intent
import android.graphics.Matrix
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Dashboard : AppCompatActivity(), QuickActionPopup.DashboardFragmentListener {

    //ui vars
    private lateinit var bottomNav: BottomNavigationView

    //============================================================================
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // ======================= Declarations ======================= //
        bottomNav = findViewById(R.id.bottomNavView)
        val actionButt = findViewById<FloatingActionButton>(R.id.btnPlus)
        val linView = findViewById<LinearLayout>(R.id.linearProjectCards)
        val fragment = QuickActionPopup()
        // Var to hold fragment visibility state
        var isFragmentVisible = false
        // Initialising object list
        createActivityObjects()

        // ======================= End Declarations ======================= //
        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Menu_Stats -> {
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.add(R.id.container, Statistics())
                    transaction.commit()
                    true
                }
                R.id.Menu_Dashboard -> {
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                    true
                }
                R.id.Menu_Settings -> {
                    val intent = Intent(this, Settings::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // ----------------- Creating a new card with custom attributes ----------------- //
        for (card in ToolBox.ActivitiesList) {
            val customCard = custom_dashboard_cards(this)
            customCard.setActivityName(card.ActivityName)
            customCard.setActivityStartDate(card.DateCreated)
            customCard.setCardColor("green") // not dynamically added
            customCard.setActivityMinGoal("Min Goal: " + card.ActivityMinGoal)
            customCard.setActivityMaxGoal("Max Goal: " + card.ActivityMaxGoal)

            val timerText = customCard.findViewById<TextView>(R.id.txtTimerTick)
            timerText.text = "00:00:00";

            val play = customCard.findViewById<ImageButton>(R.id.ibPausePlay)

            play.setOnClickListener {
                startTimer(customCard, timerText)
            }

            linView.addView(customCard)
        }

        /*
        * If fragment is visible, hide when button is clicked
        * Else if fragment is not visible when button clicked, then show fragment
        * */
        actionButt.setOnClickListener {
            showPopup()
            //isFragmentVisible = true  // Setting visible to true if fragment is shown | Was only used with other load method
        }
    }

    //============================================================================
    override fun onFragmentRequested(fragment: Fragment) {
        // Replace the current fragment on the dashboard with the requested fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    //============================================================================
    // Method to Initialise object list - Is being called on page start
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createActivityObjects(){

        // Creating correct date format
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val current = LocalDateTime.now().format(formatter)

        val newActitivy = ActivityObject("1", "1", "Open-Source", current, "2", "4")
        ToolBox.ActivitiesList.add(newActitivy)
        val newActitivy2 = ActivityObject("2", "2", "Programming", current, "6", "8")
        ToolBox.ActivitiesList.add(newActitivy2)
        val newActitivy3 = ActivityObject("3", "3", "Research", current, "1", "3")
        ToolBox.ActivitiesList.add(newActitivy3)
        val newActitivy4 = ActivityObject("4", "4", "Project Management", current, "3", "6")
        ToolBox.ActivitiesList.add(newActitivy4)
    }

    //============================================================================


    private fun showPopup() {
        val fragment = QuickActionPopup()
        fragment.show(supportFragmentManager, "QuickActionPopup")
    }

    //============================================================================
    private fun loadFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, Fragment())
        transaction.commit()
    }

    private fun startTimer(customCard: custom_dashboard_cards, timerText: TextView) {
        val handler = Handler()
        var seconds = 0

        val runnable = object : Runnable {
            override fun run() {
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                val secondsText = (seconds % 60).toString().padStart(2, '0')
                val timerValue = "$hours:$minutes:$secondsText"

                timerText.text = timerValue

                seconds++
                handler.postDelayed(this, 1000)
            }
        }

        handler.post(runnable)
    }
    //============================================================================
}