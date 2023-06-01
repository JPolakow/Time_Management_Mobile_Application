package com.example.opsc_part2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.LinearLayout

class Dashboard : AppCompatActivity(), QuickActionPopup.DashboardFragmentListener {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // ---------- Declarations ---------- //
        bottomNav = findViewById(R.id.bottomNavView)
        val actionButt = findViewById<FloatingActionButton>(R.id.btnPlus)
        val linView = findViewById<LinearLayout>(R.id.linearProjectCards)
        val fragment = QuickActionPopup()
        var isFragmentVisible = false // Var to hold fragment visibility state
        // ---------- End Declarations ----------

        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Menu_Stats -> {
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.add(R.id.container, Statistics())
                    transaction.commit()
                    // Load fragment to select activity or group
                    //loadFragment(fragment)
                    Log.d("ANGELO-------------------------------", "loadFragment is being executed!")
                    //loadFragment(Statistics())
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

        val activityCard1 = custom_dashboard_cards(this)
        activityCard1.setActivityName("Testeroo")
        activityCard1.setActivityStartDate("2023/04/05")
        activityCard1.setCardColor("green")
            //val linView = findViewById<LinearLayout>(R.id.linearProjectCards)
        linView.addView(activityCard1)

        val activityCard2 = custom_dashboard_cards(this)
        activityCard2.setActivityName("One")
        activityCard2.setActivityStartDate("2023/04/05")
        activityCard2.setCardColor("green")
        //val linView = findViewById<LinearLayout>(R.id.linearProjectCards)
        linView.addView(activityCard2)

        val activityCard3 = custom_dashboard_cards(this)
        activityCard3.setActivityName("Two")
        activityCard3.setActivityStartDate("2023/04/05")
        activityCard3.setCardColor("green")
        //val linView3 = findViewById<LinearLayout>(R.id.linearProjectCards)
        linView.addView(activityCard3)

        val activityCard4 = custom_dashboard_cards(this)
        activityCard4.setActivityName("Three")
        activityCard4.setActivityStartDate("2023/04/05")
        activityCard4.setCardColor("green")
      //  val linView4 = findViewById<LinearLayout>(R.id.linearProjectCards)
        linView.addView(activityCard4)

        val activityCard5 = custom_dashboard_cards(this)
        activityCard5.setActivityName("Three")
        activityCard5.setActivityStartDate("2023/04/05")
        activityCard5.setCardColor("green")
        //  val linView4 = findViewById<LinearLayout>(R.id.linearProjectCards)
        linView.addView(activityCard5)

        val activityCard6 = custom_dashboard_cards(this)
        activityCard6.setActivityName("Three")
        activityCard6.setActivityStartDate("2023/04/05")
        activityCard6.setCardColor("green")
        //  val linView4 = findViewById<LinearLayout>(R.id.linearProjectCards)
        linView.addView(activityCard6)

        // ----------------- END OF CUSTOM CARD ----------------- //


        /*
        * If fragment is visible, hide when button is clicked
        * Else if fragment is not visible when button clicked, then show fragment
        * */
        actionButt.setOnClickListener{
                showPopup()
                //isFragmentVisible = true  // Setting visible to true if fragment is shown | Was only used with other load method
        }

        // Set the initial fragment
       // loadFragment(Dashboard())
    }



    override fun onFragmentRequested(fragment: Fragment) {
        // Replace the current fragment on the dashboard with the requested fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun showPopup()
    {
        val fragmentt = QuickActionPopup()
        fragmentt.show(supportFragmentManager, "QuickActionPopup")


    }
    private fun loadFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, Fragment())
        transaction.commit()
    }
}
// ------------------------ OLD WAY TO LOAD FRAGMENT | DOES WORK ------------------------ //
/* val fragmentManager = supportFragmentManager
                 val transaction = fragmentManager.beginTransaction()
                 transaction.add(R.id.fragment_container, fragment)
                 transaction.commit()
                 // Load fragment to select activity or group
                 //loadFragment(fragment)
                 Log.d("ANGELO-------------------------------", "loadFragment is being executed!")
                 isFragmentVisible = true*/
// ------------------------------------------------------------------------------------- //