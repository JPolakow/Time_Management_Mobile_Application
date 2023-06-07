package com.example.opsc_part2

import Classes.ToolBox
import android.annotation.SuppressLint
import android.content.*
import android.content.DialogInterface
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.example.opsc_part2.databinding.ActivityDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import kotlin.math.min
import kotlin.math.roundToInt

class Dashboard : AppCompatActivity(), QuickActionPopup.DashboardFragmentListener,
    complete_activity.CompleteActivityCallback {

    private lateinit var binding: ActivityDashboardBinding

    //UI vars
    private lateinit var actionButt: FloatingActionButton
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var tvCategory: TextView
    private lateinit var linView: LinearLayout
    private lateinit var tvActNameTime: TextView
    private lateinit var tvDisplayActivityName: TextView

    //Timer vars
    private fun makeTimeString(hour: Int, min: Int, sec: Int): String =
        String.format("%02d:%02d:%02d", hour, min, sec)

    private var time = 0.0
    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    private var TimerOutput: String = "00:00:00"
    private var TimerName: String = ""

    //============================================================================
    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        try {

            // ======================= Declarations ======================= //
            //Binding
            binding = ActivityDashboardBinding.inflate(layoutInflater)
            setContentView(binding.root)

            //ui
            bottomNav = findViewById(R.id.bottomNavView)
            actionButt = findViewById(R.id.btnPlus)
            tvCategory = findViewById(R.id.tvCategory)
            linView = findViewById(R.id.linearProjectCards)
            tvActNameTime = findViewById(R.id.tvDisplayTime)
            tvDisplayActivityName = findViewById(R.id.tvActivityNameTimer)

            // Obtain a reference to the ImageView
            val imgProfileImg = findViewById<ImageView>(R.id.imgProfileImg)

            // Create a Bitmap from the image drawable
            // This is where you change size of image
            val maxImageSize = 140

            //timer decks
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
            val drawable = resources.getDrawable(R.drawable.profile_image_placeholder2) as BitmapDrawable
            val bitmap = drawable.bitmap

            // Calculate the desired size for the circular ImageView, considering the maximum size
            val imageSize = min(bitmap.width, bitmap.height)
            val scaleFactor = maxImageSize.toFloat() / imageSize.toFloat()
            val targetSize = (imageSize * scaleFactor).toInt()

            // Resize the original image bitmap to the target size
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetSize, targetSize, true)

            // Create a circular Bitmap with the desired size
            val circularBitmap =
                Bitmap.createBitmap(targetSize, targetSize, Bitmap.Config.ARGB_8888)
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
                        val fragmentManager = supportFragmentManager
                        val transaction = fragmentManager.beginTransaction()
                        transaction.add(R.id.container, Statistics())
                        transaction.commit()
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

        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //=========================================================
    //load custom ui elements
    private fun loadCustomUI() {
        try {
            linView.removeAllViews()

            val filteredCategories = ToolBox.ActivitiesList.filter { activity ->
                activity.ActivityUserID == ToolBox.ActiveUserID
            }.filter { activity ->
                ToolBox.SelectedCategory.equals("None") || activity.ActivityCategory == ToolBox.SelectedCategory
            }

            //===== CREATE NEW CARDS FOR EACH ACTIVITY =====
            for (card in filteredCategories) {
                val customCard = custom_dashboard_cards(this)
                //===== LOAD DATA INTO THE UIs =====
                customCard.setActivityName(card.ActivityName)
                customCard.setActivityStartDate(card.DateCreated)
                customCard.setCardColor(card.ActivityColor) // not dynamically added
                customCard.setActivityMinGoal(
                    "Min Goal: " + String.format(
                        "%.1f", card.ActivityMinGoal / 60
                    ) + "hrs"
                )
                customCard.setActivityMaxGoal(
                    "Max Goal: " + String.format(
                        "%.1f", card.ActivityMaxGoal / 60
                    ) + "hrs"
                )

                val cardTile = customCard.findViewById<CardView>(R.id.cardView)

                //===== DESCRIPTION =====
                val popupView = LayoutInflater.from(applicationContext)
                    .inflate(R.layout.popup_description, null)
                val tvDescription = popupView.findViewById<TextView>(R.id.tvDescription)
                val descriptionToDisplay = "Description: ${card.ActivityDescription}"
                tvDescription.text = descriptionToDisplay
                val popupWindow = PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                popupWindow.isFocusable = true
                popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))

                cardTile.setOnClickListener {
                    popupWindow.showAsDropDown(customCard)
                }

                //===== STOP TIMER BUTTON =====
                val stopActivity = customCard.findViewById<ImageButton>(R.id.ibStop)
                //Complete the activity
                stopActivity.setOnClickListener {

                    stopTimer()
                    val fragment = complete_activity()

                    GlobalScope.launch {
                        withContext(Dispatchers.Main) {
                            // Put data into fragment
                            val args = Bundle()
                            args.putString("color", card.ActivityColor)
                            args.putDouble("duration", time)
                            args.putInt("id", card.ActivityID)
                            args.putString("name", card.ActivityName)
                            args.putString("category", card.ActivityCategory)

                            fragment.arguments = args
                            fragment.show(supportFragmentManager, "completeActivity")
                        }
                    }
                }

                //===== ADD NEW TIMER BUTTON =====
                val addNewEntry = customCard.findViewById<Button>(R.id.AddNewEntry)
                //Add new entry
                addNewEntry.setOnClickListener {
                    val fragment = complete_activity()

                    GlobalScope.launch {
                        withContext(Dispatchers.Main) {
                            val returnType = showTimePickerDialogMin().toDouble()
                            // Put data into fragment
                            val args = Bundle()
                            args.putString("color", card.ActivityColor)
                            args.putDouble("duration", returnType)
                            args.putInt("id", card.ActivityID)
                            args.putString("name", card.ActivityName)
                            args.putString("category", card.ActivityCategory)

                            fragment.arguments = args
                            fragment.show(supportFragmentManager, "completeActivity")
                        }
                    }
                }

                //===== PAUSE BUTTON =====
                val ibPause = customCard.findViewById<ImageButton>(R.id.ibPause)
                ibPause.setOnClickListener() {
                    stopTimer()
                    Log.d("timer", "started")
                }

                //===== TIMER =====
                val ibPausePlay = customCard.findViewById<ImageButton>(R.id.ibPausePlay)
                ibPausePlay.setOnClickListener() {
                    tvDisplayActivityName.text = card.ActivityName
                    startTimer()
                }

                //===== ADD CARD TO PAGE =====
                linView.addView(customCard)
            }
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    override fun onActivityComplete() {
        stopTimer()
        resetTimer()
    }

    //TIMERS
    //region
    //============================================================================
    //start the timer
    private fun startTimer() {
        Log.d("timer", "started1")
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        startService(serviceIntent)
        timerStarted = true
        Log.d("timer", "started2")
    }

    //============================================================================
    //Stop the timer
    private fun stopTimer() {
        stopService(serviceIntent)
        timerStarted = false
    }

    //============================================================================
    //Get the data form the service and update textview
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
    //endregion

    //============================================================================
    //Category picker
    private fun showCategoryPickerDialog(callback: (String) -> Unit) {

        val categoryNames = mutableListOf<String>()
        categoryNames.add("None")

        ToolBox.CategoryList.forEach { category ->
            if (category.CategoryUserID == ToolBox.ActiveUserID) {
                categoryNames.add(category.CategoryName)
            }
        }

        var displaySelected = "Catagory: ";

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick a catagory")
            .setItems(categoryNames.toTypedArray()) { dialog: DialogInterface, which: Int ->

                ToolBox.SelectedCategory = categoryNames[which]
                displaySelected += ToolBox.SelectedCategory
                tvCategory.text = displaySelected
                callback(ToolBox.SelectedCategory)

                dialog.dismiss()
            }.setCancelable(false)

        val dialog = builder.create()
        dialog.show()
    }

    //============================================================================
    //Time picker
    private suspend fun showTimePickerDialogMin(): Double {
        return withContext(Dispatchers.Main) {
            val hours = arrayOf(
                "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
            )
            val hourPicker = NumberPicker(this@Dashboard)
            hourPicker.apply {
                minValue = 0
                maxValue = hours.size - 1
                displayedValues = hours
                wrapSelectorWheel = true
            }

            val minutes =
                arrayOf("00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55")
            val minutePicker = NumberPicker(this@Dashboard)
            minutePicker.apply {
                minValue = 0
                maxValue = minutes.size - 1
                displayedValues = minutes
                wrapSelectorWheel = true
            }

            val layout = LinearLayout(this@Dashboard)
            layout.orientation = LinearLayout.HORIZONTAL
            layout.addView(hourPicker)
            layout.addView(minutePicker)

            val selectedTimeDeferred = CompletableDeferred<Double>()

            val alertDialog =
                android.app.AlertDialog.Builder(this@Dashboard, R.style.CenteredDialog)
                    .setTitle("Select Time")
                    .setView(layout)
                    .setPositiveButton("OK") { _, _ ->
                        val selectedHour = hours[hourPicker.value].toDouble()
                        val selectedMinute = minutes[minutePicker.value].toDouble()
                        val selectedTime = selectedHour * 60 * 60 + selectedMinute * 60
                        selectedTimeDeferred.complete(selectedTime)
                    }
                    .setNegativeButton("Cancel") { _, _ ->
                        selectedTimeDeferred.complete(Double.NaN)
                    }
                    .create()

            alertDialog.setOnDismissListener {
                if (alertDialog.isShowing) {
                    alertDialog.dismiss()
                    selectedTimeDeferred.complete(Double.NaN)
                }
            }

            alertDialog.show()
            selectedTimeDeferred.await()
        }
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
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    //============================================================================
    private fun showPopup() {
        val fragment = QuickActionPopup()
        fragment.show(supportFragmentManager, "QuickActionPopup")
    }

}



