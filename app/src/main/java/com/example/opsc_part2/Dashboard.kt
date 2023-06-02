package com.example.opsc_part2

import Classes.ActivityObject
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.LinearLayout
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
        // Initialising object list to a new Val
        val listActivities = createActivityObjects()

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

        for (card in listActivities) {
            val customCard = custom_dashboard_cards(this)
            customCard.setActivityName(card.ActivityName)
            customCard.setActivityStartDate(card.DateCreated)
            customCard.setCardColor("green") // not dynamically added
            customCard.setActivityMinGoal("Min Goal: " + card.ActivityMinGoal)
            customCard.setActivityMaxGoal("Max Goal: " + card.ActivityMaxGoal)
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
    private fun createActivityObjects(): List<ActivityObject> {

        // Creating correct date format
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val current = LocalDateTime.now().format(formatter)

        // Initialising values for object list
        val ActivityObjectList = listOf(
            ActivityObject("1", "1", "Open-Source", current, "2", "4"),
            ActivityObject("2", "2", "Programming", current, "6", "8"),
            ActivityObject("3", "3", "Research", current, "1", "3"),
            ActivityObject("4", "4", "Project Management", current, "3", "6")
        )

        // Returning Object List
        return ActivityObjectList
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
}