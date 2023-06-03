package com.example.opsc_part2

import Classes.ActivityObject
import TimerManager
import Classes.ToolBox
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
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
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Collections.min
import kotlin.math.min

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
        // Obtain a reference to the ImageView
        val imgProfileImg = findViewById<ImageView>(R.id.imgProfileImg)
        // Define the maximum size for the image (in pixels)
        val maxImageSize = 140 // Set your desired maximum size value here


// Create a Bitmap from the image drawable
        val drawable = resources.getDrawable(R.drawable.temp_profilepicture) as BitmapDrawable
        val bitmap = drawable.bitmap

// Calculate the desired size for the circular ImageView, considering the maximum size
        val imageSize = min(bitmap.width, bitmap.height)
        val scaleFactor = maxImageSize.toFloat() / imageSize.toFloat()
        val targetSize = (imageSize * scaleFactor).toInt()

        // Resize the original image bitmap to the target size
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetSize, targetSize, true)


// Create a circular Bitmap with the desired size
        val circularBitmap = Bitmap.createBitmap(targetSize, targetSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(circularBitmap)


// Create a Paint object to define the circular shape
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(resizedBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

// Draw a circular shape on the canvas using the Paint
        val radius = targetSize / 2f
        canvas.drawCircle(radius, radius, radius, paint)

// Set the circular Bitmap as the new image for the ImageView
        imgProfileImg.setImageBitmap(circularBitmap)

// Set the dimensions of the ImageView to match the circular Bitmap
        val params = RelativeLayout.LayoutParams(targetSize, targetSize)
        params.leftMargin = 30
        params.topMargin = 10
        imgProfileImg.layoutParams = params



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
                TimerManager.startTimer(customCard, timerText)
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
    private fun createActivityObjects() {

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

    //============================================================================
    private fun startTimer(customCard: custom_dashboard_cards, timerText: TextView) {
        val handler = Handler()
        var seconds = 0

        val runnable = object : Runnable {
            override fun run() {
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                val secondsText = (seconds % 60).toString().padStart(2, '0')
                val timerValue = "$hours:$minutes:$secondsText"

                //============================================================================
            }
        }
    }
}