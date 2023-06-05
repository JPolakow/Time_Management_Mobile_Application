package com.example.opsc_part2

import Classes.ToolBox

import TimerManager
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.min

class Dashboard : AppCompatActivity(), QuickActionPopup.DashboardFragmentListener {

    //ui vars
    private lateinit var actionButt: FloatingActionButton
    private lateinit var bottomNav: BottomNavigationView

    //============================================================================
    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // ======================= Declarations ======================= //
        bottomNav = findViewById(R.id.bottomNavView)
        actionButt = findViewById<FloatingActionButton>(R.id.btnPlus)

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

        // ----------------- MENU ----------------------------
        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Menu_Stats -> {
                    var toast = Toast.makeText(
                        this,
                        "Stats feature not available in prototype",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                    true
                }
                R.id.Menu_Dashboard -> {
                    val intent = Intent(this, Dashboard::class.java)
                    val options = ActivityOptionsCompat.makeCustomAnimation(this, 0, 0)
                    ActivityCompat.startActivity(this, intent, options.toBundle())
                    true
                }
                R.id.Menu_Settings -> {
                    val intent = Intent(this, Settings::class.java)
                    val options = ActivityOptionsCompat.makeCustomAnimation(this, 0, 0)
                    ActivityCompat.startActivity(this, intent, options.toBundle())
                    true
                }
                R.id.Menu_Logs -> {
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.add(R.id.container, Logs())
                    transaction.commit()
                    true
                }
                else -> false
            }
        }
        // ----------------- END OF MENU -----------------------------------

        // ----------------- Creating a new card with custom attributes ----------------- //
        for (card in ToolBox.ActivitiesList) {
            if (card.ActivityUserID == ToolBox.ActiveUserID) {
                val customCard = custom_dashboard_cards(this)
                customCard.setActivityName(card.ActivityName)
                customCard.setActivityStartDate(card.DateCreated)
                customCard.setCardColor(card.ActivityColor) // not dynamically added
                customCard.setActivityMinGoal("Min Goal: " + card.ActivityMinGoal)
                customCard.setActivityMaxGoal("Max Goal: " + card.ActivityMaxGoal)

                val timerText = customCard.findViewById<TextView>(R.id.txtTimerTick)
                timerText.text = "00:00:00";

                val play = customCard.findViewById<ImageButton>(R.id.ibPausePlay)
                val completeActivity = customCard.findViewById<ImageButton>(R.id.ibFinsih)

                play.setOnClickListener {
                    if (customCard.isTimerRunning) {
                        // Pause the timer
                        TimerManager.pauseTimer(this, customCard)
                        play.setImageResource(R.drawable.play_circle_48px)
                        customCard.isTimerRunning = true
                    } else {
                        // Start the timer
                        TimerManager.startTimer(this ,customCard, timerText)
                        play.setImageResource(R.drawable.pause_circle_48px)
                        customCard.isTimerRunning = false

                    }
                    customCard.isTimerRunning = !customCard.isTimerRunning
                }


                completeActivity.setOnClickListener {
                    val fragment = complete_activity()

                    // put data into fragment
                    val args = Bundle()

                    args.putString("color", card.ActivityColor)
                    args.putString("duration", "2")
                    args.putInt("id", card.ActivityID)
                    args.putString("name", card.ActivityName)

                    fragment.arguments = args
                    fragment.show(supportFragmentManager, "completeActivity")
                }

                linView.addView(customCard)
            }
            }
        // ----------------- END OF CUSTOM CARDS -------------------


            /*
            * If fragment is visible, hide when button is clicked
            * Else if fragment is not visible when button clicked, then show fragment
            * */
            actionButt.setOnClickListener {
                showPopup()
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