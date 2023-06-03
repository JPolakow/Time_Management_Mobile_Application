package com.example.opsc_part2

import Classes.ActivityObject
import TimerManager
import Classes.ToolBox
import android.annotation.SuppressLint
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
    @SuppressLint("UseCompatLoadingForDrawables")
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

        // Obtain a reference to the ImageView
        val imgProfileImg = findViewById<ImageView>(R.id.imgProfileImg)
        // Create a Bitmap from the image drawable
        // This is where you change size of image
        val maxImageSize = 140
        // ======================= End Declarations ======================= //
        imgProfileImg.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

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
        params.leftMargin = 50
        params.topMargin = 25
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
            customCard.setCardColor(card.ActivityColor) // not dynamically added
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
    private fun showPopup() {
        val fragment = QuickActionPopup()
        fragment.show(supportFragmentManager, "QuickActionPopup")
    }
}