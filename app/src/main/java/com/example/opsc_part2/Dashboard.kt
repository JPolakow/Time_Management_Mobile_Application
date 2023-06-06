package com.example.opsc_part2

import Classes.ToolBox
import Classes.WorkEntriesObject
import android.annotation.SuppressLint
import android.content.*
import android.content.DialogInterface
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.example.opsc_part2.databinding.ActivityDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.min
import kotlin.math.roundToInt

class Dashboard : AppCompatActivity(), QuickActionPopup.DashboardFragmentListener {

    //ui vars
    private lateinit var actionButt: FloatingActionButton
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var tvCategory: TextView
    private lateinit var linView: LinearLayout
    private lateinit var tvActNameTime: TextView

    private var TimerOutput: String = "00:00:00"
    private var TimerName: String = ""

    //timer vars
    private fun makeTimeString(hour: Int, min: Int, sec: Int): String =
        String.format("%02d:%02d:%02d", hour, min, sec)

    private lateinit var binding: ActivityDashboardBinding
    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    //============================================================================
    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // ======================= Declarations ======================= //
        //binding
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ui
        bottomNav = findViewById(R.id.bottomNavView)
        actionButt = findViewById(R.id.btnPlus)
        tvCategory = findViewById(R.id.tvCategory)
        linView = findViewById(R.id.linearProjectCards)
        tvActNameTime = findViewById(R.id.tvDisplayTime)

        // Obtain a reference to the ImageView
        val imgProfileImg = findViewById<ImageView>(R.id.imgProfileImg)

        // Create a Bitmap from the image drawable
        // This is where you change size of image
        val maxImageSize = 140

        //timer decs
        serviceIntent = Intent(this, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

        // ======================= End Declarations ======================= //

        imgProfileImg.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }


        val displayCategoryText = "Category: ${ToolBox.SelectedCategory}"
        tvCategory.text = displayCategoryText
        tvCategory.setOnClickListener {
            showCategoryPickerDialog { selectedCategory ->
                val showCategoryText = "Category: $selectedCategory"
                tvCategory.text = showCategoryText

                loadCustomUI()
            }
        }

        //---------------------------SET UP BOTTOM UI-------------------------
        //region
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
        //endregion
        //------------------------------------END OF BOTTOM UI---------------------------------

        // ----------------- MENU ----------------------------
        //region
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
        //endregion
        // ----------------- END OF MENU -----------------------------------

        loadCustomUI()

        actionButt.setOnClickListener {
            showPopup()
        }
    }

    //=========================================================
    //load custom ui elements
    private fun loadCustomUI() {
        linView.removeAllViews()


        // ----------------- Creating a new card with custom attributes ----------------- //
        for (card in ToolBox.ActivitiesList) {
            if (card.ActivityUserID == ToolBox.ActiveUserID && card.ActivityCategory.equals(
                    ToolBox.SelectedCategory
                )
            ) {
                val customCard = custom_dashboard_cards(this)
                customCard.setActivityName(card.ActivityName)
                customCard.setActivityStartDate(card.DateCreated)
                customCard.setCardColor(card.ActivityColor) // not dynamically added
                customCard.setActivityMinGoal("Min Goal: " + card.ActivityMinGoal)
                customCard.setActivityMaxGoal("Max Goal: " + card.ActivityMaxGoal)

                val completeActivity = customCard.findViewById<ImageButton>(R.id.ibFinish)


                //complete the activity
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

                //timer
                val ibPausePlay = customCard.findViewById<ImageButton>(R.id.ibPausePlay)
                ibPausePlay.setOnClickListener()
                {
                    if (timerStarted && card.ActivityName.equals(TimerName)) {
                        stopTimer()
                        ibPausePlay.setImageResource(R.drawable.play_circle_48px)
                    } else if (timerStarted) {
                        stopTimer()
                        resetTimer()
                        TimerName = card.ActivityName
                        startTimer()
                    } else {
                        TimerName = card.ActivityName
                        startTimer()
                        ibPausePlay.setImageResource(R.drawable.pause_circle_48px)
                    }
                }

                //add to the page
                linView.addView(customCard)
            }
        }
        // ----------------- END OF CUSTOM CARDS -------------------
    }

    //============================================================================
    //start the timer
    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        startService(serviceIntent)
        timerStarted = true
    }

    //============================================================================
    //stop the timer
    private fun stopTimer() {
        stopService(serviceIntent)
        timerStarted = false
    }

    //============================================================================
    //get the data form the service and update textview
    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            tvActNameTime.text = getTimeStringFromDouble(time)
        }
    }

    //============================================================================
    //reset the timer
    private fun resetTimer() {
        stopTimer()
        time = 0.0
        TimerOutput = getTimeStringFromDouble(time)
    }

    //============================================================================
    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
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

    //============================================================================
    //catagory picker
    private fun showCategoryPickerDialog(callback: (String) -> Unit) {

        val catagoryNames = mutableListOf<String>()

        for (item in ToolBox.CategoryList) {
            val secondIndexEntry = item.CategoryName
            catagoryNames.add(secondIndexEntry)
        }

        var displaySelected = "Catagory: ";

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick a catagory")
            .setItems(catagoryNames.toTypedArray()) { dialog: DialogInterface, which: Int ->
                val selectedCatagory = which

                ToolBox.SelectedCategory = catagoryNames[selectedCatagory]
                displaySelected += ToolBox.SelectedCategory
                tvCategory.setText(displaySelected)
                callback(ToolBox.SelectedCategory)

                dialog.dismiss()
            }.setCancelable(false)

        val dialog = builder.create()
        dialog.show()
    }
}
